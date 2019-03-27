package ca.ualberta.ishelf;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.UUID;


/**
 * Must pass in "User" extra
 * Can also pass in "Book" extra
 * Both extras are strings of the names
 * The objects are retrieved from Firebase
 */
public class RatingActivity extends AppCompatActivity {
    private TextView tvUsername;
    private TextView tvBookname;
    private Button saveButton;
    private RatingBar rbUser;
    private RatingBar rbBook;
    private String username;
    private EditText etUserComment;
    private EditText etBookComment;
    private User user;
    private Book book;
    private String bookname;
    private String TAG = "RatingActivity";
    private String bookID;
    private String currentUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);

        // initialize the UI elements
        tvUsername = findViewById(R.id.tvUserName);
        rbUser = findViewById(R.id.rbUser);
        etUserComment = findViewById(R.id.etUserComment);

        tvBookname = findViewById(R.id.tvBookName);
        rbBook = findViewById(R.id.rbBook);
        etBookComment = findViewById(R.id.etBookComment);

        saveButton = findViewById(R.id.saveButton);

        // retrieve the user to be rated name
        Intent intent = this.getIntent();
        username = intent.getStringExtra("User");
        Log.d(TAG, "onCreate: Username: " + username);
        // TODO: retrieve User from Firebase using username
        getUser(username);

//        if (intent.hasExtra("Book")){
//            bookname = intent.getStringExtra("Book");
//            Log.d(TAG, "onCreate: Bookname: " + bookname);
//            // TODO: retrieve Book from Firebase using bookname
//        }

        if (intent.hasExtra("BookID")){
            bookID = intent.getStringExtra("BookID");
            Log.d(TAG, "onCreate: BookID: " + bookID);
            getBook(bookID);
        }

        // update the UI with the username that's passed in
        tvUsername.setText("Please review " + username);

        // check if book exists, if it doesn't we need to hide it, else we need to set it
        if (bookname != null) {
            // book exists, so update the relevant UI elements
            tvBookname.setText("Please review the book: " + bookname);
        } else {
            // book does not exist, so remove all relevant UI elements
            tvBookname.setVisibility(View.GONE);
            rbBook.setVisibility(View.GONE);
            etBookComment.setVisibility(View.GONE);
        }

        currentUsername = getSharedPreferences("UserPreferences", Context.MODE_PRIVATE).getString("username", null);


    }

    public void saveButton(View v){
        Rating userRating = new Rating(rbUser.getRating(), etUserComment.getText().toString());
        userRating.setReviewer(currentUsername);
        user.addRating(userRating);
        //userRating.setDate(date.getTime());
        // TODO: update user with firebase
        Database db = new Database(this);
        db.editUser(user);

        if (book != null){
            Rating bookRating = new Rating(rbBook.getRating(), etBookComment.getText().toString());
            bookRating.setReviewer(currentUsername);
            book.addRating(bookRating);
            // TODO: update book with firebase
            db.editBook(book);
        }
        Toast.makeText(this, "Rating Saved", Toast.LENGTH_LONG).show();
        finish();
    }

    /**
     * get User based on username from Firebase
     */

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
                        // Get book object from Gson
                        Gson gson = new Gson();
                        Type tokenType = new TypeToken<Book>() {
                        }.getType();
                        book = gson.fromJson(jBook, tokenType); // here is where we get the user object
                        // do something with the book here
                        bookname = book.getName();
                        tvBookname.setText("Please review the book: " + bookname);
                    } else {
                        Log.d("FBerrorFragmentRequest", "User doesn't exist or string is empty");
                        tvBookname.setVisibility(View.GONE);
                        rbBook.setVisibility(View.GONE);
                        etBookComment.setVisibility(View.GONE);
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
                    // Get user object from Gson
                    Gson gson = new Gson();
                    Type tokenType = new TypeToken<User>() {
                    }.getType();
                    user = gson.fromJson(jUser, tokenType); // here is where we get the user object
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
