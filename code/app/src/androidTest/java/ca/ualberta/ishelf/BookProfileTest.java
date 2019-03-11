package ca.ualberta.ishelf;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
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
import static org.hamcrest.Matchers.allOf;

/**
 *
 * Send in either:
 *                  key: "Book" - a Book object
 *
 * .
 *
 *This is the Intent test for the BookProfileActivity
 *
 *
 * @author: Mehrab
 */

@RunWith(AndroidJUnit4.class)
@LargeTest
public class BookProfileTest {

    @Rule
    public ActivityTestRule<BookProfileActivity> BookProfileActivityActivityTestRule =
            new ActivityTestRule<BookProfileActivity>(BookProfileActivity.class, false, false);
// {
//                @Override
//                protected Intent getActivityIntent() {
//                    Intent intent = new Intent(InstrumentationRegistry.getContext(),BookProfileActivity.class);
//                    intent.putExtra("Key","Value");
//                    return intent;
//                }
//            };


    @Test
    public void DisplayBookInfo() throws Exception{
        Book book = new Book("Book 3", "Description", 1234L, "Year", "Genre", "author", false);
        book.setOwner("abcdef");
        // Check if Title is displayed
        Intent intent= new Intent();
        intent.putExtra("Book Data", book);

        BookProfileActivityActivityTestRule.launchActivity(intent);
        onView(withId(R.id.Title)).check(matches(isDisplayed()));


        onView(withId(R.id.Author)).check(matches(isDisplayed()));
//        onView(withId(R.id.Author)).perform(clearText(),typeText("Amr"));

        onView(withId(R.id.ISBN)).check(matches(isDisplayed()));
//        onView(withId(R.id.ISBN)).perform(clearText(),typeText("Amr"));

        onView(withId(R.id.des)).check(matches(isDisplayed()));


        onView(withId(R.id.Year)).check(matches(isDisplayed()));
//        onView(withId(R.id.Year)).perform(clearText(),typeText("Amr"));

        onView(withId(R.id.genre)).check(matches(isDisplayed()));
//        onView(withId(R.id.genre)).perform(clearText(),typeText("Amr"));

        onView(withId(R.id.ownerUsername)).check(matches(isDisplayed()));
//        onView(withId(R.id.ownerUsername)).perform(clearText(),typeText("Amr"));
    }



}


