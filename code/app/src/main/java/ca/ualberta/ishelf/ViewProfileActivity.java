package ca.ualberta.ishelf;

import android.content.Context;
import android.content.Intent;
import android.nfc.Tag;
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

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * ViewProfileActivity
 * Send in either:  - a Username in the form of a String
 *                  - a User object
 * Send in a parcelled User with your intent called "User"
 *
 * This Activity shows the profile/user information of any user.
 * If the user is the signed in user, an "Edit" button is visible
 * The Edit button takes the user to the EditProfileActivity
 *
 * TODO: add message to the screen if viewing the profile of no-one (aka nothing passed in)
 *
 * author: Jeremy
 */
public class ViewProfileActivity extends AppCompatActivity {
    private String TAG = "ViewProfileActivity";
    /*
    TextView tvUsername = (TextView) findViewById(R.id.tvUsername);
    TextView tvContactInformation = (TextView) findViewById(R.id.tvContactInformation);
    */
    Button editProfileButton;
    private final String link = "https://ishelf-bb4e7.firebaseio.com";
    private Firebase ref;
    TextView tvUsername;
    TextView tvPhoneNum;
    TextView tvEmail;
    User user;
    String username;
    RatingBar ratingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);
        String TAG = "viewProfile";

        // initialize the various TextViews
        tvUsername = (TextView) findViewById(R.id.tvUsername);
        tvPhoneNum = (TextView) findViewById(R.id.tvPhoneNum);
        tvEmail = (TextView) findViewById(R.id.tvEmail);
        // initialize the RatingBar
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);

        // get the signed-in user's username
        String currentUsername = getSharedPreferences("UserPreferences", Context.MODE_PRIVATE).getString("username", null);

        if (this.getIntent().hasExtra("Username")) {
            Log.d(TAG, "onCreate: USERNAME passed in");
            // If just a username is passed in
            Bundle bundle = this.getIntent().getExtras();
            username = (String) bundle.getSerializable("Username");
            Log.d(TAG, "onCreate: username passed in is: " + username);
            if (username != null) {
                tvUsername.setText(username);

                // Get user from the passed in username
                Firebase.setAndroidContext(this);
                ref = new Firebase(link);

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
                            Type tokenType = new TypeToken<User>() {
                            }.getType();
                            user = gson.fromJson(jUser, tokenType); // here is where we get the user object

                            // fill the fields with their current info
                            tvPhoneNum.setText("PHONE: " + user.getPhoneNum());
                            tvEmail.setText("EMAIL: " + user.getEmail());

                            // set the Rating
                            ratingBar.setRating(user.getOverallRating());
                            ratingBar.setIsIndicator(true);
                            Linkify.addLinks(tvPhoneNum, Linkify.PHONE_NUMBERS); // make phone number callable/textable
                            Linkify.addLinks(tvEmail, Linkify.EMAIL_ADDRESSES); // make email clickable
                            username = user.getUsername();

                            Log.d("Confirm", user.getUsername());
                        } else {
                            Log.d("FBerror1", "User doesn't exist or string is empty");
                        }
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {
                        return;
                    }
                });
            } else {
                Log.d(TAG, "onCreate: Username passed in is Null");
            }


        } else if (this.getIntent().hasExtra("User")){
            Log.d(TAG, "onCreate: USER");

            // if the entire user is passed in
            user = (User) this.getIntent().getExtras().getSerializable("User");
            username = user.getUsername();
            tvUsername.setText(user.getUsername());
            tvPhoneNum.setText("PHONE: " + user.getPhoneNum());
            tvEmail.setText("EMAIL: " + user.getEmail());
            ratingBar.setRating(user.getOverallRating());
            ratingBar.setIsIndicator(true); // make it so the rating is non-changeable by the user
            Linkify.addLinks(tvPhoneNum, Linkify.PHONE_NUMBERS); // make phone number callable/textable
            Linkify.addLinks(tvEmail, Linkify.EMAIL_ADDRESSES); // make email clickable
        } else {
            Log.d(TAG, "onCreate: NOTHING PASSED IN");
            // When nothing is passed in
            // TODO: show the logged in user's info?
        }

        editProfileButton = findViewById(R.id.editProfileButton);
        if (currentUsername.equals(username)){
            // if profile we are viewing is the logged in user's
            Log.d(TAG, "onCreate: logged in user");
            editProfileButton.setVisibility(View.VISIBLE);
        } else {
            Log.d(TAG, "onCreate: not logged in user" + username + currentUsername);
            editProfileButton.setVisibility(View.GONE);
        }

    }

    public void EditProfile(View v){
        // when "Edit" button is clicked - "Edit" button is only viewable for the logged-in user
        // nothing needs to be sent in, since we are editing the logged-in user
        Intent intent = new Intent(this, EditProfileActivity.class);
        startActivityForResult(intent, 3);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == 3) {

            Log.d(TAG, "onCreate: USER");

            // if the entire user is passed in
            user = (User) intent.getExtras().getSerializable("User");
            username = user.getUsername();
            tvUsername.setText(user.getUsername());
            tvPhoneNum.setText("PHONE: " + user.getPhoneNum());
            tvEmail.setText("EMAIL: " + user.getEmail());
            Linkify.addLinks(tvPhoneNum, Linkify.PHONE_NUMBERS); // make phone number callable/textable
            Linkify.addLinks(tvEmail, Linkify.EMAIL_ADDRESSES); // make email clickable

        }
    }

}