package ca.ualberta.ishelf;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.util.Linkify;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.w3c.dom.Text;

import java.lang.reflect.Type;
import java.util.Calendar;
import java.util.UUID;

/**
 * BookProfileActivity
 * Send in either:
 *                  key: "Book" - a Book object
 *
 * US 01.06.01
 * s an owner, I want to view and edit a book description in my books.
 *   if he enters it in by mistake or the book description is updated by the author
 *
 * this lets the user view the book in detail, also allows the user to go into the
 * edit menu
 *
 * US 01.07.01
 * As an owner, I want to delete a book in my books.
 *   the owner loses a book or stops wanting to lend it out
 *
 * This activity allows the user to delete the book
 *
 *US 01.03.01
 * As an owner or borrower, I want a book to have a status of one of: available, requested, accepted, or borrowed.
 * the user wants to see if books are available and its current status
 *
 * US 01.02.01
 * As an owner, I want the book description by scanning it off the book (at least the ISBN).
 * the owner wants to be able to easily add his new books(Not done yet)
 *
 *
 *
 * @author: Mehrab
 */

public class BookProfileActivity extends AppCompatActivity {
    private final String link = "https://ishelf-bb4e7.firebaseio.com";
    private Firebase ref;
    private Book passedBook = null;
    final String TAG = "BookProfileActivity";

