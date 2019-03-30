package ca.ualberta.ishelf;

import android.app.Instrumentation;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.RecyclerView;
import android.widget.AutoCompleteTextView;
import android.widget.FrameLayout;
import android.widget.SearchView;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;


import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import android.support.test.espresso.contrib.RecyclerViewActions;

import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.espresso.core.internal.deps.dagger.internal.Preconditions.checkNotNull;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.not;

import ca.ualberta.ishelf.RecyclerAdapters.BorrowFragment;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 *
 * tests borrow fragment
 * @author Evan
 */
@RunWith(AndroidJUnit4.class)
@LargeTest

public class BorrowFragmentTest {
    @Rule
    public ActivityTestRule<MainActivity> BorrowFragmentTestRule =
            new ActivityTestRule<>(MainActivity.class, false, false);

    private MainActivity activity = null;

    @Before
    public void start() throws Exception{
        Intent intent = new Intent();
        activity = (MainActivity) BorrowFragmentTestRule.launchActivity(intent);
    }

    @Test
    public void searchTest() {
        FrameLayout fragmentContainer = (FrameLayout) activity.findViewById(R.id.fragment_container);

        BorrowFragment fragment = new BorrowFragment();

        activity.getSupportFragmentManager().beginTransaction()
                .replace(fragmentContainer.getId(), fragment)
                .commit();

        Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();
        instrumentation.waitForIdleSync();

        SearchView search = activity.findViewById(R.id.searchView1);
        RecyclerView view = fragment.getView().findViewById(R.id.borrow_recycler);

        onView(withId(R.id.searchView1)).perform(click());
        onView(isAssignableFrom(AutoCompleteTextView.class)).perform(typeText("H"));

        String input = search.getQuery().toString();

        onView(withId(R.id.borrow_recycler)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withId(R.id.Title)).check(matches(withText(containsString(input))));
    }
}




