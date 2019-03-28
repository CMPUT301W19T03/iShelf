package ca.ualberta.ishelf;

import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.UUID;
/**
 * deleted "state" and rearranged some code, need to get ratings working - Evan
 * deleted listOfRequests since we are restructuring firebase - Randal
 * @author Evan
 *
 */
public class User implements Serializable {
    private String username;
    private ArrayList<UUID> borrowedBooks = new ArrayList<>();
    private ArrayList<UUID> ownedBooks = new ArrayList<>();
    private Rating rating;
    private ArrayList<Rating> ratingArrayList = new ArrayList<>();
    private String phoneNum;
    private String email;
    private static final String TAG = "User";

    public  User(){}


    public User(String username, String phoneNum, String email) {
        this.username = username;
        this.phoneNum = phoneNum;
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Rating getRating() {
        return rating;
    }

    public void setRating(Rating rating) {
        this.rating = rating;
    }

    public ArrayList<UUID> getOwnedBooks() {
        return ownedBooks;
    }

    public void setOwnedBooks(ArrayList<UUID> ownedBooks) {
        this.ownedBooks = ownedBooks;
    }

    public ArrayList<UUID> getBorrowedBooks() {
        return borrowedBooks;
    }

    public void setBorrowedBooks(ArrayList<UUID> borrowedBooks) {
        this.borrowedBooks = borrowedBooks;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (toString().indexOf("@") >= 0) {
            this.email = email;
        } // else it should popup something like "fake ass email bro" and prompt for re-enter
    }

    public ArrayList<Rating> getRatingArrayList() {
        return ratingArrayList;
    }

    /**
     * delete the book UUID from the list of user owned books
     * @param bookId the id of the book
     * @author rmnattas
     * @edited Evan
     */
    public void addOwnedBook(UUID bookId){
        ownedBooks.add(bookId);
    }
    public void deleteOwnedBook(UUID bookId){
        ownedBooks.remove(bookId);
    }
    public void addBorrowedBook(UUID bookId){
        borrowedBooks.add(bookId);
    }
    public void removeBorrowedBook(UUID bookId){
        borrowedBooks.remove(bookId);
    }
    public float getOverallRating(){
        int numberOfRatings = ratingArrayList.size();
        float aggregateRating = 0;
        if (ratingArrayList.size() != 0) {
            for (int i = 0; i < ratingArrayList.size(); i++) {
                aggregateRating += ratingArrayList.get(i).getRating();
            }
        }
        aggregateRating = aggregateRating / numberOfRatings;
        return aggregateRating;

    }

    public void addRating(Rating rating){
        ratingArrayList.add(rating);
    }
}
