package com.example.memegram.chat;

public class Chat {
    int imageURL;
    String latestMessage,senderUsername;
    public Chat(int imageURL,String latestMessage,String senderUsername){
        this.imageURL = imageURL;
        this.latestMessage = latestMessage;
        this.senderUsername = senderUsername;
    }

    public String getSenderUsername() {
        return senderUsername;
    }

    public int getImageURL() {
        return imageURL;
    }

    public String getLatestMessage() {
        return latestMessage;
    }
}
