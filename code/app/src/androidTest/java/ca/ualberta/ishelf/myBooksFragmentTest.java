package ca.ualberta.ishelf;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ca.ualberta.ishelf.RecyclerAdapters.myBooksFragment;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertEquals;

/**
 *  * ignore this test, myBooksFragment is going ot be changed to match the borrowBooks view that Faisal made so this intent test should match his intent test
 * @author Evan
 * ignore this test, myBooksFragment is going ot be changed to match the borrowBooks view that Faisal made so this intent test should match his intent test
 */
@RunWith(AndroidJUnit4.class)

public class myBooksFragmentTest {
    @Rule
    public ActivityTestRule activityActivityTestRule
            = new ActivityTestRule(myBooksFragment.class);
    @Before
    public void init(){
        activityActivityTestRule.getActivity();//getSupportFragmentManager().beginTransaction();
    }
    @Test
    public void TestAutoComplete(){
        onView(withId(R.id.my_books)).perform(click()).check(matches((isDisplayed())));
    }
}

