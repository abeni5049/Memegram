package com.example.memegram.ui.home;

public class Post {
    private String username,location;
    public Post(String username, String location){
        this.username = username;
        this.location = location;
    }

    public String getUsername(){ return username; }
    public String getLocation(){ return  location; }
}
