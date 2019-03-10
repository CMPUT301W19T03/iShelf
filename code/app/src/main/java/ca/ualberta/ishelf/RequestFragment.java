package ca.ualberta.ishelf;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SharedMemory;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;

import static android.support.constraint.Constraints.TAG;

/**
 * Requests Activity
 *
 * US 04.02.01 (2)
 * As a borrower, I want to view a list of books I have requested, each book with its description, and owner username.
 * the borrower wants to see which books they have requested, and the associated information
 *
 * This Activity shows all the requests, sorted by pending and accepted
 *
 *
 * US 05.04.01
 * As a borrower, I want to view a list of books I have requested that are accepted, each book with its description, and owner username.
 * the borrower wants to see the list of books that have been accepted for his request to borrow
 *
 * This Acivity has a filter that can filter out all the accepted requests
 *
 *
 *
 * @author mehrab
 */

public class RequestFragment extends Fragment {
    private ArrayList<Request> requestPendingList = new ArrayList<Request>();
    private ArrayList<Request> requestAcceptedList = new ArrayList<Request>();
    private Spinner spinner;
    private RecyclerView requestRecyclerView;
    private RecyclerView.Adapter requestAdapter;
    private RecyclerView.LayoutManager requestLayoutManager;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_request, container, false);

        return view;

    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        spinner = getActivity().findViewById(R.id.spinner2);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();


                Request_Filter(selectedItem);


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        requestRecyclerView = (RecyclerView) getActivity().findViewById((R.id.request_recycler));
        requestLayoutManager = new LinearLayoutManager(this.getContext());
        requestRecyclerView.setLayoutManager(requestLayoutManager);
        requestAdapter = new RequestAdapter(this.getContext(), requestPendingList);
        requestRecyclerView.setAdapter(requestAdapter);

        // creates a line between each individual record in the list
        // https://developer.android.com/reference/android/support/v7/widget/DividerItemDecoration
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(requestRecyclerView.getContext(),
                LinearLayoutManager.VERTICAL);
        requestRecyclerView.addItemDecoration(dividerItemDecoration);
        createDummy();

        getRequests();


    }

    /**
     * get list requests for the logged in user and update list
     */
    public void getRequests(){

        // get logged in username
        String username = getActivity().getSharedPreferences("UserPreferences", Context.MODE_PRIVATE).getString("username", null);
        if (username == null){
            return;
        }

        //connect to firebase
        Database db = new Database(getContext());
        Firebase fb = db.connect(getContext());
        Firebase childRef = fb.child("Users").child(username);

        childRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String jUser = dataSnapshot.getValue(String.class);
                Log.d("jUser", jUser);
                if (jUser != null) {
                    // Get user object from Gson
                    Gson gson = new Gson();
                    Type tokenType = new TypeToken<User>(){}.getType();
                    User user = gson.fromJson(jUser, tokenType); // here is where we get the user object
                    requestPendingList.addAll(user.getListofRequests());
                    requestAdapter.notifyDataSetChanged();
                } else {
                    Log.d("FBerrorFragmentRequest", "User doesn't exist or string is empty");
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                return;
            }

        });
    }

    public void Request_Filter(String filter){ //this is going to filter strings for now, but should work whenever we pass in book/user

//filters between pending and accepted requests as wanted by the US
        if(filter.equals("Pending Requests")) {

            initRecyclerView(requestPendingList, this.getContext());



        }else if (filter.equals(("Accepted Requests"))){

            initRecyclerView(requestAcceptedList, this.getContext());
             // basically do nothing and go back to main page



        }
    }
    // Recycler view initialization
    //This resets the recycler view to a new ArrayList everytime
    private void initRecyclerView( ArrayList list, Context context){
        Log.d(TAG, "initRecyclerView: init recyclerview.");
        RecyclerView recyclerView = (RecyclerView) getActivity().findViewById(R.id.request_recycler);
        RequestAdapter adapter = new RequestAdapter( this.getContext(),list); //in the same order as the constructor in MyAdapter
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
    }
    private void enteredAlert(String msg) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this.getContext());
        alertDialogBuilder.setMessage((CharSequence) msg);
        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                // TODO Auto-generated catch block
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
//test dummy
    public void createDummy() {
//        Book testBook = new Book("Book 1", "Description", 1234L, "Year", "Genre", "author", false);
//        User testRequester = new User("ppl", "1234567", "someone@Email.com");
//        Rating rating = new Rating(3, "Okay");
//        testRequester.addRating(rating);
//        Request request1 = new Request(null, testRequester, testBook);
//        requestPendingList.add(request1);
//
//        Book test1Book = new Book("Book 3", "Description", 1234L, "Year", "Genre", "author", false);
//        User test1Requester = new User("people", "1234567", "someone@Email.com");
//        Rating rating1 = new Rating(3, "Okay");
//        testRequester.addRating(rating1);
//        Request request2 = new Request(null, test1Requester, test1Book);
//        requestAcceptedList.add(request2);



    }


}
