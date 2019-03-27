package ca.ualberta.ishelf;

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

        if (intent.hasExtra("Book")){
            bookname = intent.getStringExtra("Book");
            Log.d(TAG, "onCreate: Bookname: " + bookname);
            // TODO: retrieve Book from Firebase using bookname
        }
        
        if (intent.hasExtra("BookID")){
            bookID = intent.getStringExtra("BookID");
            Log.d(TAG, "onCreate: BookID: " + bookID);
        }

        // TODO: delete this block once Firebase is implemented
        // retrieve user's name
        user = new User();
        user.setUsername("Username1");
        user.setEmail("email@email.com");
        user.setPhoneNum("123-456-0000");

        //book = new Book();
        //book.setName("Peppa Pig");

        // update
        tvUsername.setText("Please review " + user.getUsername());

        // check if book exists, if it doesn't we need to hide it, else we need to set it
        if (book != null) {
            // book exists, so update the relevant UI elements
            bookname = book.getName();
            tvBookname.setText("Please review the book: " + bookname);
        } else {
            // book does not exist, so remove all relevant UI elements
            tvBookname.setVisibility(View.GONE);
            rbBook.setVisibility(View.GONE);
            etBookComment.setVisibility(View.GONE);
        }

    }

    public void saveButton(View v){
        Rating userRating = new Rating(rbUser.getRating(), etUserComment.getText().toString());
        user.setRating(userRating);
        // TODO: update user with firebase

        if (book != null){
            Rating bookRating = new Rating(rbBook.getRating(), etBookComment.getText().toString());
            book.addRating(bookRating);
            // TODO: update book with firebase
        }
        Toast.makeText(this, "Rating Saved", Toast.LENGTH_LONG).show();
        finish();
    }
}
