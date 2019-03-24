/**
 * ListOfRequestsActivity
 * Version 1
 * March 10, 2019
 * @author : Randal Kimpinski
 */
package ca.ualberta.ishelf;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

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

/**
 * This class deals with accepting requests that other users have made for one of our books
 * The bookId for the appropriate book Object is passed in through the intent,
 * and the current user's username is passed in through shared preferences
 * The rest of the information necessary for displaying the requests is accessed via
 * additional queries to firebase
 * @author : Randal
 */
public class ListOfRequestsActivity extends AppCompatActivity {
    // tag For debugging
    private static final String TAG = "ListOfRequestsActivity";
    // List of strings that are displayed in requests
    private ArrayList<String> mNames = new ArrayList<>();
    // List of book titles that are displayed in requests
    private ArrayList<String> mBookNames = new ArrayList<>();
    // List of integers of user ratings
    private ArrayList<Float> mRatings = new ArrayList<>();
    // List of request statuses, used to determine visibility of boxes
    private ArrayList<Integer> mStatus = new ArrayList<>();
    // Username for current user
    String username;
    // RecyclerView view and adapter
    public RecyclerView recyclerView;
    // RecylerView adapter
    LORRecyclerViewAdapter adapter;
    // Holds the current app user's username
    User user;
    // Holds the BookId of the book being viewed in this activity
    UUID bookId;
    // List of request objects
    ArrayList<Request> requests = new ArrayList<>();
    // Firebase variables
    private final String link = "https://ishelf-bb4e7.firebaseio.com";
    private Firebase ref;

    /**
     * The onCreate method initializes Firebase, gets the User object associated with the
     * current User's username. It also queries Firebase to take the requests under User
     * and get the additional information we need (bookNames, Requester rating)
     * @param savedInstanceState savedInstanceState needed for onCreate
     * @author : Randal Kimpinski
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_requests2);
        Log.d(TAG, "onCreate");
        // Initialize firebase for use
        Firebase.setAndroidContext(this);
        ref = new Firebase(link);
        // Get the Book Id
        Intent intent = getIntent();
        String bookID  = intent.getStringExtra("ID");
        bookId = UUID.fromString(bookID);

        // Get the current user's username from shared preferences
        username = getSharedPreferences("UserPreferences", Context.MODE_PRIVATE).getString("username", "TestUsername");
        Log.d(TAG+" getUser ", "User is " + username);

        // get the reference to RecyclerView
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.listOfRequestsRecycler);
        // set a LinearLayoutManager with default orientation
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager); // set LayoutManager to RecyclerView

        // Initialize the recyclerView
        initRecyclerView();

        // Run the code that gets our user object
        //TODO do I need to get User Object?
        getUser();
        // Add test data to test getRequestInformation
        //addTestData();
        getRequests();
        getRequestInformation();
    }

    /**
     * Initialize the recycler view
     * This is when all the entries are set to their appropriate values
     * This method is called again later to simplify updating the data
     * @author : Randal Kimpinski
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
     * @param position the position in the recycler view of the clicked button
     * @author : Randal Kimpinski
     */
    void acceptRequest(int position){
        Log.d(TAG+" acceptRequest", "Called with " + position);
        // Create Database object that we will use
        Database db = new Database(this);

        Intent intent = getIntent();
        String bookID  = intent.getStringExtra("ID");


        //TODO replace with actual code
//        String stringBookId = "02f36eb7-12c4-40f1-89dc-68f0ab21a900";


        bookId = UUID.fromString(bookID);

        Book book = new Book();
        book = intent.getParcelableExtra("book");

        book.setTransition(1);
        book.setNext_holder(mNames.get(position));



        db.editBook(book);


        // The specific request object
        Request selectedRequest = requests.get(position);
        // Decline all other requests
        /*
        for (int i = 0; i < requests.size(); i++) {
            if (i != position) {
                Request tempRequest = requests.get(i);
                tempRequest.decline();
                db.addRequest(tempRequest);
                Notification notification = new Notification(new Date(),
                        username + " has declined your request for "+ mBookNames.get(i), tempRequest.getOwner());
                db.addNotification(notification);
            }
        }
        */
        // Accept Request
        selectedRequest.accept();
        db.addRequest(selectedRequest);
        // Create the appropriate notification and add to firebase
        Notification notification = new Notification(new Date(),
                username + " has accepted your request", selectedRequest.getRequester());
        db.addNotification(notification);

        // Remove appropriate array items
        //TODO make this CLEAR then re-add our request (so save all the values)
        //TODO or iterate over it backwards, and remove all except position
        //mRatings.remove(position);
        //mNames.remove(position);
        //mBookNames.remove(position);

        // Call locationActivity and pass request UUID as an extra string
        /*
        Intent myIntent = new Intent(this, LocationActivity.class);
        String requestId = selectedReqeust.getId().toString();
        myIntent.puExtra("RequestId", requestId);
        startActivity(myIntent);
         */

        // Update display
        safeNotify();


        // send the "selectedRequest" Request to the MapsActivity passed as an extra called "Request"
        Intent mapIntent = new Intent(this, MapsActivity.class);
        mapIntent.putExtra("Request", selectedRequest);
        startActivity(mapIntent);
    }




