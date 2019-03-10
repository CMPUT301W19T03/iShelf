package ca.ualberta.ishelf;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static junit.framework.TestCase.assertEquals;
import static org.hamcrest.Matchers.allOf;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class ViewProfileTest {

    @Test
    public void useAppContext() {
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("ca.ualberta.ishelf", appContext.getPackageName());
    }

    // Start viewProfileActivity without launching so we can add in item to our intent
    @Rule
    public ActivityTestRule<ViewProfileActivity> viewProfileActivityActivityTestRule =
            new ActivityTestRule<>(ViewProfileActivity.class, false, false);

    /**
     * 02.01.01
     * As an owner or borrower, I want a profile with a unique username and my contact information.
     */

    @Test
    public void DisplayProfile() throws Exception {
        User user = new User();
        String name = "Test Name";
        String phoneNum = "123-456-0000";
        String email = "test@email.com";
        user.setUsername(name);
        user.setPhoneNum(phoneNum);
        user.setEmail(email);
        Intent intent = new Intent();
        intent.putExtra("User", user);
        viewProfileActivityActivityTestRule.launchActivity(intent);

        // Check if Username is displayed
        onView(withId(R.id.tvUsername)).check(matches(isDisplayed()));
        // Check if Phone is displayed
        onView(withId(R.id.tvPhoneNum)).check(matches(isDisplayed()));
        // Check if Email is displayed
        onView(withId(R.id.tvEmail)).check(matches(isDisplayed()));
    }


/*
    @Rule
    public ActivityTestRule<MainActivity> mainActivityActivityTestRule =
            new ActivityTestRule<>(MainActivity.class);

    @Test
    public void DisplayUsername() throws Exception{
        // Check if Username is displayed
        onView(withId(R.id.tvUsername)).check(matches(isDisplayed()));
    }


    /*
    @Test
    public void clickProfileIcon_ShowsUserProfile() throws Exception{
        // Click on the User's profile icon in the App Bar
        onView(withId(R.id.profile_icon)).perform(click());

        // Check if the User's profile screen is displayed
        onView(withId(R.id.tvUsername)).check(matches(isDisplayed()));
    }
    */
}

/*
package ca.ualberta.ishelf;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */

/*
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("ca.ualberta.ishelf", appContext.getPackageName());
    }
}

 */