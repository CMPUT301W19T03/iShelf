package ca.ualberta.ishelf;

import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;
import java.util.UUID;

/**
 * A request object represents one user requesting to borrow a book from its owner
 * A request will always be stored under the owners User object, so we only need
 * to store the requesters username
 * Status is used to represent whether a request has been accepted, declined, or
 * if no action has been taken yet
 * Since these objects are not directly stored in firebase, they don't need a unique id
 */
public class Request implements Parcelable {
    // id of Request Object
    private UUID id;
    // id of book to be borrowed
    private UUID bookId;
    // username of requester
    private String requester;
    // username of owner
    private String owner;
    // when the book was requested
    private Date timeRequested;
    // Location set for meeting
    private Location location;
    // Status of the request
    private int status;
    // 1: accepted
    // -1: declined
    // 0: neither accepted nor declined

    /**
     * Empty initializer
     */
    public Request(){
        this.id = UUID.randomUUID();
    }

    /**
     * Request initializer
     * timeRequested is automatically set to when the request object is created
     * but can be manually set afterwards
     * @param bookId id of book being requested
     * @param requester id of person requesting the book
     */
    public Request(UUID bookId, String requester, String owner) {
        this.id = UUID.randomUUID();
        this.bookId = bookId;
        this.requester = requester;
        this.timeRequested = new Date();
        this.owner = owner;
    }

    protected Request(Parcel in) {
        requester = in.readString();
        status = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(requester);
        dest.writeInt(status);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Request> CREATOR = new Creator<Request>() {
        @Override
        public Request createFromParcel(Parcel in) {
            return new Request(in);
        }

        @Override
        public Request[] newArray(int size) {
            return new Request[size];
        }
    };

    /**
     * get the id of this request object
     * @return UUID the id of the Request object
     */
    public UUID getId() {
        return id;
    }

    /**
     * set the id of this request object
     * @param id the id of the Request object
     */
    public void setId(UUID id) {
        this.id = id;
    }

    /**
     * get the id of the book to be borrowed
     * @return UUID the id of the book
     */
    public UUID getBookId() {
        return bookId;
    }

    /**
     * set the id of the book to be borrowed
     * @param bookId the id of the book
     */
    public void setBookId(UUID bookId) {
        this.bookId = bookId;
    }

    /**
     * Get the username of the user requesting the book
     * @return String the username of the requester
     */
    public String getRequester() {
        return requester;
    }

    /**
     * set the username of the user requesting the book
     * @param requester the username of the requester
     */
    public void setRequester(String requester) {
        this.requester = requester;
    }

    /**
     * get the time that the book was requested
     * @return
     */

    /**
     * get the username of the owner of the book
     * @return String the username of the owner
     */
    public String getOwner() {
        return owner;
    }

    /**
     * set the username of the owner of the book
     * @param owner the username of the owner
     */
    public void setOwner(String owner) {
        this.owner = owner;
    }

    /**
     * Get the time that the book was requested
     * @return timeRequested the time requested
     */
    public Date getTimeRequested() {
        return timeRequested;
    }

    /**
     * set the time that the book was requested
     * @param timeRequested the time requested
     */
    public void setTimeRequested(Date timeRequested) {
        this.timeRequested = timeRequested;
    }

    /**
     * get the location that the owner designated to meet at
     * @return Location to pick up the book
     */
    public Location getLocation() {
        return location;
    }

    /**
     * set the location for that owner and requester to meet at
     * set the location using a location object
     * @param location the location to pick up the book
     */
    public void setLocation(Location location) {
        this.location = location;
    }

    /**
     * set the location for that owner and requester to meet at
     * set the location using doubles representing latitude and longitude
     * @param latitude the latitude of the location
     * @param longitude the longitude of the location
     */
    public void setLocation(double latitude, double longitude) {
        this.location = new Location("");
        this.location.setLatitude(latitude);
        this.location.setLongitude(longitude);
    }

    /**
     * Set the status of the request to accepted, reprented by a 1
     * This means that the owner has accepted the request
     */
    public void accept() {
        this.status = 1;
    }

    /**
     * Set the status of the request to declined, represented by -1
     * This means that the owner has declined the request
     */
    public void decline() {
        this.status = -1;
    }

    /**
     * Get the status of the request
     * If the status is 0, then the owner has taken no action on the request
     * If the status is 1, then the owner has accepted the request
     * If the status is -1, then the owner has declined the request
     * @return int the status of the request
     */
    public int getStatus() {
        return status;
    }

    /**
     * Return a string representation of the request
     * This is a very simple toSting method which may or may not be useful
     * @return String the request as a string
     */
    @Override
    public String toString() {
        String message = "User " + requester + "requested to borrow a book at "
                + timeRequested.toString();
        return message;
    }
}
