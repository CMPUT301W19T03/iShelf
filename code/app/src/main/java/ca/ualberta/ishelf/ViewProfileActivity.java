package ca.ualberta.ishelf;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

/**
 * ViewProfileActivity
 * Send in either:  key: "Username" - a Username in the form of a String
 *                  key: "User" - a User object
 *
 * This Activity shows the profile/user information of any user.
 * If the user is the signed in user, an "Edit" button is visible
 * The Edit button takes the user to the EditProfileActivity
 *
 * User Stories:
 * US 02.01.01
 * As an owner or borrower, I want a profile with a unique username and my contact information.
 *      -> This profile displays a user's unique username and contact information
 *
 * US 02.02.01:
 * As an owner or borrower, I want to edit the contact information in my profile.
 *      -> If user is viewing their profile, an edit button appears which takes
 *      them to the Edit Profile Activity
 *
 * US 02.03.01
 * As an owner or borrower, I want to retrieve and show the profile of a presented username.
 *      -> When a user clicks on a username somewhere in the app, it brings them to this profile
 *      to retrieve that user's information
 *
 * US 10.04.01 (1)
 * As a user, I want to be able to see an owners rating when viewing their profile.
 *      -> The rating of an owner is displayed when viewing their profile as a score out of 5 stars
 *
 * US 10.06.01 (1)
 * As a user, I want to be able to see a borrowers rating when viewing their profile.
 *      -> The rating of a borrower is displayed when viewing their profile as a score out of 5 stars
 *
 * Allows User to sign out of their account
 *
 *
 * TODO: add message to the screen if viewing the profile of no-one (aka nothing passed in)
 *
 * @author : Jeremy
 */
public class ViewProfileActivity extends AppCompatActivity {
    private String TAG = "ViewProfileActivity";
    private Button editProfileButton;
    private final String link = "https://ishelf-bb4e7.firebaseio.com";
    private Firebase ref;
    private TextView tvUsername;
    private TextView tvPhoneNum;
    private TextView tvEmail;
    private User user;
    private String username;
    private RatingBar ratingBar;
    private Button signOutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);
        String TAG = "viewProfile";

        // Initialize the various UI components (TextViews, RatingBar, Button)
        tvUsername = (TextView) findViewById(R.id.tvUsername);
        tvPhoneNum = (TextView) findViewById(R.id.tvPhoneNum);
        tvEmail = (TextView) findViewById(R.id.tvEmail);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        editProfileButton = (Button) findViewById(R.id.editProfileButton);
        signOutButton = (Button) findViewById(R.id.signOutButton);

        // Retrieve the signed-in user's username
        String currentUsername = getSharedPreferences("UserPreferences", Context.MODE_PRIVATE).getString("username", null);

        if (this.getIntent().hasExtra("Username")) {
            Log.d(TAG, "onCreate: USERNAME passed in");
            // If just a username is passed in
            Bundle bundle = this.getIntent().getExtras();
            username = bundle.getString("Username");
            Log.d(TAG, "onCreate: username passed in is: " + username);
            if (username != null) {
                getUser(username); // getUser also calls updateUI
            } else {
                Log.d(TAG, "onCreate: NOTHING PASSED IN");
                // When nothing is passed in
            }

            if (currentUsername.equals(username)) {
                // if profile we are viewing is the logged in user's
                Log.d(TAG, "onCreate: Viewing profile of currently logged in user");
                editProfileButton.setVisibility(View.VISIBLE);
                signOutButton.setVisibility(View.VISIBLE);
            } else {
                Log.d(TAG, "onCreate: Viewing profile of not the currently logged in user." +
                        " Passed in: " + username +
                        " Current: " + currentUsername);
                editProfileButton.setVisibility(View.GONE);
                signOutButton.setVisibility(View.GONE);
            }

        }
    }

    /**
     * This function is called when the Edit button is pressed
     * @param v the view
     */
    public void EditProfile(View v){
        // when "Edit" button is clicked - "Edit" button is only viewable for the logged-in user
        // nothing needs to be sent in, since we are editing the logged-in user
        Intent intent = new Intent(this, EditProfileActivity.class);
        startActivityForResult(intent, 3);
    }


    /**
     * This function is called When a user hits the Sign Out button
     * Removes the Shared Prefences username parameter and sends
     * the user back to Main Activity
     * @param v the view
     */
    public void SignOut(View v){
        SharedPreferences.Editor editor = getSharedPreferences("UserPreferences", Context.MODE_PRIVATE).edit();
        editor.putString("username", null).apply();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    /**
     * When we return from Edit Profile Activity
     * @param requestCode int
     * @param resultCode int
     * @param intent Intent
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        Log.d(TAG, "onActivityResult: requestCode: " + requestCode + " resultCode: " + resultCode);
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == 3) {
            Log.d(TAG, "onCreate: USER");
            username = intent.getStringExtra("Username");
            getUser(username);
        }
    }

    /**
     * Used for updating the UI elements
     * Called from getUsername
     * @param user User, the current user object we are displaying
     */
    public void updateUI(User user){
        Log.d(TAG, "in updateUI");
        tvUsername.setText(user.getUsername());
        username=user.getUsername();
        String phoneNumber = user.getPhoneNum();
        if (phoneNumber != null && phoneNumber.length() > 0) {
            tvPhoneNum.setText("PHONE: " + phoneNumber);
        } else {
            tvPhoneNum.setText("Phone number not provided");
        }
        String email = user.getEmail();
        if (email != null && email.length() > 0) {
            tvEmail.setText("EMAIL: " + email);
        } else {
            tvEmail.setText("Email not provided");
        }
        ratingBar.setRating(user.getOverallRating());
        ratingBar.setIsIndicator(true); // make it so the rating is non-changeable by the user
        Linkify.addLinks(tvPhoneNum, Linkify.PHONE_NUMBERS); // make phone number callable/textable
        Linkify.addLinks(tvEmail, Linkify.EMAIL_ADDRESSES); // make email clickable
    }

    public void reviewsClicked(View v){
        Intent intent = new Intent(this, ViewRatingsActivity.class);
        intent.putExtra("UserID", username);
        startActivity(intent);
    }

    /**
     * get a user based on username from the database
     * @author rmnattas
     */
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
                    // update the UI using the User object
                    updateUI(user);
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