package ca.ualberta.ishelf;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.app.SearchManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.UUID;


/**
 * MainActivity
 *
 * This activity directs to the sign in activity when called
 * holds the navigations that goes to the different fragments
 * Holds the app bar which directs to different functionalities
 *
 * @author : Mehrab
 */

public class MainActivity extends AppCompatActivity {

    private static final String CHANNEL_ID = "1256";
    private static final int NOTIFICATION_ID = 192873641;
    // Here is my first comment
    private TextView mTextMessage;
    private Toolbar myToolbar;
    private SearchView searchView;
    private TextView appName;
    private ImageView profileIcon;
    // Firebase variables
    private final String link = "https://ishelf-bb4e7.firebaseio.com";
    private Firebase ref;


    private static final String TAG = "MainActivity";

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;

            switch (item.getItemId()) {
                case R.id.my_books:
                    selectedFragment = new myBooksFragment();
                    break;
                case R.id.borrow_books:
                    selectedFragment = new BorrowFragment();
                    //selectedFragment = new FavoritesFragment();
                    break;
                case R.id.request_books:
                    selectedFragment = new RequestFragment();
                    break;
            }

            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    selectedFragment).commit();

            return true;
        }
        };

    Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Necessary to activate notifications for the app
        createNotificationChannel();


        // Firebase listener, activated when there is a change to notifications
        // Will be activated regardless of activity, or if the app is even running
        // Set up test firebase listener
        Firebase.setAndroidContext(this);
        ref = new Firebase(link);
        Firebase tempRef = ref.child("Notifications");
        // create a one time use listener to immediately access datasnapshot
        tempRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d(TAG + " NotifcationEvent", "Notification listener activated");
                Log.d("snapshot values", dataSnapshot.getKey() + " " +
                        dataSnapshot.getValue().toString());
                String jNotification = dataSnapshot.getValue(String.class);
                if (jNotification != null) {
                    Log.d("jNotification", jNotification);
                    // Get user object from Gson
                    Gson gson = new Gson();
                    Type tokenType = new TypeToken<Notification>() {
                    }.getType();
                    Notification notification = gson.fromJson(jNotification, tokenType);
                    String username = getSharedPreferences("UserPreferences", Context.MODE_PRIVATE).getString("username", "TestUsername");
                    if (notification.getUserName().equals(username)) {
                        Log.d(TAG + " validNotification", "Firebase contains a valid username for this user");
                        pushNotification(notification.getText());
                    }
                }
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) { }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) { }
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) { }
            @Override
            public void onCancelled(FirebaseError firebaseError) {
                return;
            }
        });

        SignIn();


        mTextMessage = (TextView) findViewById(R.id.message);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        appName = (TextView) findViewById(R.id.app_name);
        profileIcon = (ImageView) findViewById(R.id.profile_icon);


        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        searchView = (SearchView) findViewById(R.id.searchView1);
        searchView.setQueryHint("Search for Books");





        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appName.setVisibility(View.INVISIBLE);
                profileIcon.setVisibility(View.INVISIBLE);
            }
        });

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                appName.setVisibility(View.VISIBLE);
                profileIcon.setVisibility(View.VISIBLE);
                return false;
            }
        });



        // for back button later
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);


        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new BorrowFragment()).commit();
        }

        //loadFragment(new myBooksFragment());
    }

    private void pushNotification(String text) {
        // Create the notification
        //TODO why does it show all notifications when starting up
        //TODO this could be fixed by deleting notifications when we see them
        String title = "Request";
        String content = text;
        // Create instance of notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                //TODO notification_icon is empty right now, that may be causing errors
                .setSmallIcon(R.drawable.ic_stat_name)
                .setContentTitle(title)
                .setContentText(content)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        // Set context for notification
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        // notificationId is a unique int for each notification that you must define
        //TODO fix notification id, might be okay since there is only 1 notification
        // Generate the notification
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.toolbar, menu);
//        return true;
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.action_favorite:
//                // User chose the "Settings" item, show the app settings UI...
//                Intent intent = new Intent(this, ViewProfileActivity.class);
//                String username = getSharedPreferences("UserPreferences", Context.MODE_PRIVATE).getString("username", "TestUsername");
//                Log.d(TAG, "onOptionsItemSelected: Username:" + username);
//                intent.putExtra("Username", username);
//                startActivity(intent);
//                finish();
//                return true;
//
//            case R.id.action_settings:
//                // User chose the "Favorite" action, mark the current item
//                // as a favorite...
//                return true;
//
//            default:
//                // If we got here, the user's action was not recognized.
//                // Invoke the superclass to handle it.
//                return super.onOptionsItemSelected(item);
//
//        }
//    }





