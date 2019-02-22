package ca.ualberta.ishelf;

import java.util.ArrayList;

public class User {
    private String username;
    private ArrayList<Book> borrowerBooks;
    private ArrayList<Book> ownedBooks;
    private Rating rating;
    private ArrayList<Request> listofRequests;
    private ArrayList<Rating> lenderRatings;
    private ArrayList<Rating> borrowerRatings;
    private String state; //user needs to know if it's acting as a lender or borrower

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

    public ArrayList<Book> getOwnedBooks() {
        return ownedBooks;
    }

    public void setOwnedBooks(ArrayList<Book> ownedBooks) {
        this.ownedBooks = ownedBooks;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public ArrayList<Book> getBorrowerBooks() {
        return borrowerBooks;
    }

    public void setBorrowerBooks(ArrayList<Book> borrowerBooks) {
        this.borrowerBooks = borrowerBooks;
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

    /*should users be able to add books or delete/edit ratings or is that all firebase*/
    //these methods should be public
    public void deleteBook(Book book){
        for (int i = 0; i < ownedBooks.size(); i++){
            if (ownedBooks.get(i) == book){
                ownedBooks.remove(i);
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
