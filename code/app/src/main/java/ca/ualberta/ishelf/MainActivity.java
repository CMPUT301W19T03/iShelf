package ca.ualberta.ishelf;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
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

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        // Testing Database Object
        Database db = new Database(this);
        User user = new User();
        user.setUsername("testUsername");
        db.addUser(user);
        db.getUser("testUsername");
        db.deleteUser("testUsername");
        Book book = new Book();
        UUID id = new UUID(123l, 1234l);
        book.setId(id);
        db.addBook(book);
        db.getBook(book.getId());
        db.deleteBook(book.getId());
    }

}
