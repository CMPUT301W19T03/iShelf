package ca.ualberta.ishelf_lab9;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
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
import android.widget.ImageView;
import android.widget.SearchView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.graphics.Color;

import com.firebase.client.ChildEventListener;
        import com.firebase.client.DataSnapshot;
        import com.firebase.client.Firebase;
        import com.firebase.client.FirebaseError;
        import com.firebase.client.ValueEventListener;
        import com.google.gson.Gson;
        import com.google.gson.reflect.TypeToken;

        import java.io.File;
        import java.io.FileNotFoundException;
        import java.io.FileReader;
        import java.io.FileWriter;
        import java.io.IOException;
        import java.lang.reflect.Type;
        import java.util.ArrayList;
        import java.util.Date;


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
    // File for saving date object for notifications
    private static final String FILENAME = "date1.sav";


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
                    selectedFragment = new BorrowBooksFragment();
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
        SignInUser();
        // Create the listener for children being added to firebase.Notifications
        createNotificationListener();



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
                int id = searchView.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
                TextView textView = (TextView) searchView.findViewById(id);
                textView.setTextColor(Color.CYAN);
                textView.setHintTextColor(Color.CYAN);
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
                    new BorrowBooksFragment()).commit();
        }

        //loadFragment(new myBooksFragment());
    }

    /**
     * Create an external notification object, and push it so that the viewer can see it
     * Only takes a string which is the message of the notification
     * @author : Randal Kimpinski
     * @param text message of the notification
     * @since March 3, 2019
     */
    private void pushNotification(String text) {
        // Create the notification
        String title = "Request";
        String content = text;

        // Create an explicit intent for an app Activity and add book as extra
        //TODO get this working
        //Intent intent = new Intent(this, BookProfileActivity.class);
        Intent intent = new Intent(this, NotificationActivity.class);
        //Bundle extras = new Bundle();
        //extras.putParcelable("Book Data", new Book());
        //intent.putExtras(extras);

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        // Create instance of notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_stat_name)
                .setContentTitle(title)
                .setContentText(content)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                // Set the intent that will fire when the user taps the notification
                .setContentIntent(pendingIntent)
                // Delete the notification when the user touches it
                .setAutoCancel(true);
        // Set context for notification
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        // Generate the notification, NOTIFICATION_ID is unique for each notification
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
    // refactored the name of the method
    private void SignInUser(){

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
     * @param v the view
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

    /**
     * If the SDK version is above a certain number, we need to create the notification channel
     * Conditional statements ensure that we are backwards compatible
     * @author : Android Tutorial at "https://developer.android.com/training/notify-user/build-notification#java"
     * @see MainActivity
     * @see Notification
     * @since March 3, 2019
     */
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

    /**
     * Get the last time the notification listener was run
     * This code is necessary so that not all Firebase notifications are shown when the
     * app is completely closed and reopened. Only Notifications after this date are "new"
     * @return Date object representing the last time the listener was activated
     * @author : Randal Kimpinski
     * @see MainActivity
     * @see Notification
     * @since March 3, 2019
     */
    private Date getLastNotificationDate() {
        try {
            FileReader in = new FileReader(new File(getFilesDir(),FILENAME));
            Gson gson = new Gson();
            Type listtype = new TypeToken<ArrayList<Date>>(){}.getType();
            ArrayList<Date> dates;
            dates = gson.fromJson(in, listtype);
            return dates.get(0);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            Date date = new Date();
            setLastNotificationDate(date);
            return date;
        }
    }

    /**
     * Set the last time the notification listener was run
     * This code is necessary so that not all Firebase notifications are shown when the
     * app is completely closed and reopened. Only Notifications after this date are "new"
     * @author : Randal Kimpinski
     * @see MainActivity
     * @see Notification
     * @since March 3, 2019
     */
    private void setLastNotificationDate(Date date) {
        try {
            FileWriter out = new FileWriter(new File(getFilesDir(),FILENAME));
            Gson gson = new Gson();
            ArrayList<Date> dates = new ArrayList<>();
            dates.add(date);
            gson.toJson(dates, out);
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates the listener for notifications in firebase
     * Anytime a firebase notification is created, this method is activated
     * If the notification is for the current user, we create the external notification
     * This function uses a file to track the last time it was called, so that no
     * old notifications seem valid and have external notifications created for them
     * @author : Randal Kimpinski
     * @see MainActivity
     * @see Notification
     * @since March 3, 2019
     */
    private void createNotificationListener() {
        // Firebase listener, activated when there is a change to notifications
        // Will be activated regardless of activity, or if the app is even running
        // Set up test firebase listener
        Firebase.setAndroidContext(this);
        ref = new Firebase(link);
        Firebase tempRef = ref.child("Notifications");
        // create a one time use listener to immediately access datasnapshot
        // Create a listener to access the database if an entry is added to "Notifications"
        tempRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d(TAG + " NotifcationEvent", "Notification listener activated");
                Log.d("snapshot values", dataSnapshot.getKey() + " " +
                        dataSnapshot.getValue().toString());
                String jNotification = dataSnapshot.getValue(String.class);
                if (jNotification != null) {
                    Log.d("jNotification", jNotification);
                    // Get notification object from Gson
                    Gson gson = new Gson();
                    Type tokenType = new TypeToken<Notification>() {
                    }.getType();
                    Notification notification = gson.fromJson(jNotification, tokenType);
                    // Get username from shared preferences
                    String username = getSharedPreferences("UserPreferences", Context.MODE_PRIVATE)
                            .getString("username", "TestUsername");
                    // Get when the listener was lasted called
                    Date lastNotificationDate = getLastNotificationDate();
                    // Update when listener was lasted called
                    setLastNotificationDate(new Date());
                    Log.d(TAG + " notificationDate", "File notification date is set to " + lastNotificationDate.toString());
                    // Check that the notification is for this user, and the it is a "new" notification
                    if (notification.getUserName().equals(username) && notification.getDate().after(lastNotificationDate)) {
                        Log.d(TAG + " validNotification", "Firebase contains a new notification with a valid username for this user");
                        // Create an external notification for the user
                        pushNotification(notification.getText());
                    }
                }
            }
            // Do nothing onChildChanged
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }
            // Do nothing onChildRemoved
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }
            // Do nothing onChildMoved
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }
            // Do nothing onCancelled
            @Override
            public void onCancelled(FirebaseError firebaseError) {
                return;
            }
        });
    }
}