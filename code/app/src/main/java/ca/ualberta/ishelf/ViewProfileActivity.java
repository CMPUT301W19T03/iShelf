package ca.ualberta.ishelf;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.util.Linkify;
import android.widget.TextView;

/**
 * ViewProfileActivity
 *
 * author: Jeremy
 */
public class ViewProfileActivity extends AppCompatActivity {
    /*
    TextView tvUsername = (TextView) findViewById(R.id.tvUsername);
    TextView tvContactInformation = (TextView) findViewById(R.id.tvContactInformation);
    */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);

        TextView tvUsername = (TextView) findViewById(R.id.tvUsername);
        TextView tvPhoneNum = (TextView) findViewById(R.id.tvPhoneNum);
        TextView tvEmail = (TextView) findViewById(R.id.tvEmail);
        User user;


        Bundle bundle = getIntent().getExtras();
        if (false){//(bundle != null){
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

        tvUsername.setText(user.getUsername());
        tvPhoneNum.setText("PHONE: " + user.getPhoneNum());
        tvEmail.setText("EMAIL: " + user.getEmail());
        Linkify.addLinks(tvPhoneNum, Linkify.PHONE_NUMBERS); // make phone number callable/textable
        Linkify.addLinks(tvEmail, Linkify.EMAIL_ADDRESSES); // make email clickable
    }
}