            /**
     * Decline a requesters request
     * This method is called from LORRecyclerViewAdapter when the accept button is pressed
     * This function updates the display, updates the users requests,
     * and creates the appropriate notification
     * @param position the position in the recycler view of the clicked button
     * @author : Randal Kimpinski
     */
    void declineRequest(int position) {
        Log.d(TAG+" declineRequest", "Called with " + position);
        // Create Database object that we will use
        Database db = new Database(this);
        // The specific request object
        Request selectedRequest = requests.get(position);
        // Decline this request
        selectedRequest.decline();
        // Create the appropriate notification and add to firebase
        Notification notification = new Notification(new Date(),
                username + " has declined your request for "+ mBookNames.get(position),
                username);
        db.addNotification(notification);
        // Delete the request in firebase
        db.deleteRequest(selectedRequest.getId().toString());
        // Remove appropriate array items
        mRatings.remove(position);
        mNames.remove(position);
        mBookNames.remove(position);
        requests.remove(position);

        // Update display
        safeNotify();
    }

    /**
     * Get the User object representing the current user
     * From there, make calls to getReqeusterRating and getBookNames * We have to make these calls within onDataChange to avoid threading errors
     * Since they depend on us having a User object that isn't null
     * @author : Randal Kimpinski
     */
    private void getUser() {
        Log.d(TAG+" getUser", "get user has been called");
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
                    safeNotify();
                    getRequests();
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

    /**
     * Get all the requests for a specific book and specific user
     * In this case, the book is provided by the previous activity,
     * and the user is the user currently using the app
     * @author : Randal Kimpinski
     */
    private void getRequests() {
        Log.d(TAG + " getRequests", "getRequests has been called");
        // get reference to specific entry
        // create a one time use listener to immediately access datasnapshot
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Request tempRequest;
                for (DataSnapshot d : dataSnapshot.child("Requests").getChildren()) {
                    String requestString = d.getValue(String.class);
                    if (requestString != null) {
                        // Get the request object from Gson
                        Gson gson = new Gson();
                        Type tokenType = new TypeToken<Request>() { }.getType();
                        tempRequest = gson.fromJson(requestString, tokenType);
                        if (tempRequest.getOwner().equals(username) && tempRequest.getBookId().equals(bookId)) {
                            // Add request to requests array
                            requests.add(tempRequest);
                        }
                        initRecyclerView();
                    }
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    /**
     * Using the arrays of proper requests we got, add the additional information
     * necessary to display them. This includes the requester rating and book name
     * Issues will arise if the bookId or Requester username don't reference valid
     * entries in the firebase database
     * @author : Randal Kimpinski
     */
    private void getRequestInformation() {
        Log.d(TAG + " getRequests", "getRequests has been called");
        // create a one time use listener to immediately access datasnapshot
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Type tokenType;
                Gson gson;
                for (Request tempRequest: requests) {
                    // Add requester to array
                    mNames.add(tempRequest.getRequester());
                    // Get the requester rating
                    String jUser = dataSnapshot.child("Users").
                            child(tempRequest.getRequester()).getValue(String.class);
                    if (jUser != null) {
                        Log.d("jUser", jUser);
                        // Get user object from Gson
                        gson = new Gson();
                        tokenType = new TypeToken<User>() {
                        }.getType();
                        User reqUser = gson.fromJson(jUser, tokenType);
                        // Add requester rating to array
                        mRatings.add(reqUser.getOverallRating());
                    } else {
                        Log.d("getRequestInformation User FBerror1", "jUser was null");
                    }
                    // Get the book rating
                    String jBook = dataSnapshot.child("Books").
                            child(tempRequest.getBookId().toString()).getValue(String.class);
                    if (jBook != null) {
                        Log.d("jBook", jBook);
                        // Get user object from Gson
                        gson = new Gson();
                        tokenType = new TypeToken<Book>() {
                        }.getType();
                        Book book = gson.fromJson(jBook, tokenType);
                        mBookNames.add(book.getName());
                    } else {
                        Log.d("getRequestInformation Book FBerror1", "jBook was null");
                    }
                }
                safeNotify();
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });
    }

    /**
     * Update our RecyclerView when the data has changed
     * Currently implemented by reinitializing the recyclerView
     * But There may be a more optimal method
     * @author : Randal Kimpinski
     */
    private void safeNotify() {
        initRecyclerView();
    }

    /**
     * version of safeNotify that is called from the XML onClick() method connector
     * Calls recycler view to easily update the data
     * Also prints extensive error messages detailing the current state of all the
     * relevant information. This is because there were many errors getting to this point
     * @param view the view needed for onCLick method in xml
     * @author : Randal Kimpinski
     */
    public void safeNotify(View view) {
        initRecyclerView();
        // Print out a bunch of error messages
        for (String x: mBookNames) {
            Log.d("current data", x);
        }
        for (String x: mNames) {
            Log.d("current data", x);
        }
        for (float x: mRatings) {
            Log.d("current data", Float.toString(x));
        }
    }

    /**
     * Add test Request objects to firebase
     * Request attributes must be valid keys for other firebase entries
     * Primary purpose is to test
     * Useful for just testing getRequestInformation
     * @author Randal Kimpinski
     */
    public void addTestData() {
        // Create test Request objects that refer to real Firebase items
        UUID id;
        id = UUID.fromString("e1afab89-77e9-49d6-afdd-ab98e4e245d4");
        Request r1 = new Request(id, "Evan", username);
        Request r2 = new Request(id, "aalattas", username);
        Request r3 = new Request(id, "jsgray1", username);
        // Add requests to Firebase
        Database db = new Database(this);
        db.addRequest(r1);
        db.addRequest(r2);
        db.addRequest(r3);
    }
}
