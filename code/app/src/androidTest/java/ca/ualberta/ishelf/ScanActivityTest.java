package ca.ualberta.ishelf;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.containsString;



/**
 * @author Faisal
 *
 */

@RunWith(AndroidJUnit4.class)
@LargeTest
public class ScanActivityTest {

    @Rule
    public ActivityTestRule<ScanActivity> scanActivityTestRule =
            new ActivityTestRule<>(ScanActivity.class, false, false);
    @Rule
    public ActivityTestRule<EditBookActivity> editBookActivityTestRule =
            new ActivityTestRule<>(EditBookActivity.class, false, false);


    @Test
    /**
     * US 01.02.01:
     * As an owner, I want the book description by scanning it off the book (at least the ISBN)
     */
    public void getISBN() {
        Bundle extras = new Bundle();
        extras.putString("task", "get_book_info");
        Intent intentScan = new Intent();
        intentScan.putExtras(extras);
        scanActivityTestRule.launchActivity(intentScan);
        ScanActivity scanActivity = scanActivityTestRule.getActivity();
        scanActivity.testing = true;

        // click the scan fab
        onView(withId(R.id.scan_fab)).perform(click());

        onView(withId(R.id.last_ISBN)).check(matches(withText("9780307401199")));
        scanActivity.testing = false;
    }
}

