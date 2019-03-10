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
import java.util.UUID;

public class ListOfRequestsActivity extends AppCompatActivity {
    // For debugging
    private static final String TAG = "ListOfRequestsActivity";
    // List of strings that are displayed in requests
    private ArrayList<String> mNames = new ArrayList<>();
    // List of integers of user ratings
    private ArrayList<Integer> mRatings = new ArrayList<>();
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
        //TODO get ratings working
        //TODO get buttons working
        //TODO get requests from firebase
        //TODO update UML for requests
        //TODO add intent testing

        // Add testUsername, since we should be signed in from here
        //TODO remove forced sharedPreference editing
        SharedPreferences.Editor editor = getSharedPreferences("UserPreferences", Context.MODE_PRIVATE).edit();
        editor.putString("username", "testUsername").apply();
        final String username = getSharedPreferences("UserPreferences", Context.MODE_PRIVATE).getString("username", "TestUsername");
        Log.d(TAG+" getUser ", "User is " + username);

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
        mRatings.add(2);
        mRatings.add(3);
        mRatings.add(5);
        mRatings.add(4);
        Request r1 = new Request(UUID.randomUUID(), "james");
        Request r2 = new Request(UUID.randomUUID(), "jessica");
        Request r3 = new Request(UUID.randomUUID(), "shun");
        userRequests.add(r1);
        userRequests.add(r2);
        userRequests.add(r3);

        initRecyclerView();
    }

    private void initRecyclerView(){
        Log.d(TAG, "initRecyclerView: init recyclerview.");
        RecyclerView recyclerView = findViewById(R.id.listOfRequestsRecycler);
        LORRecyclerViewAdapter adapter = new LORRecyclerViewAdapter(this, mNames, mRatings);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }
    void acceptRequest(int position){
        Log.d(TAG+" acceptRequest", "Called with " + position);
    }
    void declineRequest(int position){
        Log.d(TAG+" declineRequest", "Called with " + position);
    }
    // Get requests from user object (so also get user object)

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


}
