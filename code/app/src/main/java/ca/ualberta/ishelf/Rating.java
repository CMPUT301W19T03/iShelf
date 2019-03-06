package ca.ualberta.ishelf;

public class Rating {
    private int rating;
    private String comment;

    public Rating() { //Evan - added constructor for rating to test add books
        this.rating = 4;
        this.comment = "comment here please";
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
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
