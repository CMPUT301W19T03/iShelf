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

public class Database extends Application {
    private final String link = "https://ishelf-bb4e7.firebaseio.com";
    private Firebase ref;

    //TODO documentation
    //TODO saveInFile so that we actually return an object
    Database(Context context) {
        Firebase.setAndroidContext(context);
        ref = new Firebase(link);
    }

    public void addUser(User user) {
        // Save user to Firebase
        Firebase userchild = ref.child("Users").child(user.getUsername());
        // Convert to Gson
        Gson gson = new Gson();
        String jUser = gson.toJson(user);
        // Save to firebase
        userchild.setValue(jUser);
    }

    public void deleteUser(String username) {
        // get reference to specific entry
        Firebase tempRef = ref.child("Users").child(username);
        // create a one time use listener to immediately access datasnapshot
        tempRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            // Delete our entry
            public void onDataChange(DataSnapshot dataSnapshot) {
                dataSnapshot.getRef().removeValue();
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {
                return;
            }
        });
    }

    public void editUser(User user) {
        deleteUser(user.getUsername());
        addUser(user);
    }

    public User getUser(String username) {
        // get reference to specific entry
        Firebase tempRef = ref.child("Users").child(username);
        // create a one time use listener to immediately access datasnapshot
        tempRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String jUser = dataSnapshot.getValue(String.class);
                if (jUser != null) {
                    // Get user object from Gson
                    Gson gson = new Gson();
                    Type tokenType = new TypeToken<User>(){}.getType();
                    User user = gson.fromJson(jUser, tokenType);
                    Log.d("Confirm", user.getUsername());
                } else {
                    Log.d("Firebase Retrieval Error", "User doesn't exist or string is empty");
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                return;
            }
        });
        // If User doesn't exist, or string is empty, return null
        Log.d("next", "this is where code jumps to next");
        return null;
    }

    public void addBook(Book book) {
        // Save user to Firebase
        Firebase bookchild = ref.child("Books").child(book.getId().toString());
        // Convert to Gson
        Gson gson = new Gson();
        String jBook = gson.toJson(book);
        // Save to firebase
        bookchild.setValue(jBook);
    }

    public void deleteBook(UUID id) {
        // get reference to specific entry
        Firebase tempRef = ref.child("Books").child(id.toString());
        // create a one time use listener to immediately access datasnapshot
        tempRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            // Delete our entry
            public void onDataChange(DataSnapshot dataSnapshot) {
                dataSnapshot.getRef().removeValue();
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {
                return;
            }
        });
    }

    public void editBook(Book book) {
        deleteBook(book.getId());
        addBook(book);
    }

    public Book getBook(UUID bookId) {
        // get reference to specific entry
        Firebase tempRef = ref.child("Books").child(bookId.toString());
        // create a one time use listener to immediately access datasnapshot
        tempRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String jBook = dataSnapshot.getValue(String.class);
                if (jBook != null) {
                    // Get user object from Gson
                    Gson gson = new Gson();
                    Type tokenType = new TypeToken<Book>(){}.getType();
                    Book book = gson.fromJson(jBook, tokenType);
                    //TODO how to return this book object?
                    //Log.d("Confirm", book.getId());
                } else {
                    Log.d("Firebase Retrieval Error", "User doesn't exist or string is empty");
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                return;
            }
        });
        // If Book doesn't exist, or string is empty, return null
        return null;
    }


    public void addNotification(Notification notification) {
        // Save Notification to Firebase
        Firebase notificationchild = ref.child("Notifications").child(notification.getId().toString());
        // Convert to Gson
        Gson gson = new Gson();
        String jNotification = gson.toJson(notification);
        // Save to firebase
        notificationchild.setValue(jNotification);
    }

    public void deleteNotification(UUID id) {
        // get reference to specific entry
        Firebase tempRef = ref.child("Notifications").child(id.toString());
        // create a one time use listener to immediately access datasnapshot
        tempRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            // Delete our entry
            public void onDataChange(DataSnapshot dataSnapshot) {
                dataSnapshot.getRef().removeValue();
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {
                return;
            }
        });
    }

    public void editNotification(Notification notification) {
        deleteNotification(notification.getId());
        addNotification(notification);
    }

    public Notification getNotification(UUID id){
        // get reference to specific entry
        Firebase tempRef = ref.child("Notifications").child(id.toString());
        // create a one time use listener to immediately access datasnapshot
        tempRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String jNotification = dataSnapshot.getValue(String.class);
                if (jNotification != null) {
                    // Get user object from Gson
                    Gson gson = new Gson();
                    Type tokenType = new TypeToken<Book>(){}.getType();
                    Notification notification = gson.fromJson(jNotification, tokenType);
                    //TODO how to return this book object?
                    //Log.d("Confirm", book.getId());
                } else {
                    Log.d("Firebase Retrieval Error", "Notification doesn't exist or string is empty");
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                return;
            }
        });
        // If Notification doesn't exist, or string is empty, return null
        return null;
    }


    public ArrayList<Notification> getUserNotifications(String username) {
        // get reference to specific entry
        Firebase tempRef = ref.child("Notifications");
        // create a one time use listener to immediately access datasnapshot
        tempRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<Notification> notificationList = new ArrayList<Notification>();
                Type tokenType = new TypeToken<Book>(){}.getType();
                Notification tempNotification;
                String tempString;

                for(DataSnapshot d: dataSnapshot.getChildren()) {
                    tempString = d.getValue(String.class);
                    if (tempString != null) {
                        // Get user object from Gson
                        Gson gson = new Gson();
                        tempNotification = gson.fromJson(tempString, tokenType);
                        /*
                        if (tempNotification.getUserName().equals(username)) {
                            notificationList.add(tempNotification);
                        }
                        */
                        notificationList.add(tempNotification);
                    } else {
                        Log.d("Firebase Retrieval Error", "Notification doesn't exist or string is empty");
                    }
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        return null;
    }

    public ArrayList<Book> getBooks() {
        return null;
    }

    public ArrayList<Book> getAvailableBooks() {
        return null;
    }

    public ArrayList<Book> getUserBooks(String username) {
        return null;
    }
}
