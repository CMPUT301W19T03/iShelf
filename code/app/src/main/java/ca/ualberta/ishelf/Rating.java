package ca.ualberta.ishelf;

public class Rating {


    private int rating;



    private String comment;
    Rating(){};

    Rating(int rating){
        this.rating=rating;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

}
