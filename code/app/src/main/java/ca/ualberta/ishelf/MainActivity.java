package ca.ualberta.ishelf;

import android.content.Context;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
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
 * - Evan edited Feb 27
 */

public class MainActivity extends AppCompatActivity {
    Fragment fragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SignIn();

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        loadFragment(new myBooksFragment());
    }
    private TextView mTextMessage;

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

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment = null;
            switch (item.getItemId()) {
                case R.id.navigation_my_books:
                    fragment = new myBooksFragment();
                    break;
                case R.id.navigation_borrow:
                    mTextMessage.setText(R.string.borrow_books);
                    break;
                case R.id.navigation_requested:
                    mTextMessage.setText(R.string.requested_books);
                    break;
            }
            return loadFragment(fragment);
        }
    };
    private boolean loadFragment (Fragment fragment){ //should have error checking
        getSupportFragmentManager().beginTransaction().replace(R.id.recycler_fragment, fragment).commit();
        return true;
    }

}
