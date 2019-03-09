package ca.ualberta.ishelf;

import java.util.Date;

public class Request {
    private String owner;
    private String borrower;
    private String bookName;
    private int borrowerRating;
    private Date timeRequested;
    // 1: accepted
    // -1: declined
    // 0: neither accepted nor declined
    private int status;

    public Request(Date timeRequested) {
        this.timeRequested = timeRequested;
    }

    public Request(Date timeRequested, String owner, String borrower,
                   String bookName, int borrowerRating) {
        this.timeRequested = timeRequested;
        this.owner = owner;
        this.borrower = borrower;
        this.bookName = bookName;
        this.borrowerRating = borrowerRating;
        this.status = 0;
    }

    public Date getTimeRequested() {
        return timeRequested;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public void setBorrower(String borrower) {
        this.borrower = borrower;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getOwner() {
        return owner;
    }

    public String getBorrower() {
        return borrower;
    }

    public String getBookName() {
        return bookName;
    }

    public void accept() {
        this.status = 1;
    }

    public void decline() {
        this.status = -1;
    }

    @Override
    public String toString() {
        String message = "User " + borrower + " with rating " + borrowerRating +
                " requested to borrow " + bookName + " at " + timeRequested.toString();
        return message;
    }
}
