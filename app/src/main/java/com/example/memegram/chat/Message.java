package com.example.memegram.chat;

public class Message {
    String message,senderUsername,profileURL;
    long createdAt;
    public Message(String message, String senderUsername, long createdAt,String profileURL){
        this.message = message;
        this.senderUsername = senderUsername;
        this.createdAt = createdAt;
        this.profileURL = profileURL;
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
}
