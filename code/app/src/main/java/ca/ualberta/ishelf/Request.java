package ca.ualberta.ishelf;

import java.util.Date;
import java.util.UUID;

public class Request {
    private UUID id;
    private User requester;
    private User owner;
    private Book book;
    private Date timeRequested;
    // 1: accepted
    // -1: declined
    // 0: neither accepted nor declined
    private int status;

    public Request(){
        this.id = UUID.randomUUID();
    }

    public Request(Date timeRequested, User requester, Book book) {
        this.id = UUID.randomUUID();
        this.requester = requester;
        this.book = book;
        this.timeRequested = timeRequested;
    }

    public Request(User requester, User owner, Book book) {
        this.id = UUID.randomUUID();
        this.timeRequested = new Date();
        this.owner = owner;
        this.requester = requester;
        this.book = book;
        this.status = 0;
    }

    public void setTimeRequested(Date timeRequested) {
        this.timeRequested = timeRequested;
    }


    public Date getTimeRequested() {
        return timeRequested;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public User getRequester() {
        return requester;
    }

    public void setRequester(User requester) {
        this.requester = requester;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void accept() {
        this.status = 1;
    }

    public void decline() {
        this.status = -1;
    }

    @Override
    public String toString() {
        String message = "User " + requester.getUsername() + " with rating " + requester.getRating() +
                " requested to borrow " + book.getName() + " at " + timeRequested.toString();
        return message;
    }
}
