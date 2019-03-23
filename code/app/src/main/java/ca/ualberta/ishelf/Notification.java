package ca.ualberta.ishelf;

import java.util.Date;
import java.util.UUID;

/**
 * display notifications
 */
public class Notification implements Comparable<Notification> {
    private Date date;
    private String text;
    private String userName;
    private UUID id; // changed from int to UUID

    public Notification(){
        this.id = UUID.randomUUID();
    }

    public Notification(Date date, String text, String userName){
        this.date = date;
        this.text = text;
        this.userName = userName;
        this.id = UUID.randomUUID();
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

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    @Override
    public int compareTo(Notification o) {
        Date d1 = this.getDate();
        Date d2 = o.getDate();
        if (d1.after(d2)) {
            return 1;
        } else if (d1.before(d2)) {
            return -1;
        }
        return 0;
    }
}
