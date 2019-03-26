package ca.ualberta.ishelf_lab9;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static junit.framework.TestCase.assertEquals;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;

/**
 * @author Jeremy
 * works for jeremy but not for the rest of group
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class ViewProfileTest {
    private String username;
    private String testUsername = "Charles Barkley";

    // Start viewProfileActivity without launching so we can add in item to our intent
    @Rule
    public ActivityTestRule<ViewProfileActivity> viewProfileActivityActivityTestRule =
            new ActivityTestRule<>(ViewProfileActivity.class, false, false);

    @Rule
    public ActivityTestRule<EditProfileActivity> editProfileActivityActivityTestRule =
            new ActivityTestRule<>(EditProfileActivity.class, false, false);

    @Rule
    public ActivityTestRule<MainActivity> mainActivityActivityTestRule =
            new ActivityTestRule<>(MainActivity.class, false, false);

    @Rule
    public ActivityTestRule<BookProfileActivity> bookProfileActivityActivityTestRule =
            new ActivityTestRule<>(BookProfileActivity.class, false, false);


    /**
     * setSharedPrefs sets the sharedPreferences of the app to the fake user before running the tests
     */
    @Before
    public void setSharedPrefs(){
        SharedPreferences sharedPreferences = getInstrumentation().getTargetContext().getSharedPreferences("UserPreferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        username = sharedPreferences.getString("username", "");
        editor.putString("username", testUsername).apply();
        editor.commit();
    }

    /**
     * restoreSharedPrefs restores the Shared Preferences to the values that they were before
     * running the tests. This is performed after testing
     */
    @After
    public void restoreSharedPrefs(){
        SharedPreferences sharedPreferences = getInstrumentation().getTargetContext().getSharedPreferences("UserPreferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("username", username);
        editor.commit();
    }


    /**
     * 02.01.01
     * As an owner or borrower, I want a profile with a unique username and my contact information.
     * 02.02.01
     * As an owner or borrower, I want to edit the contact information in my profile.
     */
    @Test
    public void DisplayProfile() {
        // Need to create an account on Firebase and also save username to Shared Preferences
        // Easiest way is to go through the steps of registering a new account
        // This will sign the current user out by overwriting the shared preferences

        String phoneNum = "123-456-0000";
        String email = "test@email.com";

        Intent intent = new Intent();
        intent.putExtra("Registering", true);
        //intent.putExtra("User", user);
        editProfileActivityActivityTestRule.launchActivity(intent);

        // 02.02.01- Edit our information
        // Enter our Test user information
        onView(withId(R.id.editName)).perform(typeText(testUsername));
        onView(withId(R.id.editEmail)).perform(typeText(email));
        onView(withId(R.id.editPhone)).perform(typeText(phoneNum));

        // Close the SoftKeyboard so we can click on Save
        onView(withId(R.id.saveButton)).perform(closeSoftKeyboard());

        // Click Save and save the User
        onView(withId(R.id.saveButton)).perform(click());


        // Navigate to view our signed-in profile via the AppBar profile icon button
        onView(withId(R.id.profile_icon)).perform(click());

        // 02.01.01 - Have a profile with our information
        // Check if Username is displayed and the same as entered earlier
        onView(withId(R.id.tvUsername)).check(matches(withText(testUsername)));

        // Check if Phone is displayed and the same as entered earlier
        onView(withId(R.id.tvPhoneNum)).check(matches(withText(containsString(phoneNum))));

        // Check if Email is displayed and the same as entered earlier
        onView(withId(R.id.tvEmail)).check(matches(withText(containsString(email))));


        // Check if the edit button appears for our profile
        onView(withId(R.id.editProfileButton)).check(matches(isDisplayed()));
        //onView(withId(R.id.editProfileButton)).perform(click());
    }

    /**
     * 02.03.01
     * As an owner or borrower, I want to retrieve and show the profile of a presented username.
     */
    @Test
    public void viewProfileOfUsername(){
        Intent intent = new Intent();
        mainActivityActivityTestRule.launchActivity(intent);
        onView(withId(R.id.borrow_books)).perform(click());
        // Click on the second book
        onView(withId(R.id.borrow_recycler)).perform(RecyclerViewActions.actionOnItemAtPosition(2, click()));

        // Check and see if a username is presented
        // Click on that username
        onView(withId(R.id.ownerUsername))
                .check(matches(isDisplayed()))
                .perform(click());

        // Check and see if clicking on the username takes us to the View Profile activity
        onView(withId(R.id.tvUsername)).check(matches(isDisplayed()));
    }


    /**
     * 10.04.01
     * As a user, I want to be able to see an owners rating when viewing their profile.
     * 10.06.01
     * As a user, I want to be able to see a borrowers rating when viewing their profile.
     * 10.09.01
     * As an owner, I want to be able to see borrower ratings when selecting a borrower to lend a book to.
     */
    @Test
    public void seeRating(){
        // View a profile and check and see if Rating Bar is implemented
        // Actual ratings have not yet been implemented so we are not checking
        // how many stars are displayed yet
        Intent intent = new Intent();
        viewProfileActivityActivityTestRule.launchActivity(intent);
        // See if Rating Bar is visible
        onView(withId(R.id.ratingBar)).check(matches(isDisplayed()));
    }

}