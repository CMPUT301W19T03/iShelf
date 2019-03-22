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

    /**
     * Empty initializer for Notification
     * Still generates a random UUID
     */
    public Notification(){
        this.id = UUID.randomUUID();
    }

    /**
     * Initializer for Notification
     * @param date the date the notification was created
     * @param text the text of the notification
     * @param userName the username the notification is meant for
     */
    public Notification(Date date, String text, String userName){
        this.date = date;
        this.text = text;
        this.userName = userName;
        this.id = UUID.randomUUID();
    }

    /**
     * Get the notification date
     * @return Date
     */
    public Date getDate() {
        return date;
    }

    /**
     * Set the notification date
     * @param date the date the notification was created
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * Get the text of the notification, or its message
     * @return String the message
     */
    public String getText() {
        return text;
    }

    /**
     * Set the text of the notification, or its message
     * @param text the message of the notification
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * Get the username of the user the notification is intended for
     * @return String the user's unique username
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Set the username of the user the notification is intended for
     * @param userName the username of the user
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * Get the notifications unique id as a UUID
     * @return UUID the id
     */
    public UUID getId() {
        return id;
    }

    /**
     * Set the notifications unique id as a UUID
     * @param id the unique id of the notification
     */
    public void setId(UUID id) {
        this.id = id;
    }

    /**
     * Set the comparitor for the sorting of notifications
     * Notifications are sorted in order of date created, aka
     * Older notifications are sorted in front of Newer notifications
     * This is reversed when they are displayed
     * @param o
     * @return
     */
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
