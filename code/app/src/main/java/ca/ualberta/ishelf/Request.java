package ca.ualberta.ishelf;

import java.util.Date;

public class Request {


    public Request(Date timeRequested) {
        this.timeRequested = timeRequested;
    }

    private Date timeRequested;

    public Date getTimeRequested() {
        return timeRequested;
    }

}
