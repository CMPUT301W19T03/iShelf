package ca.ualberta.ishelf;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.view.View;

import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
import com.firebase.client.Firebase;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileWriter;
import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Database object used as an interface between our applications and our Firebase
 * Real-Time database
 * Important Note: Any method that gets an object from the firebase will not work
 * due to the nature of threads. Copy and alter code, inserting into the actual activity.
 * @author : Randal Kimpinski
 */
public class Database extends Application {
    private final String link = "https://ishelf-bb4e7.firebaseio.com";
    private Firebase ref;

    /**
     * Initialize database using current context
     * Database db = new Database(this)
     * @author : Randal Kimpinski
     */
    Database(Context context){
        Firebase.setAndroidContext(context);
        ref = new Firebase(link);
    }

    /**
     * Used to change the context of our firebase
     * @param context
     * @return
     */
    public Firebase connect(Context context){
        Firebase ref;
        Firebase.setAndroidContext(context);
        ref = new Firebase(link);
        return ref;
    }

    /**
     * Given a user object, adds that object to firebase
     * Uses Gson to store the object
     * @param user
     * @author : Randal Kimpinski
     */
    public void addUser(User user) {
        // Save user to Firebase
        Firebase userchild = ref.child("Users").child(user.getUsername());
        Gson gson = new Gson();
        String jUser = gson.toJson(user);
        // save the new User object to firebase
        userchild.setValue(jUser);
    }

    /**
     * Given a username, finds that object in our database and deletes it
     * @param username
     * @author : Randal Kimpinski
     */
    public void deleteUser(String username) {
        // get reference to specific entry
        Log.d("Delete","Here");
        ref.child("Users").child(username).removeValue();
    }

    /**
     * Given a user object, finds that object in the database and replaces it
     * @param user
     * @author : Randal Kimpinski
     */
    public void editUser(User user) {
        deleteUser(user.getUsername());
        addUser(user);
    }

    /**
     * Given a user object, and it's original username, finds the old User object in the
     * firebase real-time database and replaces it with the new user.
     * @param oldUsername
     * @param user
     * @author : Randal Kimpinski
     */
    public void editUser(String oldUsername, User user) {
        deleteUser(oldUsername);
        addUser(user);
    }


    /**
     * Given a book object, adds that object to the firebase real-time database
     * @param book
     * @author : Randal Kimpinski
     */
    public void addBook(Book book) {
        // Save book to Firebase
        Firebase bookchild = ref.child("Books").child(book.getId().toString());
        // Convert to Gson
        Gson gson = new Gson();
        String jBook = gson.toJson(book);
        // Save to firebase
        bookchild.setValue(jBook);
    }

    /**
     * Given the UUID of a book, finds that book in the Firebase real-time database
     * and removes it
     * @param id
     * @author : Randal Kimpinski
     */
    public void deleteBook(UUID id) {
        // get reference to specific entry
        ref.child("Books").child(id.toString()).removeValue();
    }


    /**
     * Given a book object, finds that object in the database and replaces it
     * @param book
     * @author : Randal Kimpinski
     */
    public void editBook(Book book) {
        deleteBook(book.getId());
        addBook(book);
    }


    /**
     * Given a Request object, adds that object to firebase
     * Uses Gson to store the object
     * @param request
     * @author : Randal Kimpinski
     */
    public void addRequest(Request request) {
        // Save user to Firebase
        Firebase userchild = ref.child("Requests").child(request.getId().toString());
        Gson gson = new Gson();
        String jRequest = gson.toJson(request);
        // save the new User object to firebase
        userchild.setValue(jRequest);
    }

    /**
     * Given a Request id, finds that object in our database and deletes it
     * @param requestId
     * @author : Randal Kimpinski
     */
    public void deleteRequest(String requestId) {
        // get reference to specific entry
        Log.d("Delete","Here");
        ref.child("Requests").child(requestId).removeValue();
    }

    /**
     * Given a user object, finds that object in the database and replaces it
     * @param request
     * @author : Randal Kimpinski
     */
    public void editUser(Request request) {
        deleteRequest(request.getId().toString());
        addRequest(request);
    }
    /**
     * Given a notification object, adds that object to the firebase realt-time database
     * @param notification
     * @author : Randal Kimpinski
     */
    public void addNotification(Notification notification) {
        // Save notification to Firebase
        Firebase notificationChild = ref.child("Notifications").child(notification.getId().toString());
        // Convert to Gson
        Gson gson = new Gson();
        String jNotification = gson.toJson(notification);
        // Save to firebase
        notificationChild.setValue(jNotification);
    }

    /**
     * Given the id of a notification, finds that obejct in the firebase database and deletes it
     * @param id
     * @author : Randal Kimpinski
     */
    public void deleteNotification(UUID id) {
        // get reference to specific entry
        ref.child("Notifications").child(id.toString()).removeValue();
    }

    /**
     * Given a notification, finds that notification in the firebase real-time database
     * and replaces it with our new notification
     * @param notification
     * @author : Randal Kimpinski
     */
    public void editNotification(Notification notification) {
        deleteNotification(notification.getId());
        addNotification(notification);
    }


}
