package ca.ualberta.ishelf;


import android.media.Image;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Book class
 * Signatures created by Jeremy
 *
 */
public class Book implements Parcelable {
    private String owner;
    private String name;
    private String description;
    private Long ISBN;
    private int status = 1; // 1 = Available To Borrow / 0 = Borrowed / -1 = Not Available
    private ArrayList<Rating> ratings = new ArrayList<Rating>(); // Error due to no Rating class yet
    private UUID id; // changed from int to UUID
    private Image photo;
    private String year;
    private String genre;
    private String author;

    public  Book(){}
    public Book(String name, String description, Long ISBN, String year, String genre, String author) {
        this.name = name;
        this.description = description;
        this.ISBN = ISBN;
        this.year = year;
        this.genre = genre;
        this.author = author;
    }

    protected Book(Parcel in) {
        owner = in.readString();
        name = in.readString();
        description = in.readString();
        if (in.readByte() == 0) {
            ISBN = null;
        } else {
            ISBN = in.readLong();
        }
        status = in.readInt();
        year = in.readString();
        genre = in.readString();
        author = in.readString();
    }

    public static final Creator<Book> CREATOR = new Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel in) {
            return new Book(in);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
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
        // TODO: implement logic
        this.setStatus(0);
    }

    /**
     * setAvailable() sets the status of the book to available, so that others can borrow it
     */
    public void setAvailable(){
        // TODO: implement logic
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

        // TODO: implement logic

    }


    /**
     * addRating() adds a rating to the book's list of ratings
     */
    public void addRating(Rating rating){
        // TODO: implement logic
        ratings.add(rating);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(owner);
        dest.writeString(name);
        dest.writeString(description);
        if (ISBN == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(ISBN);
        }
        dest.writeInt(status);
        dest.writeString(year);
        dest.writeString(genre);
        dest.writeString(author);
    }
}