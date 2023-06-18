package com.example.idverifier;

public class notification
{
    String notificationTitle,notificationMessage;
    String notifier;

    public notification() {
    }

    public notification(String notificationTitle, String notificationMessage, String notifier) {
        this.notificationTitle = notificationTitle;
        this.notificationMessage = notificationMessage;
        this.notifier = notifier;
    }

    public String getNotificationTitle() {
        return notificationTitle;
    }

    public void setNotificationTitle(String notificationTitle) {
        this.notificationTitle = notificationTitle;
    }

    public String getNotificationMessage() {
        return notificationMessage;
    }

    public void setNotificationMessage(String notificationMessage) {
        this.notificationMessage = notificationMessage;
    }

    public String getNotifier() {
        return notifier;
    }

    public void setNotifier(String notifier) {
        this.notifier = notifier;
    }
}
