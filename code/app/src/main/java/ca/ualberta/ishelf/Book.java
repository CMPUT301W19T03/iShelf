package ca.ualberta.ishelf;


import java.util.ArrayList;
import java.util.UUID;

/**
 * Book class
 * Signatures created by Jeremy
 *
 */
public class Book {
    private String name;
    private String description;
    private Integer ISBN;
    private int status = 1; // 1 = Available To Borrow / 0 = Borrowed / -1 = Not Available
    private ArrayList<Rating> ratings; // Error due to no Rating class yet
    private UUID id; // changed from int to UUID

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
    public Integer getISBN() {
        return ISBN;
    }

    public void setISBN(int ISBN) {
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


    // Public Methods

    /**
     * setBorrowed() sets the status of the book to borrowed, so that others can't borrow it
     */
    public void setBorrowed(){
        // TODO: implement logic
    }

    /**
     * setAvailable() sets the status of the book to available, so that others can borrow it
     */
    public void setAvailable(){
        // TODO: implement logic
    }

    /**
     * checkBorrowed() returns true if available, false otherwise
     */
    public boolean checkBorrowed(){
        // TODO: implement logic
    }


    /**
     * addRating() adds a rating to the book's list of ratings
     */
    public void addRating(Rating rating){
        // TODO: implement logic
    }

}