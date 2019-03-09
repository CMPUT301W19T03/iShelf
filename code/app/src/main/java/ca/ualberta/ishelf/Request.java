package ca.ualberta.ishelf;

import java.util.Date;
import java.util.UUID;

public class Request {
    private UUID id;
    private UUID bookId;
    private String requester;
    private Date timeRequested;
    private int status;
    // 1: accepted
    // -1: declined
    // 0: neither accepted nor declined

    public Request(){
        this.id = UUID.randomUUID();
    }

    public Request(UUID bookId, String requester) {
        this.id = UUID.randomUUID();
        this.bookId = bookId;
        this.requester = requester;
        this.timeRequested = new Date();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getBookId() {
        return bookId;
    }

    public void setBookId(UUID bookId) {
        this.bookId = bookId;
    }

    public String getRequester() {
        return requester;
    }

    public void setRequester(String requester) {
        this.requester = requester;
    }

    public Date getTimeRequested() {
        return timeRequested;
    }

    public void setTimeRequested(Date timeRequested) {
        this.timeRequested = timeRequested;
    }

    public void accept() {
        this.status = 1;
    }

    public void decline() {
        this.status = -1;
    }

    @Override
    public String toString() {
        String message = "User " + requester + "requested to borrow a book at "
                + timeRequested.toString();
        return message;
    }
}