    // to see a gallery of books
    private Button galleryButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_profile);

        galleryButton = (Button) findViewById(R.id.gallery_button);

        galleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle extras = new Bundle();
                extras.putParcelable("sent_book", passedBook);
                Intent intent = new Intent(view.getContext(), GalleryActivity.class);
                intent.putExtras(extras);
                startActivity(intent);
            }
        });

        // get the book object passed by intent
        Intent intent = getIntent();
        passedBook = intent.getParcelableExtra("Book Data");
        Boolean canEdit = intent.getBooleanExtra("Button Visible", false);

        // get the signed-in user's username
        String currentUsername = getSharedPreferences("UserPreferences", Context.MODE_PRIVATE).getString("username", null);
        Boolean isOwner = (currentUsername.equals(passedBook.getOwner()));    // is the user the owner of this book


        if(canEdit){
            // show the edit and delete book buttons
            Button delButton = findViewById(R.id.del);
            Button editButton = findViewById(R.id.edit);
            Button reqButton = findViewById(R.id.req);

            delButton.setVisibility(View.VISIBLE);
            editButton.setVisibility(View.VISIBLE);
            reqButton.setVisibility(View.VISIBLE);
        }

        if (!isOwner && !passedBook.checkBorrowed()){
            Button bkingButton = findViewById(R.id.bking);
            bkingButton.setVisibility(View.VISIBLE);
        }


        //gets all the elements in the object
        String title = passedBook.getName();
        String author = passedBook.getAuthor();
        String genre = passedBook.getGenre();
        String year = passedBook.getYear();
        String description = passedBook.getDescription();
        Long isbn = passedBook.getISBN();
        final String owner = passedBook.getOwner();

        //sets them onto the text views of the activity
        TextView textView = findViewById(R.id.Title);
        textView.setText(title);

        TextView textView1 = findViewById(R.id.Author);
        textView1.setText(author);

        TextView textView2 = findViewById(R.id.genre);
        textView2.setText(genre);

        TextView textView3 = findViewById(R.id.Year);
        textView3.setText(year);

        TextView textView4 = findViewById(R.id.des);
        textView4.setText(description);

        TextView textView5 = findViewById(R.id.ISBN);
        textView5.setText(Long.toString(isbn));

        TextView textView6 = findViewById(R.id.status);
        textView6.setText("AVAILABLE");

        TextView ownerUsername = findViewById(R.id.ownerUsername);
        ownerUsername.setText(owner);

        getOwner();

    }

    /**
     * Retrieve the owner user info from Firebase so we can get their overall rating
     */
    public void getOwner(){
        // connect to firebase
        final Database db = new Database(this);
        final Firebase ref = db.connect(this);

        Firebase tempRef = ref.child("Users");
        tempRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Boolean found = false;  // true if user found in firebase

                // look for user in firebase
                for(DataSnapshot d: dataSnapshot.getChildren()) {
                    if (d.getKey().equals(passedBook)){    // user found
                        /**
                         * If the Owner is in Firebase
                         * retrieves the Owner object
                         * updates the RatingBar to display their overall rating
                         */
                        Log.d(TAG, "User found");
                        Gson gson = new Gson();
                        Type tokenType = new TypeToken<User>(){}.getType();
                        User user = gson.fromJson(d.getValue().toString(), tokenType);
                        final RatingBar ownerRatingBar = (RatingBar) findViewById(R.id.ownerRatingBar);
                        float rating = user.getOverallRating();
                        ownerRatingBar.setRating(rating);
                    }
                }

                if (!found) {
                    /**
                     * If the Owner is not in Firebase
                     * Prints a debug log
                     * Hide the RatingBar
                     */
                    Log.d(TAG, "Username: [" + passedBook.getOwner() + "] is not in firebase");
                    final RatingBar ownerRatingBar = (RatingBar) findViewById(R.id.ownerRatingBar);
                    ownerRatingBar.setVisibility(View.GONE);
                }
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    //sends parcelable data into the edit book activity and goes the by intent
    public void edit(View v){

        Intent intent = getIntent();
        Book data = intent.getParcelableExtra("Book Data");
        int pos = intent.getIntExtra("pos data", 0);

        Intent newINTent = new Intent(BookProfileActivity.this, EditBookActivity.class);

        newINTent.putExtra("Book Data", data);
        newINTent.putExtra("Pos Data",pos );
        newINTent.putExtra("Check Data", true);

        startActivity(newINTent);
        finish();

    }

    public void request(View v){


    }
    //goes back to myBookFragment, sending with it the position of the book that needs to be
    //deleted
    public  void delete(View view){

        // delete book from firebase
        final Database db = new Database(this);
        db.deleteBook(passedBook.getId());

        // get logged in user username
        final String currentUsername = getSharedPreferences("UserPreferences", Context.MODE_PRIVATE).getString("username", null);
        DeleteBookFromUser(currentUsername, passedBook.getId());

        Intent intent = new Intent();
        intent.putExtra("edit", false);
        intent.putExtra("Book", passedBook);
        setResult(RESULT_OK, intent);
        finish();

    }

    /**
     * add the book to the user owned list and update firebase
     * @author rmnattas
     */
    public void DeleteBookFromUser(final String username, final UUID bookId){
        // get the user object from firebase
        final Database db = new Database(this);
        Firebase ref = db.connect(this);
        Firebase tempRef = ref.child("Users").child(username);
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
                    User user = gson.fromJson(jUser, tokenType); // here is where we get the user object
                    user.deleteOwnedBook(bookId);
                    db.editUser(username, user);
                } else {
                    Log.d("myBookFrag", "11321");
                }
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {
                return;
            }
        });
    }
    //goes into viewProfile if the owner is clicked
    public void viewProfile(View view){
        Intent intent = getIntent();
        Book data = intent.getParcelableExtra("Book Data");
        Intent newIntent = new Intent(this, ViewProfileActivity.class);
        newIntent.putExtra("Username", data.getOwner());
        startActivity(newIntent);
    }

    /**
     * called when the request button is clicked
     * @param v
     * @author rmnattas
     */
    public void requestClicked(View v){
        // set a request object


        Intent intent2 = new Intent(BookProfileActivity.this, ListOfRequestsActivity.class);

        Request request = new Request();
        request.setBookId(passedBook.getId());
        // set requester username
        //TODO requester shouldn't be the current user
        String currentUsername = getSharedPreferences("UserPreferences", Context.MODE_PRIVATE).getString("username", null);
        request.setRequester(currentUsername);
        // set request time
        request.setTimeRequested(Calendar.getInstance().getTime());

        // add the request to the book owner listOfRequests


        intent2.putExtra("request",request);
        startActivity(intent2);
        finish();
    }

    public void Booking(View v){

        Request request = new Request();
        request.setBookId(passedBook.getId());
        // set requester username
        String currentUsername = getSharedPreferences("UserPreferences", Context.MODE_PRIVATE).getString("username", null);
        request.setRequester(currentUsername);
        // set request time
        request.setTimeRequested(Calendar.getInstance().getTime());

        // add the request to the book owner listOfRequests
        Database db = new Database(this);
        db.addRequest(passedBook.getOwner(), request);
        Toast toast = Toast.makeText(getApplicationContext(),
                "Book Requested",
                Toast.LENGTH_LONG);
        toast.show();

    }

}
