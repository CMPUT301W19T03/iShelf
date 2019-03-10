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
    // List of book titles that are displayed in requests
    private ArrayList<String> mBookNames = new ArrayList<>();
    // List of integers of user ratings
    private ArrayList<Float> mRatings = new ArrayList<>();
    // Username for current user
    String username;
    // Array of requests for our users books
    //ArrayList<Request> userRequests = new ArrayList<Request>();
    // RecyclerView view and adapter
    RecyclerView recyclerView;
    LORRecyclerViewAdapter adapter;
    // firebase reference
    private final String link = "https://ishelf-bb4e7.firebaseio.com";
    private Firebase ref;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_requests2);
        Log.d(TAG, "onCreate");
        // Initialize firebase for use
        Firebase.setAndroidContext(this);
        ref = new Firebase(link);


        //TODO when book request is accepted, add notification for other user
        //TODO get ratings working
        //TODO get buttons working
        //TODO get requests from firebase
        //TODO update UML for requests
        //TODO add intent testing

        // Add testUsername, since we should be signed in from here
        //TODO remove forced sharedPreference editing
        username = "testUsername";
        SharedPreferences.Editor editor = getSharedPreferences("UserPreferences", Context.MODE_PRIVATE).edit();
        editor.putString("username", "testUsername").apply();

        final String username = getSharedPreferences("UserPreferences", Context.MODE_PRIVATE).getString("username", "TestUsername");
        Log.d(TAG+" getUser ", "User is " + username);

        // get the reference of RecyclerView
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.listOfRequestsRecycler);
        // set a LinearLayoutManager with default orientation
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager); // set LayoutManager to RecyclerView

        /*
        // Add simple test entries for the display
        // Add entries to my mNames array
        mNames.add("Paul");
        mNames.add("James");
        mNames.add("Danny");
        mNames.add("Jessica");
        mBookNames.add("Moby Dick");
        mBookNames.add("I, Robot");
        mBookNames.add("In may we soar");
        mBookNames.add("Hello, would you like to spend the day together and maybe fall in love");
        mRatings.add(2f);
        mRatings.add(3f);
        mRatings.add(5f);
        mRatings.add(4f);
        */
        initRecyclerView();
        getUser();

        /*
        Request r1 = new Request(UUID.randomUUID(), "james");
        Request r2 = new Request(UUID.randomUUID(), "jessica");
        Request r3 = new Request(UUID.randomUUID(), "shun");
        userRequests.add(r1);
        userRequests.add(r2);
        userRequests.add(r3);
        */

        /*
        // Add test data to database for use in this activity
        User u1 = new User();
        u1.setUsername("testUsername");
        User t1 = new User();
        t1.setUsername("testReq1");
        t1.setRating(new Rating(4f,""));
        User t2 = new User();
        t2.setUsername("testReqr2");
        t2.setRating(new Rating(3f, ""));
        Book b1 = new Book();
        b1.setName("bName1");
        Book b2 = new Book();
        b2.setName("bName2");
        u1.addRequest(new Request(b1.getId(), "testReq1"));
        u1.addRequest(new Request(b2.getId(), "testReq2"));
        Database db = new Database(this);
        db.addUser(u1);
        db.addUser(t1);
        db.addUser(t2);
        db.addBook(b1);
        db.addBook(b2);
        */
        // Expected output
        // testReq1, 3 stars, book bName1
        // testReq2, 4 stars, book bName1

    }

    /**
     * Initialize the recycler view
     * This is when all the entires are set to their appropriate values
     */
    private void initRecyclerView(){
        Log.d(TAG, "initRecyclerView: init recyclerview.");
        RecyclerView recyclerView = findViewById(R.id.listOfRequestsRecycler);
        LORRecyclerViewAdapter adapter = new LORRecyclerViewAdapter(this, mNames, mRatings, mBookNames);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    /**
     * Accept a requesters request
     * This method is called from LORRecyclerViewAdapter when the accept button is pressed
     * This function updates the display, updates the users requests,
     * and creates the appropriate notification
     * @param position
     */
    void acceptRequest(int position){
        Log.d(TAG+" acceptRequest", "Called with " + position);
        // If the adapter has been initialized, run the appropriate code
        // Create Database object that we will use
        Database db = new Database(this);
        // Delete rating and name entries and update display
        mRatings.remove(position);
        mNames.remove(position);
        //safeNotify();
        // Create the appropriate notification and add to firebase
        Notification notification = new Notification(new Date(),
                user.getUsername() + " has accepted your request", "username");
        db.addNotification(notification);
        // Update user's Requests array, and update in database
        //TODO is this what want to do with a accepted request?
        user.getListofRequests().get(position).accept();
        db.addUser(user);
    }

    /**
     * Decline a requesters request
     * This method is called from LORRecyclerViewAdapter when the accept button is pressed
     * This function updates the display, updates the users requests,
     * and creates the appropriate notification
     * @param position
     */
    void declineRequest(int position){
        Log.d(TAG+" declineRequest", "Called with " + position);
        // If the adapter has been initialized, run the appropriate code
        // Create Database object that we will use
        Database db = new Database(this);
        // Delete rating and name entries and update display
        mRatings.remove(position);
        mNames.remove(position);
        //safeNotify();
        // Create the appropriate notification and add to firebase
        Notification notification = new Notification(new Date(),
                user.getUsername() + " has declined your request", "username");
        db.addNotification(notification);
        //TODO do we want to delete the request when it is declined?
        // Update user's Requests array, and update in database
        user.getListofRequests().remove(position);
        db.addUser(user);
    }

    /**
     * Get the User object representing the current user
     * From there, make calls to getReqeusterRating and getBookNames
     * We have to make these calls within onDataChange to avoid threading errors
     * Since they depend on us having a User object that isn't null
     */
    private void getUser() {
        // get reference to specific entry
        Firebase tempRef = ref.child("Users").child(username);
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
                    user = gson.fromJson(jUser, tokenType);
                    Log.d("Confirm", user.getUsername());
                    for(Request request: user.getListofRequests()) {
                        mNames.add(request.getRequester());
                    }
                    // This call must be nested since we need
                    // the user object to access the requests data
                    //getRequesterRatings();
                    //getBookNames();
                    safeNotify();
                    getRequestInformation();
                } else {
                    Log.d("getUser FBerror1", "jUser was null");
                }
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {
                return;
            }
        });
    }

    private void getRequestInformation(){
        for (Request request: user.getListofRequests()) {
            // Firebase reference to get Users information (requesters)
            Firebase tempRefU = ref.child("Users").child(request.getRequester());
            // Firebase reference to get Books information (names)
            Firebase tempRefB = ref.child("Books").child(request.getBookId().toString());
            tempRefU.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String jUser = dataSnapshot.getValue(String.class);
                    if (jUser != null) {
                        Log.d("jUser", jUser);
                        // Get user object from Gson
                        Gson gson = new Gson();
                        Type tokenType = new TypeToken<User>() { }.getType();
                        User reqUser = gson.fromJson(jUser, tokenType);
                        Log.d("Confirm", user.getUsername());
                        mRatings.add(reqUser.getOverallRating());
                        safeNotify();
                    } else {
                        Log.d("getRequestInformation User FBerror1", "jUser was null");
                    }
                }
                @Override
                public void onCancelled(FirebaseError firebaseError) {
                    return;
                }
            });
            tempRefB.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String jBook = dataSnapshot.getValue(String.class);
                    Log.d("jBook", jBook);
                    if (jBook != null) {
                        // Get user object from Gson
                        Gson gson = new Gson();
                        Type tokenType = new TypeToken<Book>() { }.getType();
                        Book book = gson.fromJson(jBook, tokenType);
                        Log.d("Confirm", user.getUsername());
                        mBookNames.add(book.getName());
                        safeNotify();
                    } else {
                        Log.d("getRequestInformation Book FBerror1", "jBook was null");
                    }
                }
                @Override
                public void onCancelled(FirebaseError firebaseError) {
                    return;
                }
            });
        }

    }

    private void getRequesterRatings(){
        ArrayList<Request> requests = user.getListofRequests();
        Log.d("size", "Size is " + requests.size());
        for (int i = 0; i < requests.size(); i++) {
            // get reference to specific entry
            Firebase tempRef = ref.child("Users").child(requests.get(i).getRequester());
            // create a one time use listener to immediately access datasnapshot
            final int finalI = i;
            tempRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String jUser = dataSnapshot.getValue(String.class);
                    Log.d("jUser", jUser);
                    if (jUser != null) {
                        // Get user object from Gson
                        Gson gson = new Gson();
                        Type tokenType = new TypeToken<User>() { }.getType();
                        User reqUser = gson.fromJson(jUser, tokenType);
                        Log.d("Confirm", user.getUsername());
                        mRatings.set(finalI, reqUser.getOverallRating());
                        //adapter.notifyDataSetChanged();
                    } else {
                        Log.d("FBerror1", "User doesn't exist or string is empty");
                    }
                }
                @Override
                public void onCancelled(FirebaseError firebaseError) {
                    return;
                }
            });
        }
    }

    private void getBookNames(){
        ArrayList<Request> requests = user.getListofRequests();
        for (int i = 0; i < requests.size(); i++) {
            // get reference to specific entry
            Firebase tempRef = ref.child("Books").child(requests.get(i).getBookId().toString());
            // create a one time use listener to immediately access datasnapshot
            final int finalI = i;
            tempRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String jBook = dataSnapshot.getValue(String.class);
                    Log.d("jBook", jBook);
                    if (jBook != null) {
                        // Get user object from Gson
                        Gson gson = new Gson();
                        Type tokenType = new TypeToken<Book>() { }.getType();
                        Book book = gson.fromJson(jBook, tokenType);
                        Log.d("Confirm", user.getUsername());
                        mBookNames.set(finalI, book.getName());
                        //adapter.notifyDataSetChanged();
                    } else {
                        Log.d("FBerror1", "User doesn't exist or string is empty");
                    }
                }
                @Override
                public void onCancelled(FirebaseError firebaseError) {
                    return;
                }
            });
        }
    }

    private void safeNotify() {
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        } else {
            Log.d(TAG+" safeNotify", "adapter is null");
        }
    }

}