//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.search_menu, menu);
//
//        MenuItem searchItem = menu.findItem(R.id.book_search);
//        SearchView searchView = (SearchView) searchItem.getActionView();
//
//        return true;
//    }

//    @Override
//    public boolean onQueryTextChange(String query) {
//        // Here is where we are going to implement the filter logic
//        return false;
//    }
//
//    @Override
//    public boolean onQueryTextSubmit(String query) {
//        return false;
//    }





//    public boolean onCreateOptionsMenu(Menu menu) {
//
//        final MenuItem searchItem = menu.findItem(R.id.book_search);
//        final android.support.v7.widget.SearchView searchView = (SearchView) searchItem.getActionView();
//        searchView.setQueryHint("test for something");
//
//        return true;
//    }

    /**
     * Check if a user is signed in, if not
     * go to the SignInActivity
     * @author rmnattas
     */
    private void SignIn(){

        // code to reset username in UserPreferences
//        SharedPreferences.Editor editor = getSharedPreferences("UserPreferences", Context.MODE_PRIVATE).edit();
//        editor.putString("username", null).apply();

        // Check if logged-in
        final String username = getSharedPreferences("UserPreferences", Context.MODE_PRIVATE).getString("username", null);
        if (username == null) {     // user is not logged-in
            Intent intent = new Intent(this, SignInActivity.class);
            startActivityForResult(intent, 1);
        } else {
            // connect to firebase
            final Database db = new Database(this);
            final Firebase ref = db.connect(this);

            Firebase tempRef = ref.child("Users");
            tempRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Boolean found = false;  // true if user found in firebase

                    // look for user in firebase
                    for (DataSnapshot d : dataSnapshot.getChildren()) {
                        if (d.getKey().equals(username)) {    // user found
                            Log.d("User", "User found");

                            found = true;
                            break;

                        }
                    }

                    if (!found) {
                        // logged in user not in firebase, clear user prefrences and go to signInActivity
                        SharedPreferences.Editor editor = getSharedPreferences("UserPreferences", Context.MODE_PRIVATE).edit();
                        editor.putString("username", null).apply();
                        Intent intent = new Intent(getBaseContext(), SignInActivity.class);
                        startActivityForResult(intent, 1);
                    }

                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });
        }

    }


    /**
     * When the profile icon is hit on the appbar
     * Takes the user to the ViewProfileActivity
     * @author Jeremy
     * @param v
     */
    public void ViewProfile(View v){
        Log.d(TAG, "ViewProfile: button clicked");
        Intent intent = new Intent(v.getContext(), ViewProfileActivity.class);
        String username = getSharedPreferences("UserPreferences", Context.MODE_PRIVATE).getString("username", "TestUsername");
        Log.d(TAG, "onOptionsItemSelected: Username:" + username);
        intent.putExtra("Username", username);
        startActivity(intent);
    }

    public void ViewNotification(View v){
        Intent intent = new Intent(v.getContext(), NotificationActivity.class);
        startActivity(intent);
    }
    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //TODO created string value resources for channel_name and channel_description
            // test_channel_name, test_description
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
