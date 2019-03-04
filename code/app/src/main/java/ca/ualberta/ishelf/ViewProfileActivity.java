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
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * ViewProfileActivity
 * Send in a parcelled User with your intent called "User"
 *
 * If the user is the signed in user, an "Edit" button is visible
 *
 * author: Jeremy
 */
public class ViewProfileActivity extends AppCompatActivity {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);
        String TAG = "editProfile";

        tvUsername = (TextView) findViewById(R.id.tvUsername);
        tvPhoneNum = (TextView) findViewById(R.id.tvPhoneNum);
        tvEmail = (TextView) findViewById(R.id.tvEmail);

        // get the signed-in user's username
        String currentUsername = getSharedPreferences("UserPreferences", Context.MODE_PRIVATE).getString("username", null);

        if (getIntent().hasExtra("Username")) {
            // If just a username is passed in
            Bundle bundle = getIntent().getExtras();
            username = (String) bundle.getSerializable("Username");

            // Get user from the passed in username
            Firebase.setAndroidContext(this);
            ref = new Firebase(link);

            // get reference to specific entry
            Firebase tempRef = ref.child("Users").child(username);
            final ArrayList<User> userList = new ArrayList<User>();
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

                        // fill the fields with their current info
                        tvUsername.setText(user.getUsername());
                        tvPhoneNum.setText("PHONE: " + user.getPhoneNum());
                        tvEmail.setText("EMAIL: " + user.getEmail());
                        Linkify.addLinks(tvPhoneNum, Linkify.PHONE_NUMBERS); // make phone number callable/textable
                        Linkify.addLinks(tvEmail, Linkify.EMAIL_ADDRESSES); // make email clickable

                        Log.d("Confirm", user.getUsername());
                        userList.add(user);
                    } else {
                        Log.d("FBerror1", "User doesn't exist or string is empty");
                    }
                    Log.d("Size", String.valueOf(userList.size()));
                }
            });


        } else if (getIntent().hasExtra("User")){
            // if the entire user is passed in
            user = (User) getIntent().getExtras().getSerializable("User");

            tvUsername.setText(user.getUsername());
            tvPhoneNum.setText("PHONE: " + user.getPhoneNum());
            tvEmail.setText("EMAIL: " + user.getEmail());
            Linkify.addLinks(tvPhoneNum, Linkify.PHONE_NUMBERS); // make phone number callable/textable
            Linkify.addLinks(tvEmail, Linkify.EMAIL_ADDRESSES); // make email clickable
        } else {
            // When nothing is passed in
            Log.d(TAG, "onCreate: nothing passed in");
            // TODO: show the logged in user's info?
        }

        editProfileButton = findViewById(R.id.editProfileButton);
        if (user.getUsername() == currentUsername){
            // if profile we are viewing is the logged in user's
            editProfileButton.setVisibility(View.VISIBLE);
        } else {
            editProfileButton.setVisibility(View.GONE);
        }

    }

    public void EditProfile(){
        // when "Edit" button is clicked - "Edit" button is only viewable for the logged-in user
        // nothing needs to be sent in, since we are editing the logged-in user
        Intent intent = new Intent(this, EditProfileActivity.class);
        startActivity(intent);
    }
}