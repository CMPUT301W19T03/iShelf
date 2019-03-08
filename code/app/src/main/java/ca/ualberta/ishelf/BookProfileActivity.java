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
        String owner = data.getOwner();



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
        /*
        // Get user from the passed in username
        Firebase.setAndroidContext(this);
        ref = new Firebase(link);

        // get reference to specific entry
        Firebase tempRef = ref.child("Users").child(owner);
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
                    User user = gson.fromJson(jUser, tokenType); // here is where we get the user object

                    // TODO: update the rating in here
                    final RatingBar ownerRatingBar = (RatingBar) findViewById(R.id.ownerRatingBar);
                    ownerRatingBar.setRating(user.getOverallRating());
                } else {
                    Log.d("FBerror1", "User doesn't exist or string is empty");
                }
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {
                return;
            }
        });
        // TODO: retrieve user from firebase
        //final RatingBar ownerRatingBar = (RatingBar) findViewById(R.id.ownerRatingBar);
        //ownerRatingBar.setRating(3); //); // TODO: get user's user.getOverallRating() here
        */


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
        newintent = new Intent(BookProfileActivity.this, myBooksFragment.class);


        System.out.print("Here:");
        System.out.print(pos);
        newintent.putExtra("Pos Data", pos );

        newintent.putExtra("Check Data", false);


        setResult(RESULT_OK,newintent);
        finish();

    }
}
