package ca.ualberta.ishelf_lab9;


import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.content.Intent;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

/**
 *
 * Send in either:
 *                  key: "Book" - a Book object
 *
 * .
 *
 *This is the Intent test for the EditBookActivity
 *
 *
 * @author : Mehrab
 */


@RunWith(AndroidJUnit4.class)
@LargeTest
public class EditBookTest {
    @Rule
    public ActivityTestRule<EditBookActivity> EditBookActivityActivityTestRule =
            new ActivityTestRule<EditBookActivity>(EditBookActivity.class, false, false);



    @Test
    public void EditBook() throws Exception{

        Intent intent= new Intent();

        EditBookActivityActivityTestRule.launchActivity(intent);

        onView(withId(R.id.editTitle)).perform(clearText(),typeText("HP"));
        onView(withId(R.id.editTitle)).check(matches(isDisplayed()));

        onView(withId(R.id.editAuthor)).perform(clearText(),typeText("Mehrab"));
        onView(withId(R.id.editAuthor)).check(matches(isDisplayed()));

        onView(withId(R.id.editISBN)).perform(clearText(),typeText("123454"));
        onView(withId(R.id.editISBN)).check(matches(isDisplayed()));


        onView(withId(R.id.editYear)).perform(clearText(),typeText("1998"));
        onView(withId(R.id.editYear)).check(matches(isDisplayed()));
//
        onView(withId(R.id.editGenre)).perform(clearText(),typeText("Fantasy"));
        onView(withId(R.id.editGenre)).check(matches(isDisplayed()));

        onView(withId(R.id.editDes)).perform(clearText(),typeText("kdasdkjaldask"));
        onView(withId(R.id.editDes)).check(matches(isDisplayed()));

    }

}
