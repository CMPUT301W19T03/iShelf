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
     * Given a username, finds and returns that object from Firebase
     * @param username
     * @return
     * @deprecated
     */
//    public User getUser(String username) {
//        // get reference to specific entry
//        Firebase tempRef = ref.child("Users").child(username);
//        final ArrayList<User> userList = new ArrayList<User>();
//        Log.d("SizeBefore ", String.valueOf(userList.size()));
//        // create a one time use listener to immediately access datasnapshot
//        tempRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                String jUser = dataSnapshot.getValue(String.class);
//                Log.d("jUser", jUser);
//                if (jUser != null) {
//                    // Get user object from Gson
//                    Gson gson = new Gson();
//                    Type tokenType = new TypeToken<User>(){}.getType();
//                    User user = gson.fromJson(jUser, tokenType);
//                    Log.d("Confirm", user.getUsername());
//                    userList.add(user);
//                } else {
//                    Log.d("FBerror1", "User doesn't exist or string is empty");
//                }
//                Log.d("Size", String.valueOf(userList.size()));
//            }
//
//            @Override
//            public void onCancelled(FirebaseError firebaseError) {
//                return;
//            }
//        });
//        // If User doesn't exist, or string is empty, return null
//        Log.d("next", "this is where code jumps to next");
//        Log.d("SizeAfter", String.valueOf(userList.size()));
//        try {
//            //Log.d("SizeLoop", String.valueOf(userList.size()));
//            return userList.get(0);
//        } catch (Exception e) {
//            Log.d("FBerror", "No user exists in firebase with that username");
//        }
//        return null;
//    }

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


//    /**
//     * Given a request object, adds that object to the firebase real-time database
//     * @author rmnattas
//     * @param request request to add to firebase
//     */
//    public void addRequest(Request request) {
//        // Save request to Firebase
//        Firebase requestChild = ref.child("Requests").child(request.getId().toString());
//        // Convert to Gson
//        Gson gson = new Gson();
//        String jRequest = gson.toJson(request);
//        // Save to firebase
//        requestChild.setValue(jRequest);
//    }
//
//    /**
//     * Given the UUID of a request, finds that request in the Firebase real-time database
//     * and removes it
//     * @author rmnattas
//     * @param id request id
//     */
//    public void deleteRequest(String id) {
//        // get reference to specific entry
//        ref.child("Requests").child(id).removeValue();
//    }
//
//
//    /**
//     * Given a book object, finds that object in the database and replaces it
//     * @author rmnattas
//     * @param request request object to edit
//     */
//    public void editRequest(Request request) {
//        deleteRequest(request.getId().toString());
//        addRequest(request);
//    }


    /**
     * Given the Id of a book, finds that book in the database
     * @param bookId
     * @return
     * @deprecated
     */
//    public Book getBook(UUID bookId) {
//        // get reference to specific entry
//        Firebase tempRef = ref.child("Books").child(bookId.toString());
//        final ArrayList<Book> bookList = new ArrayList<Book>();
//        // create a one time use listener to immediately access datasnapshot
//        tempRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                String jBook = dataSnapshot.getValue(String.class);
//                if (jBook != null) {
//                    // Get user object from Gson
//                    Gson gson = new Gson();
//                    Type tokenType = new TypeToken<Book>(){}.getType();
//                    Book book = gson.fromJson(jBook, tokenType);
//                    bookList.add(book);
//                    //TODO how to return this book object?
//                    //Log.d("Confirm", book.getId());
//                } else {
//                    Log.d("Firebase Retrieval Error", "User doesn't exist or string is empty");
//                }
//            }
//
//            @Override
//            public void onCancelled(FirebaseError firebaseError) {
//                return;
//            }
//        });
//        // If Book doesn't exist, or string is empty, return null
//        return bookList.get(0);
//    }


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
//    public void editNotification(Notification notification) {
//        deleteNotification(notification.getId());
//        addNotification(notification);
//    }

    /**
     * Given the id of a notification, finds that notification in the firebase real-time database
     * and returns it
     * @param id
     * @return
     * @deprecated
     */
//    public Notification getNotification(UUID id){
//        // get reference to specific entry
//        Firebase tempRef = ref.child("Notifications").child(id.toString());
//        final ArrayList<Notification> notificationList = new ArrayList<Notification>();
//        // create a one time use listener to immediately access datasnapshot
//        tempRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                String jNotification = dataSnapshot.getValue(String.class);
//                if (jNotification != null) {
//                    // Get user object from Gson
//                    Gson gson = new Gson();
//                    Type tokenType = new TypeToken<Book>(){}.getType();
//                    Notification notification = gson.fromJson(jNotification, tokenType);
//                    notificationList.add(notification);
//                    //TODO how to return this book object?
//                    //Log.d("Confirm", book.getId());
//                } else {
//                    Log.d("Firebase Retrieval Error", "Notification doesn't exist or string is empty");
//                }
//            }
//
//            @Override
//            public void onCancelled(FirebaseError firebaseError) {
//                return;
//            }
//        });
//        // If Notification doesn't exist, or string is empty, return null
//        return notificationList.get(0);
//    }

    /**
     * Returns an ArrayList of all notifications
     * @return
     * @deprecated
     */
//    public ArrayList<Notification> getNotifications() {
//        // get reference to specific entry
//        Firebase tempRef = ref.child("Notifications");
//        final ArrayList<Notification> notificationList = new ArrayList<Notification>();
//        // create a one time use listener to immediately access datasnapshot
//        tempRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                Type tokenType = new TypeToken<Book>(){}.getType();
//                Notification tempNotification;
//                String tempString;
//
//                for(DataSnapshot d: dataSnapshot.getChildren()) {
//                    tempString = d.getValue(String.class);
//                    if (tempString != null) {
//                        // Get user object from Gson
//                        Gson gson = new Gson();
//                        tempNotification = gson.fromJson(tempString, tokenType);
//                        notificationList.add(tempNotification);
//                    } else {
//                        Log.d("Firebase Retrieval Error", "Notification doesn't exist or string is empty");
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(FirebaseError firebaseError) {
//
//            }
//        });
//
//        return notificationList;
//    }

    /**
     * Given a username, returns all notifications belonging to that user
     * @param username
     * @return
     * @deprecated
     */
//    public ArrayList<Notification> getUserNotifications(String username) {
//        // get reference to specific entry
//        Firebase tempRef = ref.child("Notifications");
//        final ArrayList<Notification> notificationList = new ArrayList<Notification>();
//        final String finalUsername = username;
//        // create a one time use listener to immediately access datasnapshot
//        tempRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                Type tokenType = new TypeToken<Book>(){}.getType();
//                Notification tempNotification;
//                String tempString;
//
//                for(DataSnapshot d: dataSnapshot.getChildren()) {
//                    tempString = d.getValue(String.class);
//                    if (tempString != null) {
//                        // Get user object from Gson
//                        Gson gson = new Gson();
//                        tempNotification = gson.fromJson(tempString, tokenType);
//                        if (tempNotification.getUserName().equals(finalUsername)) {
//                            notificationList.add(tempNotification);
//                        }
//                    } else {
//                        Log.d("Firebase Retrieval Error", "Notification doesn't exist or string is empty");
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(FirebaseError firebaseError) {
//
//            }
//        });
//
//        return notificationList;
//    }

//    public ArrayList<Book> getBooks() {
//        return null;
//    }

//    public ArrayList<Book> getAvailableBooks() {
//        return null;
//    }

//    public ArrayList<Book> getUserBooks(String username) {
//        return null;
//    }
}
