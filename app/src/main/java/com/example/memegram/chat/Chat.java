package com.example.memegram.chat;

public class Chat {
    String latestMessage,senderUsername,imageURL;
    public Chat(String imageURL,String latestMessage,String senderUsername){
        this.imageURL = imageURL;
        this.latestMessage = latestMessage;
        this.senderUsername = senderUsername;
    }

    public String getSenderUsername() {
        return senderUsername;
    }

    public String getImageURL() {
        return imageURL;
    }

    public String getLatestMessage() {
        return latestMessage;
    }
}
