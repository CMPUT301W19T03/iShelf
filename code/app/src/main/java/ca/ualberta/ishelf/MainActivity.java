package ca.ualberta.ishelf;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.UUID;


/**
 * MainActivity
 *
 * - mehrab edit
 */

public class MainActivity extends AppCompatActivity {

    // Here is my first comment
    private TextView mTextMessage;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.navigation_dashboard:
                    mTextMessage.setText(R.string.title_dashboard);
                    return true;
                case R.id.navigation_notifications:
                    mTextMessage.setText(R.string.title_notifications);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SignIn();

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    private void SignIn(){

        // code to reset username in UserPreferences
//        SharedPreferences.Editor editor = getSharedPreferences("UserPreferences", Context.MODE_PRIVATE).edit();
//        editor.putString("username", null).apply();

        // Check if logged-in
        String username = getSharedPreferences("UserPreferences", Context.MODE_PRIVATE).getString("username", null);
        if (username == null) {     // user is not logged-in
            Intent intent = new Intent(this, SignInActivity.class);
            startActivityForResult(intent, 1);
        }
    }


    private void button1(View v){
        User user1 = new User();
        user1.setUsername("User1_username");
        user1.setPhoneNum("809-888-1234");
        user1.setEmail("user1@test.com");

        Intent intent = new Intent(this, ViewProfileActivity.class);
        intent.putExtra("User", user1);
        startActivity(intent);
    }

    private void button2(View v){
        User user2 = new User();
        user2.setUsername("User2 USERNAME");
        user2.setPhoneNum("222-222-2222");
        user2.setEmail("user2@t22est.com");

        Intent intent = new Intent(this, ViewProfileActivity.class);
        intent.putExtra("User", user2);
        startActivity(intent);
    }

    private void button3(View v){
        Intent intent = new Intent(this, ViewProfileActivity.class);
        intent.putExtra("User", user3);
        startActivity(intent);

    }


}
