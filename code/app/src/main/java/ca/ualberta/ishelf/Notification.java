package ca.ualberta.ishelf;

import java.util.Date;

public class Notification {
    private Date date;
    private String text;
    private String userName;

    public Notification(Date date, String text, String userName){
        this.date = date;
        this.text = text;
        this.userName = userName;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

}
