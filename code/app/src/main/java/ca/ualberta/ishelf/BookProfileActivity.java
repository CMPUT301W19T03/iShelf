package ca.ualberta.ishelf;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.util.Linkify;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

public class BookProfileActivity extends AppCompatActivity {
    private final String link = "https://ishelf-bb4e7.firebaseio.com";
    private Firebase ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_profile);
        final String TAG = "BookProfileActivity";



        Intent intent = getIntent();
        Book data = intent.getParcelableExtra("Book Data");
        Boolean vis = intent.getBooleanExtra("Button Visible", false);

        if(vis){

            Button delButton = (Button) findViewById(R.id.del);
            Button editButton =(Button) findViewById(R.id.edit);

            delButton.setVisibility(View.VISIBLE);
            editButton.setVisibility(View.VISIBLE);

        }




        String title = data.getName();
        String author = data.getAuthor();
        String genre = data.getGenre();
        String year = data.getYear();
        String description = data.getDescription();
        Long isbn = data.getISBN();
        final String owner = data.getOwner();



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

        /**
         * Retrieve the owner user info from Firebase so we can get their overall rating
         */

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
                    if (d.getKey().equals(owner)){    // user found
                        /**
                         * If the Owner is in Firebase
                         * retrieves the Owner object
                         * updates the RatingBar to display their overall rating
                         */
                        Log.d(TAG, "User found");
                        Gson gson = new Gson();
                        Type tokenType = new TypeToken<User>(){}.getType();
                        User user = gson.fromJson(d.getKey(), tokenType);
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
                    Log.d(TAG, "Username: [" + owner + "] is not in firebase");
                    final RatingBar ownerRatingBar = (RatingBar) findViewById(R.id.ownerRatingBar);
                    ownerRatingBar.setVisibility(View.GONE);
                }
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

    }


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

    public  void delete(View view){

        Intent intent = getIntent();
        int pos = intent.getIntExtra("pos data", 0);

        Intent newintent;
        newintent = new Intent(BookProfileActivity.this, MyAdapter.class);


        System.out.print("Here:");
        System.out.print(pos);
        newintent.putExtra("Pos", pos );

        newintent.putExtra("Check Data", false);


        setResult(RESULT_OK,newintent);
        finish();


    }

    public void viewProfile(View view){
        Intent intent = getIntent();
        Book data = intent.getParcelableExtra("Book Data");
        Intent newIntent = new Intent(this, ViewProfileActivity.class);
        newIntent.putExtra("Username", data.getOwner());
        startActivity(newIntent);
    }
}
