package com.example.todotitans.database;

public class Notifications {

    public String notificationId, taskId, userId, notifyAt;

    // Default constructor
    public Notifications(){}

    public Notifications(String notificationId, String taskId, String userId, String notifyAt){
        this.notificationId = notificationId;
        this.taskId = taskId;
        this.userId = userId;
        this.notifyAt = notifyAt;

    }
}
