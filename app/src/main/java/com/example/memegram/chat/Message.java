package com.example.memegram.chat;

public class Message {
    String message,senderUsername,profileURL,date,time;
    long createdAt;
    public Message(String message, String senderUsername,String profileURL,String date,String time){
        this.message = message;
        this.senderUsername = senderUsername;
        this.profileURL = profileURL;
        this.time = time;
        this.date = date;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public String getMessage() {
        return message;
    }

    public String getSenderUsername() {
        return senderUsername;
    }

    public String getProfileURL() {
        return profileURL;
    }

    public String getDate() { return date; }

    public String getTime() {return time; }

}
