package com.example.memegram.ui.home;


public class Comment {
    int imageURL;
    String username,CommentText,timeText;
    public  Comment(int imageURL, String username,String CommentText, String timeText){
        this.imageURL = imageURL;
        this.CommentText = CommentText;
        this.username = username;
        this.timeText = timeText;
    }

    public int getImageURL() {
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
