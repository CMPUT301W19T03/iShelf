package ca.ualberta.ishelf;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.UUID;

import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;

@RunWith(androidx.test.runner.AndroidJUnit4.class)
public class ListOfRequestsTest {
    @Rule
    public ActivityTestRule<ListOfRequestsActivity> activityRule =
            new ActivityTestRule<ListOfRequestsActivity>(ListOfRequestsActivity.class);


    /**
     * View and then Accept a request
     */
    @Test
    public void acceptARequest(){
        User u1 = new User();
        u1.setUsername("testUsername1");
        User t1 = new User();
        u1.setUsername("testReq1");
        User t2 = new User();
        u1.setUsername("testReqr2");
        Book b1 = new Book();
        b1.setName("bName2");
        Book b2 = new Book();
        b2.setName("bName2");

        u1.addRequest(new Request(b1.getId(), "testReq1"));
        u1.addRequest(new Request(b2.getId(), "testReq2"));
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
