package ca.ualberta.ishelf;

public class displayBooks { //nope jk probably dont need this
    private String name;
    private String image;

    public displayBooks() {
        this.name = "name";
        this.image = "https://upload.wikimedia.org/wikipedia/commons/1/1e/Default-avatar.jpg";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
