package ca.ualberta.ishelf.Models;

import android.media.Image;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Book class
 * Signatures created by Jeremy
 * -edited Mehrab
 * @author Jeremy
 *
 */
public class Book implements Parcelable{
    private String owner;
    private String holder;
    private String next_holder;
    private String name;
    private String description;
    private Long ISBN;
    private int status; // 1 = Available To Borrow / 0 = Borrowed / -1 = Not Available
    private Boolean borrowedBook = false;
    private ArrayList<Rating> ratings = new ArrayList<Rating>(); // Error due to no Rating class yet
    private ArrayList<String> galleryImages = new ArrayList<String>();
    private int indexCover = -1;
    private UUID id; // changed from int to UUID
    private Image photo;
    private String year;
    private String genre;
    private String author;
    private int transition;


    /*
    0 = is its available for booking
    1 = is when the Owner has accepted someones request.
    2 = is when you meet up with the person and pressed the lend button to hand over the book to that person
    3 = is when the borrower presses accept to receive the book
    4 = is when the borrower presses return to hand the book back to you
    It is set back to 0, when you press the accept to get the book back from the borrower, as the book becomes available again
     */

    public  Book(){
        this.status = 1;
        this.transition=0;
        this.id = UUID.randomUUID();
        this.indexCover = -1;
    }

    public Book(String name, String description, Long ISBN, String year, String genre, String author, boolean borrowedBook) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.description = description;
        this.ISBN = ISBN;
        this.year = year;
        this.genre = genre;
        this.author = author;
        this.status = 1;
        this.transition = 0;
        this.borrowedBook = borrowedBook;
        this.indexCover = -1;
    }


    protected Book(Parcel in) {
        owner = in.readString();
        next_holder = in.readString();
        holder= in.readString();
        name = in.readString();
        description = in.readString();
        if (in.readByte() == 0) {
            ISBN = null;
        } else {
            ISBN = in.readLong();
        }
        status = in.readInt();
        transition = in.readInt();
        byte tmpBorrowedBook = in.readByte();
        borrowedBook = tmpBorrowedBook == 0 ? null : tmpBorrowedBook == 1;
        year = in.readString();
        genre = in.readString();
        author = in.readString();
        id = UUID.fromString(in.readString());
        galleryImages = in.createStringArrayList();
        Gson gson = new Gson();
        String jRatings = in.readString();
        Type RatingList = new TypeToken<ArrayList<Rating>>(){}.getType();
        ratings = gson.fromJson(jRatings, RatingList);

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


    public int getTransition() {
        return transition;
    }

    public void setTransition(int transition) {
        this.transition = transition;
    }

    public String getAuthor() {
        return author;
    }

    public Boolean getBorrowedBook() {
        return borrowedBook;
    }

    public void setBorrowedBook(Boolean borrowedBook) {
        this.borrowedBook = borrowedBook;
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



    public Image getPhoto() {
        return photo;
    }

    public void setPhoto(Image photo) {
        this.photo = photo;
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

    /**
     * @deprecated
     * use setBorrowed() or setAvailable() instead
     */
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

    public String getOwner(){ return owner; }

    public String getNext_holder() {
        return next_holder;
    }

    public void setNext_holder(String next_holder) {
        this.next_holder= next_holder;
    }

    public String getHolder() {
        return holder;
    }

    public void setHolder(String name) {
        this.holder = name;
    }
    // Public Methods
    public float getAvgRating(){
        float sum=0;
        for(int i=0; i < this.ratings.size(); i++)
        {
            sum += this.ratings.get(i).getRating();
        }

        return (sum/this.ratings.size());
    }




    /**
     * setBorrowed() sets the status of the book to borrowed, so that others can't borrow it
     */
    public void setBorrowed(){
        //this.setStatus(0);
        this.status = 0;
    }

    /**
     * setAvailable() sets the status of the book to available, so that others can borrow it
     */
    public void setAvailable(){
        //this.setStatus(1);
        this.status = 1;
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
        // TODO: implement logic
        ratings.add(rating);
    }


    @Override
    public int describeContents() {
        return 0;
    }

    public ArrayList<String> getGalleryImages() {
        return galleryImages;
    }

    public void setGalleryImages(ArrayList<String> galleryImages) {
        this.galleryImages = galleryImages;
    }

    public void addImage(String image) {
        this.galleryImages.add(image);
    }

    public void removeImage(int position) {
        this.galleryImages.remove(position);
    }

    public int getIndexCover() {
        return indexCover;
    }

    public void setIndexCover(int indexCover) {
        this.indexCover = indexCover;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(owner);
        dest.writeString(next_holder);
        dest.writeString(holder);

        dest.writeString(name);
        dest.writeString(description);
        if (ISBN == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(ISBN);
        }
        dest.writeInt(status);
        dest.writeInt(transition);
        dest.writeByte((byte) (borrowedBook == null ? 0 : borrowedBook ? 1 : 2));
        dest.writeString(year);
        dest.writeString(genre);
        dest.writeString(author);
        dest.writeString(id.toString());
        dest.writeStringList(galleryImages);
        Gson gson = new Gson();
        String jRatings = gson.toJson(ratings);
        dest.writeString(jRatings);

    }
}