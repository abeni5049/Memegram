package com.example.memegram.ui.notifications;

public class Notification {
    int imageURL;
    String username,notificationText,timeText;
    public  Notification(int imageURL, String username,String notificationText, String timeText){
        this.imageURL = imageURL;
        this.notificationText = notificationText;
        this.username = username;
        this.timeText = timeText;
    }

    public int getImageURL() {
        return imageURL;
    }

    public String getNotificationText() {
        return notificationText;
    }

    public String getTimeText() {
        return timeText;
    }

    public String getUsername() {
        return username;
    }
}
