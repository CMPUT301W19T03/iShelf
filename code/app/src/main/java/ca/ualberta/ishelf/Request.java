package ca.ualberta.ishelf;

import java.util.Date;
import java.util.UUID;

public class Request {

    private Book book;
    private User requester;
    private UUID id;


    public Request(){
        this.id = UUID.randomUUID();
    }

    public Request(Date timeRequested, User requester, Book book) {
        this.id = UUID.randomUUID();
        this.requester = requester;
        this.book = book;
        this.timeRequested = timeRequested;
    }


    private Date timeRequested;

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

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

}
