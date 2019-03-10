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
 */
public class Database extends Application {
    private final String link = "https://ishelf-bb4e7.firebaseio.com";
    private Firebase ref;

    //TODO documentation
    //TODO use arrays to actually return objects
    Database(Context context){
        Firebase.setAndroidContext(context);
        ref = new Firebase(link);
    }

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
     */
    public void deleteUser(String username) {
        // get reference to specific entry
        Log.d("Delete","Here");
        ref.child("Users").child(username).removeValue();
    }

    /**
     * Given a user object, finds that object in the database and replaces it
     * @param user
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
     */
    public void editUser(String oldUsername, User user) {
        deleteUser(oldUsername);
        addUser(user);
    }


    /**
     * Given a book object, adds that object to the firebase real-time database
     * @param book
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
     */
    public void deleteBook(UUID id) {
        // get reference to specific entry
        ref.child("Books").child(id.toString()).removeValue();
    }


    /**
     * Given a book object, finds that object in the database and replaces it
     * @param book
     */
    public void editBook(Book book) {
        deleteBook(book.getId());
        addBook(book);
    }



    /**
     * Given a request object, adds that object to the firebase real-time database
     * @author rmnattas
     * @param bookOwnerUsername username to edit the request in
     * @param request request object to edit
     */
    public void addRequest(final String bookOwnerUsername, final Request request) {
        // get the user object from firebase
        Firebase tempRef = ref.child("Users").child(bookOwnerUsername);
        tempRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String jUser = dataSnapshot.getValue(String.class);
                Log.d("jUser", jUser);
                if (jUser != null) {
                    // Get user object from Gson
                    Gson gson = new Gson();
                    Type tokenType = new TypeToken<User>() {
                    }.getType();
                    User user = gson.fromJson(jUser, tokenType); // here is where we get the user object
                    user.addRequest(request);
                    editUser(bookOwnerUsername, user);
                } else {
                    Log.d("DATABASE", "add notification");
                }
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {
                return;
            }
        });
    }

    /**
     * Given the UUID of a request, finds that request in the Firebase real-time database
     * and removes it
     * @param bookOwnerUsername username to edit the request in
     * @param request request object to edit
     * @author rmnattas
     */
    public void deleteRequest(final String bookOwnerUsername, final Request request) {
        // get the user object from firebase
        Firebase tempRef = ref.child("Users").child(bookOwnerUsername);
        tempRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String jUser = dataSnapshot.getValue(String.class);
                Log.d("jUser", jUser);
                if (jUser != null) {
                    // Get user object from Gson
                    Gson gson = new Gson();
                    Type tokenType = new TypeToken<User>() {
                    }.getType();
                    User user = gson.fromJson(jUser, tokenType); // here is where we get the user object
                    user.deleteRequest(request);
                    editUser(bookOwnerUsername, user);
                } else {
                    Log.d("DATABASE", "add notification");
                }
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {
                return;
            }
        });
    }


    /**
     * Given a book object, finds that object in the database and replaces it
     * @author rmnattas
     * @param bookOwnerUsername username to edit the request in
     * @param request request object to edit
     */
    public void editRequest(final String bookOwnerUsername, final Request request) {
        deleteRequest(bookOwnerUsername, request);
        addRequest(bookOwnerUsername, request);
    }



    /**
     * Given a notification object, adds that object to the firebase realt-time database
     * @param notification
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
     */
    public void deleteNotification(UUID id) {
        // get reference to specific entry
        ref.child("Notifications").child(id.toString()).removeValue();
    }

    /**
     * Given a notification, finds that notification in the firebase real-time database
     * and replaces it with our new notification
     * @param notification
     */
    public void editNotification(Notification notification) {
        deleteNotification(notification.getId());
        addNotification(notification);
    }


}
