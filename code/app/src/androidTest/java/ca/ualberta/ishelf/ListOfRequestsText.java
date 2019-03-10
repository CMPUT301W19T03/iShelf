package ca.ualberta.ishelf;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;

@RunWith(androidx.test.runner.AndroidJUnit4.class)
public class ListOfRequestsText {
    @Rule
    public ActivityTestRule<ListOfRequestsActivity> activityRule =
            new ActivityTestRule<ListOfRequestsActivity>(ListOfRequestsActivity.class);

    /**
     * Verify that the data from tweet is correct and visible in new acitivity
     */
    @Test
    public void addSomeEntries(){
    }

}
