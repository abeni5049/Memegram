package com.example.memegram.ui.home;

public class Post {
    private String username,location,imageURL;
    public Post(String username, String location,String imageURL){
        this.username = username;
        this.location = location;
        this.imageURL = imageURL;
    }

    public String getUsername(){ return username; }
    public String getLocation(){ return  location; }
    public String getImageURL() { return imageURL; }

}
