package ca.ualberta.ishelf;

/**
 *THis is the Wow factor rating object, which deals with the star rating
 * of an User or Book object. The rating is stored as a float, which is the
 * number of staes it has. There is also a comment stored with a persons rating
 *
 * @author: Mehrab
 */

public class Rating {
    private float rating;
    private String comment;

    public Rating() { //Evan - added constructor for rating to test add books
        //this.rating = 4;
        //this.comment = "comment here please";
    }

    // constructor that takes a rating and a string
    public Rating(float rating, String comment){
        if(rating > 5){
            this.rating = 5;
        } else if(rating < 0){
            this.rating = 0;
        } else {
            this.rating = rating;
        }
        this.comment = comment;
    }

    public float getRating() {
        return rating;
    }
//setting a rating, rating is a float ranging from 0 to 5.
    public void setRating(float rating) {
        if(rating>=0 && rating <=5) {
            this.rating = rating;
        }
        else{
            this.rating =-1;
        }

    }
    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

}
