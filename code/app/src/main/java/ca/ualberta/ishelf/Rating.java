package ca.ualberta.ishelf;

public class Rating {


    private int rating;
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

}
