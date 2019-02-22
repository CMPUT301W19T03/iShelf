package ca.ualberta.ishelf;

//TODO get these imports working
import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
//import com.google.gson.Gson;
//import com.google.gson.reflect.TypeToken;
import com.firebase.client.Firebase;
import java.util.ArrayList;

public class FBInterface {
    String url;
    Firebase ref;

    FBInterface(String url) {
        this.url = url;
        Firebase.setAndroidContext(this);
        //TODO build firebase for project
        ref = new Firebase("insert database url here");
    }

    public void addUser(User user) {
    }

    public User editUser(String username) {
        return null;
    }

    //public void deleteUser(String username) {}
    public void addBook(Book book) {
    }

    public void deleteBook(int id) {
    }

    public User getUser(String username) {
        return null;
    }

    public Book getBook(int bookdID) {
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
