package ca.ualberta.ishelf;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.test.espresso.contrib.RecyclerViewActions;

import com.firebase.client.Firebase;
import com.google.gson.Gson;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.UUID;

import androidx.test.rule.ActivityTestRule;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;


@RunWith(androidx.test.runner.AndroidJUnit4.class)
public class ListOfRequestsTest {
    //@Rule
    //public ActivityTestRule<ListOfRequestsActivity> activityRule =
    //        new ActivityTestRule<ListOfRequestsActivity>(ListOfRequestsActivity.class);
    private final String link = "https://ishelf-bb4e7.firebaseio.com";
    private Firebase ref;
    private String username;

    @Rule
    // third parameter is set to false which means the activity is not started automatically
    public ActivityTestRule<ListOfRequestsActivity> mActivityRule =
            new ActivityTestRule<>(ListOfRequestsActivity.class, false, false);

    /**
     * setSharedPrefs sets the sharedPreferences of the app to the fake user before running the tests
     */
    @Before
    public void setSharedPrefs(){
        SharedPreferences sharedPreferences = getInstrumentation().getTargetContext().
                getSharedPreferences("UserPreferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        username = sharedPreferences.getString("username", "");
        editor.putString("username", "testUsername").apply();
        editor.commit();
    }
    /**
     * restoreSharedPrefs restores the Shared Preferences to the values that they were before
     * running the tests. This is performed after testing
     */
    @After
    public void restoreSharedPrefs(){
        SharedPreferences sharedPreferences = getInstrumentation().getTargetContext().
                getSharedPreferences("UserPreferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("username", username);
        editor.commit();
    }
    /**
     * View and then Accept a request
     */
    @Test
    public void acceptARequest(){
        // Set the shared preferences to what we want
        setSharedPrefs();
        // Create a request for a certain book id
        UUID id = UUID.randomUUID();
        Request request = new Request(id, "James");
        Book b1 = new Book();
        b1.setId(id);
        b1.setName("testName1");
        User u1 = new User();
        u1.setUsername("testUsername");
        u1.addRequest(request);

        // Add the request to firebase
        // Save user to Firebase
        Firebase userchild = ref.child("Users").child(u1.getUsername());
        Gson gson = new Gson();
        String jUser = gson.toJson(u1);
        // save the new User object to firebase
        userchild.setValue(jUser);

        // Save book to Firebase
        Firebase bookchild = ref.child("Books").child(b1.getId().toString());
        // Convert to Gson
        gson = new Gson();
        String jBook = gson.toJson(b1);
        // Save to firebase
        bookchild.setValue(jBook);

        // Start the intent with the extraString
        Intent i = new Intent();
        i.putExtra("myBookId", id.toString());
        mActivityRule.launchActivity(i);

        // Click accept button on first item, since we should only have the one
        //TODO These two lines should be uncommented, but I can't get RecyclerViewActions to work
        onView(withId(R.id.listOfRequestsRecycler)).perform(
              RecyclerViewActions.actionOnItemAtPosition(0, MyViewAction.clickChildViewWithId(R.id.AButton)));

        // Now check firebase to see if the reqeust has been accepted

        // restore shared preferences before ending
        restoreSharedPrefs();
    }

    /**
     * View and then Decline a request
     */
    @Test
    public void declineARequest(){
        // Set the shared preferences to what we want
        setSharedPrefs();
        // Create a request for a certain book id
        UUID id = UUID.randomUUID();
        Request request = new Request(id, "James");
        Book b1 = new Book();
        b1.setId(id);
        b1.setName("testName1");
        User u1 = new User();
        u1.setUsername("testUsername");
        u1.addRequest(request);

        // Add the request to firebase
        // Save user to Firebase
        Firebase userchild = ref.child("Users").child(u1.getUsername());
        Gson gson = new Gson();
        String jUser = gson.toJson(u1);
        // save the new User object to firebase
        userchild.setValue(jUser);

        // Save book to Firebase
        Firebase bookchild = ref.child("Books").child(b1.getId().toString());
        // Convert to Gson
        gson = new Gson();
        String jBook = gson.toJson(b1);
        // Save to firebase
        bookchild.setValue(jBook);

        // Start the intent with the extraString
        Intent i = new Intent();
        i.putExtra("myBookId", id.toString());
        mActivityRule.launchActivity(i);

        // Click accept button on first item, since we should only have the one
        //TODO These two lines should be uncommented, but I can't get RecyclerViewActions to work
        //onView(withId(R.id.listOfRequestsRecycler)).perform(
        //      RecyclerViewActions.actionOnItemAtPosition(0, MyViewAction.clickChildViewWithId(R.id.DButton)));

        // Now check firebase to see if the reqeust has been accepted

        // restore shared preferences before ending
        restoreSharedPrefs();
    }

    /**
     * View all requests
     * Just ensure that the page can be opened without errors
     */
    @Test
    public void viewAllRequests() {
        // Do nothing, just make sure the activity opens
        // And that there are no errors when using the basics of this activity
        // Start the intent with the extraString
        Intent i = new Intent();
        mActivityRule.launchActivity(i);
    }

}
