package ca.ualberta.ishelf_lab9;

import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.view.View;

import org.hamcrest.Matcher;

/**
 * answered on May 20 '15 at 1:14
 * by anonymous user blade
 * at https://stackoverflow.com/questions/28476507/using-espresso-to-click-view-inside-recyclerview-item
 * This class allows one to click on a button within an entry in a recyclerView
 */
public class MyViewAction {

    public static ViewAction clickChildViewWithId(final int id) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return null;
            }

            @Override
            public String getDescription() {
                return "Click on a child view with specified id.";
            }

            @Override
            public void perform(UiController uiController, View view) {
                View v = view.findViewById(id);
                v.performClick();
            }
        };
    }
}
