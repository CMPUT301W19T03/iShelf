package ca.ualberta.ishelf.RecyclerAdapters;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import java.util.Collections;

import ca.ualberta.ishelf.Book;
import ca.ualberta.ishelf.Database;
import ca.ualberta.ishelf.R;
import ca.ualberta.ishelf.Request;

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
    private final String TAG = "RequestFragment";
    private ArrayList<Request> requestsUserMadeList = new ArrayList<>();
    private ArrayList<Book> requestsUserMadeBooksList = new ArrayList<>();
    private ArrayList<Request> requestUserReceivedList = new ArrayList<>();
    private ArrayList<Book> requestUserReceivedBooksList = new ArrayList<>();
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
                Request_Filter(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        requestRecyclerView = getActivity().findViewById((R.id.request_recycler));
        requestLayoutManager = new LinearLayoutManager(this.getContext());
        requestRecyclerView.setLayoutManager(requestLayoutManager);
        requestAdapter = new RequestAdapter(this.getContext());
        requestRecyclerView.setAdapter(requestAdapter);

        // creates a line between each individual record in the list
        // https://developer.android.com/reference/android/support/v7/widget/DividerItemDecoration
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(requestRecyclerView.getContext(),
                LinearLayoutManager.VERTICAL);
        requestRecyclerView.addItemDecoration(dividerItemDecoration);

        // Get requests from firebase
        getRequests();


    }

    /**
     * get list requests that the logged in user made and received
     * and update the RecyclerView
     * @author rmnattas
     */
    public void getRequests(){

        // get logged in username
        final String username = getActivity().getSharedPreferences("UserPreferences", Context.MODE_PRIVATE).getString("username", null);

        //connect to firebase
        Database db = new Database(getContext());
        Firebase fb = db.connect(getContext());
        Firebase childRef = fb.child("Requests");

        childRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                clearLists();   // clear old list of requests and books
                for(DataSnapshot d: dataSnapshot.getChildren()) {
                    String jRequest = d.getValue(String.class);
                    if (jRequest != null) {
                        // Get Requests object from Gson
                        Gson gson = new Gson();
                        Type tokenType = new TypeToken<Request>() {
                        }.getType();
                        Request request = gson.fromJson(jRequest, tokenType);
                        if (request.getRequester().equals(username)) {
                            requestsUserMadeList.add(request);
                        } else if (request.getOwner().equals(username)) {
                            requestUserReceivedList.add(request);
                        }
                    } else {
                        Log.d(TAG, "ERROR #123121");
                    }
                }

                Collections.sort(requestsUserMadeList);
                Collections.reverse(requestsUserMadeList);
                Collections.sort(requestUserReceivedList);
                Collections.reverse(requestUserReceivedList);
                getBooks();

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                return;
            }

        });
    }

    private void getBooks(){

        //connect to firebase
        Database db = new Database(getContext());
        Firebase fb = db.connect(getContext());
        Firebase childRef = fb.child("Books");

        childRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot d: dataSnapshot.getChildren()) {
                    String jBook = d.getValue(String.class);
                    // Get book object from Gson
                    Gson gson = new Gson();
                    Type tokenType = new TypeToken<Book>() {}.getType();
                    Book book = gson.fromJson(jBook, tokenType); // here is where we get the user object
                    for (Request r : requestUserReceivedList){
                        if (r.getBookId().equals(book.getId())){
                            requestUserReceivedBooksList.add(book);
                            break;
                        }
                    }
                    for (Request r : requestsUserMadeList){
                        if (r.getBookId().equals(book.getId())){
                            requestsUserMadeBooksList.add(book);
                            break;
                        }
                    }
                }

                assert (requestUserReceivedList.size() == requestUserReceivedBooksList.size());
                assert (requestsUserMadeList.size() == requestsUserMadeBooksList.size());

                Request_Filter(0);

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                return;
            }

        });
    }

    /**
     * Clear all requests and books lists
     * @author rmnattas
     */
    private void clearLists(){
        requestsUserMadeList.clear();
        requestsUserMadeBooksList.clear();
        requestUserReceivedList.clear();
        requestUserReceivedBooksList.clear();
    }

    public void Request_Filter(int filter) { //this is going to filter strings for now, but should work whenever we pass in book/user

        if (filter == 0){
            ArrayList<Request> allRequests = new ArrayList<>();
            allRequests.addAll(requestUserReceivedList);
            allRequests.addAll(requestsUserMadeList);
            ArrayList<Book> allBooks = new ArrayList<>();
            allBooks.addAll(requestUserReceivedBooksList);
            allBooks.addAll(requestsUserMadeBooksList);
            requestAdapter.updateList(allRequests, allBooks);
        } else if (filter==1){
            requestAdapter.updateList(requestUserReceivedList, requestUserReceivedBooksList);
        }else if (filter==2){
            ArrayList<Request> acceptedRequests = new ArrayList<>();
            ArrayList<Book> acceptedBooks = new ArrayList<>();
            for (int i=0; i < (requestUserReceivedList.size()); i++){
                if(requestUserReceivedList.get(i).getStatus() == 1){
                    acceptedRequests.add(requestUserReceivedList.get(i));
                    acceptedBooks.add(requestUserReceivedBooksList.get(i));
                }
            }
            requestAdapter.updateList(acceptedRequests, acceptedBooks);
        }else if(filter==3) {
            requestAdapter.updateList(requestsUserMadeList, requestsUserMadeBooksList);
        } else if (filter==4){
            ArrayList<Request> acceptedRequests = new ArrayList<>();
            ArrayList<Book> acceptedBooks = new ArrayList<>();
            for (int i=0; i < (requestsUserMadeList.size()); i++){
                if(requestsUserMadeList.get(i).getStatus() == 1){
                    acceptedRequests.add(requestsUserMadeList.get(i));
                    acceptedBooks.add(requestsUserMadeBooksList.get(i));
                }
            }
            requestAdapter.updateList(acceptedRequests, acceptedBooks);
        }

    }


//    private void enteredAlert(String msg) {
//        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this.getContext());
//        alertDialogBuilder.setMessage((CharSequence) msg);
//        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface arg0, int arg1) {
//                // TODO Auto-generated catch block
//            }
//        });
//        AlertDialog alertDialog = alertDialogBuilder.create();
//        alertDialog.show();
//    }



}
