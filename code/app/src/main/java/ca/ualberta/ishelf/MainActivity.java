package ca.ualberta.ishelf;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


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
        /*in actuality, it would be some sort of login view*/

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        loadFragment(new myBooksFragment());
    }
    private TextView mTextMessage;

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
//    public void addBook(View view){
//        fragment = new myBooksFragment();
//        ((myBooksFragment) fragment).addBook(view);
//    }

}
