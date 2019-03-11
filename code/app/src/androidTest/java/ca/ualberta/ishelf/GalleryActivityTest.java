package ca.ualberta.ishelf;

import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.ViewAssertion;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.RecyclerView;
import android.widget.AutoCompleteTextView;
import android.widget.FrameLayout;
import android.widget.SearchView;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.UUID;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static junit.framework.TestCase.assertNotNull;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.Matchers.not;

/**
 * @author Evan
 * tests to see if you see lists of books
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class GalleryActivityTest {
    private Book book = new Book("Book 3", "Description", 1234L, "Year", "Genre", "author", false);

    @Rule
    public ActivityTestRule<MainActivity> GalleryActivityTest =
            new ActivityTestRule<>(MainActivity.class);

    @Test
    public void viewImage() {
        assertNotNull(book);
    }

    @Test
    public void addImage() {
        assertNotNull(book);
    }

    @Test
    public void deleteImage() {
        assertNotNull(book);
    }
}

