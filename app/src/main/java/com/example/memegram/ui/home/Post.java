package com.example.memegram.ui.home;

public class Post {
    private String username,location,imageURL;
    private int numberOfLikes;
    private  boolean liked;
    public Post(String username, String location,String imageURL,int numberOfLikes,boolean liked){
        this.username = username;
        this.location = location;
        this.imageURL = imageURL;
        this.numberOfLikes = numberOfLikes;
        this.liked = liked;
    }

    public String getUsername(){ return username; }
    public String getLocation(){ return  location; }
    public String getImageURL() { return imageURL; }
    public int getNumberOfLikes() { return numberOfLikes; }
    public boolean isLiked() { return liked; }

}
