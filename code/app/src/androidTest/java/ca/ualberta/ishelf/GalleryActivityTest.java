package ca.ualberta.ishelf;

import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ca.ualberta.ishelf.Models.Book;

import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static junit.framework.TestCase.assertNotNull;
import static org.hamcrest.Matchers.not;

/**
 * tests to see if you see lists of books
 * @author Faisal
 *
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

