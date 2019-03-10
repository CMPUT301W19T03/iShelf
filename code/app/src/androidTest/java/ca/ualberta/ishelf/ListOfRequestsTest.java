package ca.ualberta.ishelf;

import android.content.Intent;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.UUID;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static java.util.regex.Pattern.matches;

@RunWith(androidx.test.runner.AndroidJUnit4.class)
public class ListOfRequestsTest {
    //@Rule
    //public ActivityTestRule<ListOfRequestsActivity> activityRule =
    //        new ActivityTestRule<ListOfRequestsActivity>(ListOfRequestsActivity.class);

    @Rule
    // third parameter is set to false which means the activity is not started automatically
    public ActivityTestRule<ListOfRequestsActivity> mActivityRule =
            new ActivityTestRule<>(ListOfRequestsActivity.class, false, false);

    /**
     * View and then Accept a request
     */
    @Test
    public void acceptARequest(){
        // Create a request for a certain book id
        UUID id = UUID.randomUUID();
        Request request = new Request(id, "James");
        Book b1 = new Book();
        b1.setId(id);
        b1.setName("testName1");
        // Start the intent with the extraString
        Intent i = new Intent();
        i.putExtra("myBookId", id.toString());
        mActivityRule.launchActivity(i);
        //onView(withRecyclerView(R.id.recyclerView)
        //.atPositionOnView(0), R.id.textView);
        //onView(withId(R.id.recyclerView))
        //      .check(matches(atPosition(0, withText("Test Text"))));
        //onView(withId(R.id.recyclerView)) .check(matches(atPosition(0, hasDescendant(withText("James")))));
        //View v = view.findViewById(id);
        //v.performClick();
        /*
        onView(withId(R.id.listOfRequestsRecycler)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        .check(matches(atPosition(0, hasDescendent())));
        onView(withId(R.id.scroll_view))
                .perform(RecyclerViewActions.actionOnItemAtPosition(3, click()));
                */

    }

    /**
     * View and then Decline a request
     */
    @Test
    public void declineARequest(){
        // Open activity
        // Add test stuff
        // What am I asserting?
        /*
        onData(anything()).inAdapterView(withId(R.AButton))
        onView(withId(R.))
        String message = "test5328";
        // Click clear
        onView(withId(R.id.clear)).perform(click());
        // insert text "test5328" into .body
        onView(withId(R.id.body)).perform(typeText(message));
        // Click .save
        onView(withId(R.id.save)).perform(click());
        // Click on tweet in position listView
        onData(anything()).inAdapterView(withId(R.id.oldTweetsList)).atPosition(0).perform(click());
        //onView(withText(message)).perform(click());
        // Assert text in .tweetView == test5328
        onView(withId(R.id.tweetView)).check(matches(withText(message)));
        */

    }

    /**
     * View all requests
     * Just ensure that the page can be opened without errors
     */
    @Test
    public void viewAllRequests() {
        // Do nothing, just make sure the activity opens
    }

}
