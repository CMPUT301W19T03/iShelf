package ca.ualberta.ishelf;


import android.media.Image;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Book class
 * Signatures created by Jeremy
 *
 */
public class Book {
    private String owner;
    private String name;
    private String description;
    private Long ISBN;
    private int status; // 1 = Available To Borrow / 0 = Borrowed / -1 = Not Available
    private ArrayList<Rating> ratings = new ArrayList<>();
    private UUID id; // changed from int to UUID
    private Image photo;

    /**
     * Evan - created a constructor which set all values to null (except status)
     *  this is to check when the owner is inputing a new books that he has al
     *      the required fields. So add something like "if (foo == null) {display popup telling him to enter in required fields}"
     */
    public Book() {
        this.owner = null;
        this.name = null;
        this.description = null;
        this.ISBN = null;
        this.status = 1;
        this.ratings = null;
        this.id = null;
        this.photo = null;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    // Should this return an int or String representation?
    public Long getISBN() {
        return ISBN;
    }

    public void setISBN(Long ISBN) {
        this.ISBN = ISBN;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public ArrayList<Rating> getRatings() {
        return ratings;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setOwner(String name) {this.owner = name;}


    // Public Methods

    /**
     * setBorrowed() sets the status of the book to borrowed, so that others can't borrow it
     */
    public void setBorrowed(){
        this.setStatus(0);
    }

    /**
     * setAvailable() sets the status of the book to available, so that others can borrow it
     */
    public void setAvailable(){
        this.setStatus(1);
    }

    /**
     * checkBorrowed() returns true if available, false otherwise
     */
    public boolean checkBorrowed(){
        if(this.getStatus()== 0){
            return true;
        }
        return false;
    }


    /**
     * addRating() adds a rating to the book's list of ratings
     */
    public void addRating(Rating rating){
        ratings.add(rating);
    }
}