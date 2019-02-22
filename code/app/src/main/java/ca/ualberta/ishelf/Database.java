package ca.ualberta.ishelf;

import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.firebase.client.Firebase;
import java.util.ArrayList;

public class Database {
    private final String link = "";
    private Firebase ref;

    Database() {
        Firebase.setAndroidContext(this);
        ref = new Firebase(link);
    }

    public void addUser(User user) {
    }

    public void editUser(User user) {
        return null;
    }

    public void deleteUser(String username) {
    }

    public void addBook(Book book) {
    }

    public void deleteBook(int id) {
    }

    public User getUser(String username) {
        return null;
    }

    public Book getBook(UUID bookID) {
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
