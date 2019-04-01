package ca.ualberta.ishelf;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import ca.ualberta.ishelf.Models.Book;
import ca.ualberta.ishelf.Models.Database;
import ca.ualberta.ishelf.Models.Notification;
import ca.ualberta.ishelf.Models.Request;
import ca.ualberta.ishelf.Models.User;

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
 * @author : Mehrab
 */

public class BookProfileActivity extends AppCompatActivity {
    private final String link = "https://ishelf-bb4e7.firebaseio.com";
    private Firebase ref;
    private Book passedBook = null;
    final String TAG = "BookProfileActivity";
    private Button mapButton;
    private final int SCAN_AND_GET_DESCRIPTION = 212;
    private final int SCAN_AND_Accept_Borrower = 213;
    private final int SCAN_AND_Return = 214;
    private final int SCAN_AND_Accept_OWner = 215;

    // to see a gallery of books
    private Button galleryButton;
    private String ISBN = new String();
    private TextView cholder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_profile);



        // get the book object passed by intent
        Intent intent = getIntent();
        passedBook = intent.getParcelableExtra("Book Data");


        ImageView gallery_image = (ImageView) findViewById(R.id.image_book);

        ArrayList<String> images = passedBook.getGalleryImages();
        if (images.size() > 0) {
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageReference = storage.getReference();
            String image = images.get(0);
            StorageReference ref = storageReference.child(image);

            final long ONE_MEGABYTE = 1024 * 1024;
            ref.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    gallery_image.setImageBitmap(bitmap);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors
                }
            });
        }


        galleryButton = (Button) findViewById(R.id.gallery_button);

        galleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle extras = new Bundle();
                extras.putBoolean("has_book", true);
                extras.putParcelable("sent_book", passedBook);
                Intent intent = new Intent(view.getContext(), GalleryActivity.class);
                intent.putExtras(extras);
                startActivity(intent);
            }
        });

        Boolean canEdit = intent.getBooleanExtra("Button Visible", false);

        // get the signed-in user's username
        String currentUsername = getSharedPreferences("UserPreferences", Context.MODE_PRIVATE).getString("username", null);
        Boolean isOwner = (currentUsername.equals(passedBook.getOwner()));    // is the user the owner of this book
        Boolean isRequester =(currentUsername.equals(passedBook.getNext_holder()));

        if(isOwner && passedBook.getTransition()==0){
            canEdit = true;
        }

        String holder= passedBook.getHolder();
        Boolean isHolder =(currentUsername.equals(passedBook.getHolder()));
        if(isOwner && passedBook.getTransition()==1){
            Button lendButton =findViewById(R.id.lend);
            canEdit=false;
            lendButton.setVisibility(View.VISIBLE);

        }
        if((isOwner || isHolder || isRequester)&& passedBook.getTransition()>0){
            Button mapButton =findViewById(R.id.map);
            canEdit=false;
            mapButton.setVisibility(View.VISIBLE);
        }

        if(!isHolder &&isRequester&& (passedBook.getTransition()==2||passedBook.getTransition()==4)){
            Button acptButton =findViewById(R.id.acpt);
            canEdit=false;
            acptButton.setVisibility(View.VISIBLE);
        }

        if(isHolder && passedBook.getTransition()==3){
            Button retButton =findViewById(R.id.ret);
            retButton.setVisibility(View.VISIBLE);
            canEdit = false;

        }




        // TODO edit from fragment request
        if(canEdit&& isHolder && isOwner && !isRequester){
            // show the edit and delete book buttons
            Button delButton = findViewById(R.id.del);
            Button editButton = findViewById(R.id.edit);
            Button reqButton = findViewById(R.id.req);

            delButton.setVisibility(View.VISIBLE);
            editButton.setVisibility(View.VISIBLE);
            reqButton.setVisibility(View.VISIBLE);
        }

        if (!isOwner && !passedBook.checkBorrowed() && !isHolder && !isRequester){
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
        if (passedBook.getTransition() == 0){
            textView6.setText("AVAILABLE");
        }else{
            textView6.setText("BORROWED");
        }
        cholder = (TextView) findViewById(R.id.current_holder);
        cholder.setText(passedBook.getHolder());
        TextView ownerUsername = findViewById(R.id.ownerUsername);
        ownerUsername.setPaintFlags(ownerUsername.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);
        ownerUsername.setText(owner);

        RatingBar bookRating = findViewById(R.id.bookRatingBar);
        bookRating.setRating(passedBook.getAvgRating());

        getOwner();

        mapButton = findViewById(R.id.map);

        // check if the logged in user have requested the book and disable the request button
        if(!isOwner){
            haveRequested();
        }

    }

    /**
     * check if the logged in user have requested the book and disable the request button
     * @rmnattas
     */
    public void haveRequested(){
        // get logged in username
        final String username = getSharedPreferences("UserPreferences", Context.MODE_PRIVATE).getString("username", null);

        //connect to firebase
        Database db = new Database(this);
        Firebase fb = db.connect(this);
        Firebase childRef = fb.child("Requests");

        childRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot d: dataSnapshot.getChildren()) {
                    String jRequest = d.getValue(String.class);
                    if (jRequest != null) {
                        // Get Requests object from Gson
                        Gson gson = new Gson();
                        Type tokenType = new TypeToken<Request>() {
                        }.getType();
                        Request request = gson.fromJson(jRequest, tokenType);
                        if(request.getBookId().equals(passedBook.getId()) && request.getRequester().equals(username)){
                            Button bkingButton = findViewById(R.id.bking);
                            bkingButton.setBackground(getDrawable(R.drawable.roundedbuttongray));
                            bkingButton.setText("Requested");
                            bkingButton.setEnabled(false);
                            break;
                        }
                    }
                }

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                return;
            }

        });

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
                // look for user in firebase
                for(DataSnapshot d: dataSnapshot.getChildren()) {
                    if (d.getKey().equals(passedBook.getOwner())){    // user found
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
                        ownerRatingBar.setVisibility(View.VISIBLE);
                        ownerRatingBar.setIsIndicator(true);
                        float rating = user.getOverallRating();
                        ownerRatingBar.setRating(rating);
                    }
                }
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }


    public void lend(View v){
        ISBN = "";
        Intent intent = new Intent(BookProfileActivity.this, ScanActivity.class);
        Bundle extras = new Bundle();
        extras.putString("task", "lend");
        intent.putExtras(extras);
        startActivityForResult(intent, SCAN_AND_GET_DESCRIPTION);
    }

    public void accept(View v){
        Button acptButton =findViewById(R.id.acpt);
        acptButton.setVisibility(View.INVISIBLE);
        if(passedBook.getTransition()==2)
        {
            ISBN = "";
            Intent intent = new Intent(BookProfileActivity.this, ScanActivity.class);
            Bundle extras = new Bundle();
            extras.putString("task", "lend");
            intent.putExtras(extras);
            startActivityForResult(intent, SCAN_AND_Accept_Borrower);
        }
        if(passedBook.getTransition()==4){
            ISBN = "";
            Intent intent = new Intent(BookProfileActivity.this, ScanActivity.class);
            Bundle extras = new Bundle();
            extras.putString("task", "lend");
            intent.putExtras(extras);
            startActivityForResult(intent, SCAN_AND_Accept_OWner);

        }

    }

    public void deleteRequest(String bookId){
        // get logged in username
        final String username = getSharedPreferences("UserPreferences", Context.MODE_PRIVATE).getString("username", null);

        //connect to firebase
        Database db = new Database(this);
        Firebase fb = db.connect(this);
        Firebase childRef = fb.child("Requests");

        childRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot d: dataSnapshot.getChildren()) {
                    String jRequest = d.getValue(String.class);
                    if (jRequest != null) {
                        // Get Requests object from Gson
                        Gson gson = new Gson();
                        Type tokenType = new TypeToken<Request>() {
                        }.getType();
                        Request request = gson.fromJson(jRequest, tokenType);
                        if(request.getBookId().equals(bookId) && request.getOwner().equals(username)){
                            db.deleteRequest(request.getId().toString());
                            break;
                        }
                    }
                }

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                return;
            }

        });

    }

    /**
     * add the bookId to the user list of borrowedBooks
     * @param username user to add to
     * @param bookId book id to save
     * @author rmnattas
     */
    private void addToUserBorrowList(final String username, final UUID bookId){
        // get the user object from firebase
        final Database db = new Database(this);
        Firebase ref = db.connect(this);
        Firebase tempRef = ref.child("Users").child(username);
        tempRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String jUser = dataSnapshot.getValue(String.class);
//                Log.d("jUser", jUser);
                if (jUser != null) {
                    // Get user object from Gson
                    Gson gson = new Gson();
                    Type tokenType = new TypeToken<User>() {
                    }.getType();
                    User user = gson.fromJson(jUser, tokenType); // here is where we get the user object
                    user.addBorrowedBook(bookId);
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

    /**
     * remove the bookId from the user list of borrowedBooks
     * @param username user to remove from
     * @param bookId book id to remove
     * @author rmnattas
     */
    private void removeToUserBorrowList(final String username, final UUID bookId){
        // get the user object from firebase
        final Database db = new Database(this);
        Firebase ref = db.connect(this);
        Firebase tempRef = ref.child("Users").child(username);
        tempRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String jUser = dataSnapshot.getValue(String.class);
//                Log.d("jUser", jUser);
                if (jUser != null) {
                    // Get user object from Gson
                    Gson gson = new Gson();
                    Type tokenType = new TypeToken<User>() {
                    }.getType();
                    User user = gson.fromJson(jUser, tokenType); // here is where we get the user object
                    user.removeBorrowedBook(bookId);
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

    public void returnClicked(View v){
        ISBN = "";
        Intent intent = new Intent(BookProfileActivity.this, ScanActivity.class);
        Bundle extras = new Bundle();
        extras.putString("task", "lend");
        intent.putExtras(extras);
        startActivityForResult(intent, SCAN_AND_Return);
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

        if(passedBook.getTransition() != 0){
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Book cannot be deleted \n Book is in a transition state",
                    Toast.LENGTH_LONG);
            toast.show();
            return;
        }

        // get logged in user username
        final String currentUsername = getSharedPreferences("UserPreferences", Context.MODE_PRIVATE).getString("username", null);
        DeleteBookFromUser(currentUsername, passedBook.getId());

        // delete all requests for this book
        deleteBookRequests(passedBook.getId());

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


    /**
     * Delete all requests for a book
     * (Called when a book gets deleted
     * @param bookId the id for the book
     * @author rmnattas
     */
    public void deleteBookRequests(final UUID bookId){
        //connect to firebase
        final Database db = new Database(this);
        Firebase fb = db.connect(this);
        Firebase childRef = fb.child("Requests");

        childRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot d: dataSnapshot.getChildren()) {
                    String jRequest = d.getValue(String.class);
                    // Get book object from Gson
                    Gson gson = new Gson();
                    Type tokenType = new TypeToken<Request>() {}.getType();
                    Request request = gson.fromJson(jRequest, tokenType); // here is where we get the user object
                    if (request.getBookId().equals(bookId)){
                        db.deleteRequest(request.getId().toString());
                    }
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
     * @param v the view
     * @author rmnattas
     */
    public void requestClicked(View v){
        // set a request object


        Intent intent2 = new Intent(BookProfileActivity.this, ListOfRequestsActivity.class);

        String bookID = passedBook.getId().toString();


        // add the request to the book owner listOfRequests





        intent2.putExtra("ID",bookID);
        intent2.putExtra("book", passedBook);
        startActivity(intent2);
        finish();
    }

    public void Booking(View v){

        Request request = new Request();
        request.setBookId(passedBook.getId());
        // set requester username
        String currentUsername = getSharedPreferences("UserPreferences", Context.MODE_PRIVATE).getString("username", null);
        request.setRequester(currentUsername);
        request.setOwner(passedBook.getOwner());
        // set request time
        request.setTimeRequested(Calendar.getInstance().getTime());

        // add the request to the book owner listOfRequests
        Database db = new Database(this);
        db.addRequest(request);

        // create a notification and add it to Firebase
        Notification notification = new Notification(new Date(),
                currentUsername + " has requested " + passedBook.getName(),
                passedBook.getOwner());
        db.addNotification(notification);

        Toast toast = Toast.makeText(getApplicationContext(),
                "Book Requested",
                Toast.LENGTH_LONG);
        toast.show();

        haveRequested();
    }

    public void MapButton(View v){
        // set requester username
        String currentUsername = getSharedPreferences("UserPreferences", Context.MODE_PRIVATE).getString("username", null);
        getRequest(currentUsername, passedBook.getId());
    }

    public void getRequest(final String username, final UUID bookId){
        //connect to firebase
        Database db = new Database(this);
        Firebase fb = db.connect(this);
        Firebase childRef = fb.child("Requests");

        childRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Request request = new Request();
                for(DataSnapshot d: dataSnapshot.getChildren()) {
                    String jRequest = d.getValue(String.class);
                    if (jRequest != null) {
                        // Get Requests object from Gson
                        Gson gson = new Gson();
                        Type tokenType = new TypeToken<Request>() {
                        }.getType();
                        Request newRequest = gson.fromJson(jRequest, tokenType);
                        if ((newRequest.getRequester().equals(username) || newRequest.getOwner().equals(username) ) && newRequest.getBookId().equals(bookId)) {
                            request = newRequest;
                            break;
                        }
                    } else {
                        Log.d(TAG, "ERROR #123121");
                    }
                }

                Intent mapIntent = new Intent(getBaseContext(), MapsActivity.class);
                mapIntent.putExtra("Request", request);
                startActivity(mapIntent);

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                return;
            }

        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SCAN_AND_GET_DESCRIPTION && resultCode == Activity.RESULT_OK) {
            ISBN = data.getStringExtra("ISBN");
            if(passedBook.getISBN().equals(Long.valueOf(ISBN).longValue())){
                Button lendButton =findViewById(R.id.lend);
                lendButton.setVisibility(View.INVISIBLE);
                passedBook.setTransition(2);
                Database db = new Database(this);
                db.editBook(passedBook);
                Toast.makeText(this, "Correct Book",
                        Toast.LENGTH_LONG).show();
            }
            else {
                Toast.makeText(this, "Wrong Book",
                        Toast.LENGTH_LONG).show();
            }
        }
        else if(requestCode == SCAN_AND_Return && resultCode == Activity.RESULT_OK) {

            ISBN = data.getStringExtra("ISBN");
            if(passedBook.getISBN().equals(Long.valueOf(ISBN).longValue())){
                Button retButton =findViewById(R.id.ret);
                retButton.setVisibility(View.INVISIBLE);
                passedBook.setTransition(4);
                Database db =new Database(this );
                db.editBook(passedBook);

                // get the borrower to rate the book owner and book condition
                String bookOwnerName = passedBook.getOwner();
                String bookName = passedBook.getName();
                UUID bookID = passedBook.getId();
                Intent ratingIntent = new Intent(this, RatingActivity.class);
                ratingIntent.putExtra("User", bookOwnerName);
                ratingIntent.putExtra("bookName", bookName);
                ratingIntent.putExtra("BookID", bookID.toString());
                startActivity(ratingIntent);
            }
            else {
            Toast.makeText(this, "Wrong Book",
                    Toast.LENGTH_LONG).show();
            }

        }
        else if(requestCode == SCAN_AND_Accept_Borrower && resultCode == Activity.RESULT_OK){
            ISBN = data.getStringExtra("ISBN");
            if(passedBook.getISBN().equals(Long.valueOf(ISBN).longValue())){
                passedBook.setBorrowedBook(true);
                passedBook.setBorrowed();
                passedBook.setTransition(3);
                String temp = passedBook.getHolder();
                //final String currentUsername = getSharedPreferences("UserPreferences", Context.MODE_PRIVATE).getString("username", null);

                passedBook.setHolder(passedBook.getNext_holder());
                passedBook.setNext_holder(temp);
                Database db =new Database(this );
                db.editBook(passedBook);
                // Add the book ID to the new holder borrowedBooks list
                addToUserBorrowList(passedBook.getHolder(), passedBook.getId());
            }
            else {
                Toast.makeText(this, "Wrong Book",
                        Toast.LENGTH_LONG).show();
            }

        }
        else if(requestCode == SCAN_AND_Accept_OWner&& resultCode == Activity.RESULT_OK){
            // Remove the book ID to the new holder borrowedBooks list


            ISBN = data.getStringExtra("ISBN");
            if(passedBook.getISBN().equals(Long.valueOf(ISBN).longValue())){
                String borrower = passedBook.getHolder(); // get the borrower's name
                removeToUserBorrowList(passedBook.getHolder(), passedBook.getId());
                passedBook.setBorrowedBook(false);
                passedBook.setAvailable();
                passedBook.setTransition(0);
                final String currentUsername = getSharedPreferences("UserPreferences", Context.MODE_PRIVATE).getString("username", null);

                deleteRequest(passedBook.getId().toString());

                passedBook.setHolder(passedBook.getOwner());
                passedBook.setNext_holder(null);
                Database db =new Database(this );
                db.editBook(passedBook);

                // Get the Owner to review the Borrower
                Intent reviewUser = new Intent(this, RatingActivity.class);
                reviewUser.putExtra("User", borrower);
                startActivity(reviewUser);
            }
            else {
                Toast.makeText(this, "Wrong Book",
                        Toast.LENGTH_LONG).show();
            }

        }


    }

    public void reviewsClicked(View v){
        Intent intent = new Intent(this, ViewRatingsActivity.class);
        intent.putExtra("BookID", passedBook.getId().toString());
        startActivity(intent);
    }

}
