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
import java.util.UUID;

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
    private RequestAdapter requestAdapter;
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
        requestAdapter = new RequestAdapter(this.getContext());
        requestRecyclerView.setAdapter(requestAdapter);

        // creates a line between each individual record in the list
        // https://developer.android.com/reference/android/support/v7/widget/DividerItemDecoration
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(requestRecyclerView.getContext(),
                LinearLayoutManager.VERTICAL);
        requestRecyclerView.addItemDecoration(dividerItemDecoration);
        //createDummy();

        getRequests();


    }

    /**
     * get list requests for the logged in user and update list
     */
    public void getRequests(){

        requestPendingList.clear();

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
                    getBooks(requestPendingList);
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

    private void getBooks(final ArrayList<Request> requests){

        final ArrayList<Book> requestBooks = new ArrayList<>();

        //connect to firebase
        Database db = new Database(getContext());
        Firebase fb = db.connect(getContext());
        Firebase childRef = fb.child("Books");

        childRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot d: dataSnapshot.getChildren()) {
                    String jBook = d.getValue(String.class);
                    Log.d("jBook", jBook);
                    if (jBook != null) {
                        // Get book object from Gson
                        Gson gson = new Gson();
                        Type tokenType = new TypeToken<Book>() {}.getType();
                        Book book = gson.fromJson(jBook, tokenType); // here is where we get the user object
                        for (Request r : requests){
                            if (r.getBookId().equals(book.getId())) {
                                requestBooks.add(book);
                                Log.d("j!!!Book", jBook);
                            }
                        }
                    } else {
                        Log.d("FBerrorFragmentRequest", "User doesn't exist or string is empty");
                    }
                }

                assert (requestBooks.size() == requests.size());

                Log.d("j!!!Book", Integer.toString(requestBooks.size()));
                Log.d("j!!!Book2", Integer.toString(requests.size()));

                requestAdapter.updateList(requests, requestBooks);
                requestAdapter.notifyDataSetChanged();

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

            initRecyclerView(this.getContext());



        }else if (filter.equals(("Accepted Requests"))){

            initRecyclerView(this.getContext());
             // basically do nothing and go back to main page



        }
    }
    // Recycler view initialization
    //This resets the recycler view to a new ArrayList everytime
    private void initRecyclerView(Context context){
        Log.d("j!!!init", "initRecyclerView: init recyclerview.");

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



}
