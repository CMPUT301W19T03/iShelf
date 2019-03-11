package ca.ualberta.ishelf;

import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertEquals;

/**
 *  * notifications unit test
 * @author Evan
 * @edited Evan
 * notifications unit test
 */
public class NotificationTest {

        @Test
        public void testGetDate(){
            Date testDate = new Date();
            Notification notification = new Notification(testDate, "testNotification", "testUserName");
            assertEquals(testDate, notification.getDate());
        }

        @Test
        public void testSetDate(){
            Date testDate = new Date();
            Notification notification = new Notification(testDate, "testNotification", "testUserName");
            Date testDateNew = new Date();
            notification.setDate(testDateNew);
            assertEquals(testDateNew, notification.getDate());
        }

        @Test
        public void testGetText(){
            Notification notification = new Notification(new Date(), "testNotification", "testUserName");
            assertEquals("testNotification", notification.getText());
        }

        @Test
        public void testSetText(){
            Notification notification = new Notification(new Date(), "testNotification", "testUserName");
            notification.setText("newText");
            assertEquals("newText", notification.getText());
        }

        @Test
        public void testGetUserName(){
            Notification notification = new Notification(new Date(), "testNotification", "testUserName");
            assertEquals("testUserName", notification.getUserName());
        }

        @Test
        public void testSetUserName(){
            Notification notification = new Notification(new Date(), "testNotification", "testUserName");
            notification.setUserName("newUserName");
            assertEquals("newUserName", notification.getUserName());
        }

}
