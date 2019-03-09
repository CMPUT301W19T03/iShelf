package ca.ualberta.ishelf;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;

public class ListOfRequestsActivity extends AppCompatActivity {
    // For debugging
    private static final String TAG = "ListOfRequestsActivity";
    // List of strings that are displayed in requests
    private ArrayList<String> mNames = new ArrayList<>();
    // Username for current user
    String username;
    // Array of requests for our users books
    ArrayList<Request> userRequests = new ArrayList<Request>();
    // RecyclerView view and adapter
    RecyclerView recyclerView;
    LORRecyclerViewAdapter adapter;
    // firebase reference
    private Firebase ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_requests2);
        Log.d(TAG, "onCreate");
        //TODO when book request is accepted, add notification for other user
        //TODO get proper username
        final String username = "testUsername";
        // String username = getSharedPreferences("UserPreferences", Context.MODE_PRIVATE).getString("username", "TestUsername");
        // get the reference of RecyclerView
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.listOfRequestsRecycler);
        // set a LinearLayoutManager with default orientation
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager); // set LayoutManager to RecyclerView
        // Add entries to my mNames array
        mNames.add("Paul");
        mNames.add("James");
        mNames.add("Danny");
        mNames.add("Jessica");
        // THESE NEED TO BE USER AND BOOK OBJECTS
        //Request r1 = new Request("testUsername", "james", "moby dick", 4 );
        //Request r2 = new Request("testUsername", "jessica", "I, Robot", 3 );
        //Request r3 = new Request("testUsername", "shun", "Romeo and Juliet", 5 );
        //userRequests.add(r1);
        //mNames.add(r1.toString());
        //userRequests.add(r2);
        //mNames.add(r2.toString());
        //userRequests.add(r3);
        //mNames.add(r3.toString());

        initRecyclerView();

        // getRequests();
        // Add request borrowers to mNames array
    }

    private void initRecyclerView(){
        Log.d(TAG, "initRecyclerView: init recyclerview.");
        RecyclerView recyclerView = findViewById(R.id.listOfRequestsRecycler);
        LORRecyclerViewAdapter adapter = new LORRecyclerViewAdapter(this, mNames);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }
    private void getUsers() {
        /*

        public User getUser(String username) {
            // get reference to specific entry
            Firebase tempRef = ref.child("Users").child(username);
            final ArrayList<User> userList = new ArrayList<User>();
            Log.d("SizeBefore ", String.valueOf(userList.size()));
            // create a one time use listener to immediately access datasnapshot
            tempRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String jUser = dataSnapshot.getValue(String.class);
                    Log.d("jUser", jUser);
                    if (jUser != null) {
                        // Get user object from Gson
                        Gson gson = new Gson();
                        Type tokenType = new TypeToken<User>(){}.getType();
                        User user = gson.fromJson(jUser, tokenType);
                        Log.d("Confirm", user.getUsername());
                        userList.add(user);
                    } else {
                        Log.d("FBerror1", "User doesn't exist or string is empty");
                    }
                    Log.d("Size", String.valueOf(userList.size()));
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {
                    return;
                }
            });
            // If User doesn't exist, or string is empty, return null
            Log.d("next", "this is where code jumps to next");
            Log.d("SizeAfter", String.valueOf(userList.size()));
            try {
                //Log.d("SizeLoop", String.valueOf(userList.size()));
                return userList.get(0);
            } catch (Exception e) {
                Log.d("FBerror", "No user exists in firebase with that username");
            }
            return null;
        }

        */
    }

    private void getRequests() {
        // Get reference to Requests child
        final Firebase tempRef = ref.child("Requests");
        // Remove all entries from mNames so we can update them
        mNames.clear();
        // create a one time use listener to immediately access datasnapshot
        tempRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Type tokenType = new TypeToken<Book>(){}.getType();
                Request tempRequest;
                String tempString;

                for(DataSnapshot d: dataSnapshot.getChildren()) {
                    tempString = d.getValue(String.class);
                    if (tempString != null) {
                        // Get user object from Gson
                        Gson gson = new Gson();
                        tempRequest = gson.fromJson(tempString, tokenType);
                        //TODO get user and their rating
                        if (tempRequest.getRequester().equals(username)) {
                            // Add to array of requests
                            userRequests.add(tempRequest);
                            // Add to array of dispalyed strings
                            mNames.add(tempRequest.toString());
                        }
                    } else {
                        Log.d("Firebase Retrieval Error", "Notification doesn't exist or string is empty");
                    }
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        adapter.notifyDataSetChanged();

    }

}
