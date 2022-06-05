package com.example.memegram.ui.discover;

public class DiscoverItem {
    private String username,location,imageURL;
    public DiscoverItem(String username, String location, String imageURL){
        this.location = location;
        this.username = username;
        this.imageURL = imageURL;
    }

    public String getUsername(){ return this.username; }
    public String getLocation(){ return this.location; }
    public String getImageURL(){ return this.imageURL; }
}

