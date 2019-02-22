package ca.ualberta.ishelf;

import java.util.ArrayList;
import java.util.UUID;

public class User {
    private String username;
    private ArrayList<UUID> borrowedBooks;
    private ArrayList<UUID> ownedBooks;
    private Rating rating;
    private ArrayList<Request> listofRequests;
    private ArrayList<Rating> lenderRatings;
    private ArrayList<Rating> borrowerRatings;
    private String state; //user needs to know if it's acting as a lender or borrower
    private String phoneNum;
    private String email;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Rating getRating() {
        return rating;
    }

    public Rating setRating(Rating rating) {
        this.rating = rating;
    }

    public ArrayList<UUID> getOwnedBooks() {
        return ownedBooks;
    }

    public void setOwnedBooks(ArrayList<UUID> ownedBooks) {
        this.ownedBooks = ownedBooks;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        if (state == "borrower" || state == "owner") {
            this.state = state;
        }
    }

    public ArrayList<UUID> getBorrowedBooks() {
        return borrowedBooks;
    }

    public void setBorrowedBooks(ArrayList<UUID> borrowedBooks) {
        this.borrowedBooks = borrowedBooks;
    }

    public ArrayList<Request> getListofRequests() {
        return listofRequests;
    }

    public void setListofRequests(ArrayList<Request> listofRequests) {
        this.listofRequests = listofRequests;
    }

    public ArrayList<Rating> getLenderRatings() {
        return lenderRatings;
    }

    public void setLenderRatings(ArrayList<Rating> lenderRatings) {
        this.lenderRatings = lenderRatings;
    }

    public ArrayList<Rating> getBorrowerRatings() {
        return borrowerRatings;
    }

    public void setBorrowerRatings(ArrayList<Rating> borrowerRatings) {
        this.borrowerRatings = borrowerRatings;
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
    /*should users be able to add books or delete/edit ratings or is that all firebase*/
    //these methods should be public
    public void addBook(UUID book){
        if (state == "borrower"){
            borrowedBooks.add(book);
        }else {
            ownedBooks.add(book);
        }
    }
    public void deleteBook(UUID book){
        if (state == "borrower"){
            for (int i = 0; i < borrowedBooks.size(); i++){
                if (borrowedBooks.get(i) == book){
                    borrowedBooks.remove(i);
                }
            }
        }else {
            for (int i = 0; i < ownedBooks.size(); i++){
                if (ownedBooks.get(i) == book){
                    ownedBooks.remove(i);
                }
            }
        }
    }
    public void addRating(Rating rating){
        if (state == "borrower"){
            borrowerRatings.add(rating);
        }else {
            lenderRatings.add(rating);
        }
    }
}
