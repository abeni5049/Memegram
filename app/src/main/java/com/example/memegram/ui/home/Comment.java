package com.example.memegram.ui.home;


public class Comment {

    String username,CommentText,timeText,imageURL;
    public  Comment(String imageURL, String username,String CommentText, String timeText){
        this.imageURL = imageURL;
        this.CommentText = CommentText;
        this.username = username;
        this.timeText = timeText;
    }

    public String getImageURL() {
        return imageURL;
    }

    public String getCommentText() {
        return CommentText;
    }

    public String getTimeText() {
        return timeText;
    }

    public String getUsername() {
        return username;
    }
}
