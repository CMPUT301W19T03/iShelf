package ca.ualberta.ishelf;

import android.content.Intent;
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

import ca.ualberta.ishelf.RecyclerAdapters.RatingsAdapter;

public class ViewRatingsActivity extends AppCompatActivity {
    // Tag for debugging
    private String TAG = "ViewRatingsActivity";
    // List of strings that are displayed in requests
    private ArrayList<Rating> mRatings = new ArrayList<>();
    // RecyclerView view and adapter
    public RecyclerView recyclerView;
    // RecylerView adapter
    RatingsAdapter adapter;
    // Holds the User of the user being viewed in this activity
    String UserID;
    String bookID;
    User user;
    Book book;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_ratings);
        Log.d(TAG, "onCreate");

        Intent intent = this.getIntent();
        if (intent.hasExtra("UserID")){
            UserID = intent.getStringExtra("UserID");
            Log.d(TAG, "onCreate: BookID: " + UserID);
            getUser(UserID);
        }
        if (intent.hasExtra("BookID")){
            bookID = intent.getStringExtra("BookID");
            Log.d(TAG, "onCreate: BookID: " + bookID);
            getBook(bookID);
        }

        // get the reference to RecyclerView
        recyclerView = (RecyclerView) findViewById(R.id.ratingsRecyclerView);
        // set a LinearLayoutManager with default orientation
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager); // set LayoutManager to RecyclerView

        // Initialize the recyclerView
        initRecyclerView();
    }

    /**
     * get a book based on bookID from firebase
     * @author rmnattas
     */
    public void getBook(String bookId){

        //connect to firebase
        Database db = new Database(this);
        Firebase fb = db.connect(this);
        Firebase childRef = fb.child("Books").child(bookId);


        childRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String jBook = dataSnapshot.getValue(String.class);
                //Log.d("jBook", jBook);
                if (jBook != null) {
                    mRatings.clear();
                    // Get book object from Gson
                    Gson gson = new Gson();
                    Type tokenType = new TypeToken<Book>() {
                    }.getType();
                    book = gson.fromJson(jBook, tokenType); // here is where we get the user object
                    mRatings.addAll(book.getRatings());
                    initRecyclerView();
                } else {

                }
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {
                return;
            }

        });
    }

    public void getUser(String username){
        //connect to firebase
        Database db = new Database(this);
        Firebase fb = db.connect(this);

        // get reference to specific entry
        Firebase tempRef = fb.child("Users").child(username);
        // create a one time use listener to immediately access datasnapshot
        tempRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String jUser = dataSnapshot.getValue(String.class);
                Log.d("jUser", jUser);
                if (jUser != null) {
                    mRatings.clear();
                    // Get user object from Gson
                    Gson gson = new Gson();
                    Type tokenType = new TypeToken<User>() {
                    }.getType();
                    user = gson.fromJson(jUser, tokenType); // here is where we get the user object
                    mRatings.addAll(user.getRatingArrayList());
                    initRecyclerView();
                } else {


                }

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                return;
            }
        });
    }

    /**
     * Initialize the recycler view
     * This is when all the entries are set to their appropriate values
     * This method is called again later to simplify updating the data
     * @author : Randal Kimpinski
     */
    public void initRecyclerView(){
        Log.d(TAG, "initRecyclerView:");
        adapter = new RatingsAdapter(this, mRatings);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
