package ca.ualberta.ishelf;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.util.Linkify;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);

        TextView tvUsername = (TextView) findViewById(R.id.tvUsername);
        TextView tvPhoneNum = (TextView) findViewById(R.id.tvPhoneNum);
        TextView tvEmail = (TextView) findViewById(R.id.tvEmail);
        User user;


        // TODO: put below into a try/catch code block for if a user is not sent in with an activity
        Bundle bundle = getIntent().getExtras();
        user = (User) bundle.getSerializable("User");

        editProfileButton = findViewById(R.id.editProfileButton);
        // get the signed-in user's username
        String currentUsername = getSharedPreferences("UserPreferences", Context.MODE_PRIVATE).getString("username", null);

        if (user.getUsername() == currentUsername){
            // if profile we are viewing is the logged in user's
            editProfileButton.setVisibility(View.VISIBLE);
        } else {
            editProfileButton.setVisibility(View.GONE);
        }

        /*
        if (){//(bundle != null){
            user = (User) bundle.getSerializable("User");
        } else {
            user = new User();
            user.setUsername("Jeremy71");
            user.setPhoneNum("604-123-9876");
            Rating rating = new Rating();
            rating.setRating(4);
            rating.setComment("Very Fast");
            user.setRating(rating);
            user.setEmail("jsgray1@ualberta.ca");
        }
        */

        tvUsername.setText(user.getUsername());
        tvPhoneNum.setText("PHONE: " + user.getPhoneNum());
        tvEmail.setText("EMAIL: " + user.getEmail());
        Linkify.addLinks(tvPhoneNum, Linkify.PHONE_NUMBERS); // make phone number callable/textable
        Linkify.addLinks(tvEmail, Linkify.EMAIL_ADDRESSES); // make email clickable
    }

    public void EditProfile(){
        // when "Edit" button is clicked - "Edit" button is only viewable for the logged-in user
        // nothing needs to be sent in, since we are editing the logged-in user
        Intent intent = new Intent(this, EditProfileActivity.class);
        startActivity(intent);
    }
}