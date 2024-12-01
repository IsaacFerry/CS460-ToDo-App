package com.example.todotitans.database;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Task {
import java.io.Serializable;
public class Task implements Serializable{

    private String taskId;
    private String userId;
    private String priorityLevel;
    private String title;
    private String description;
    private String dueDate;
    private String status;

    // Default constructor required for Firebase
    public Task() {
    }

    // Parameterized constructor
    public Task(String taskId, String userId, String priorityLevel, String title, String description, String dueDate, String status) {
        this.taskId = taskId;
        this.userId = userId;
        this.priorityLevel = priorityLevel;
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.status = status;
    }

    // Getters and Setters
    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPriorityLevel() {
        return priorityLevel;
    }

    public void setPriorityLevel(String priorityLevel) {
        this.priorityLevel = priorityLevel;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // Method to convert dueDate string to Date object
    public Date getDueDateAsDate() {
        String dueDateString = this.dueDate; // Replace with the correct field in your Task object
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            return sdf.parse(dueDateString);
        } catch (Exception e) {
            e.printStackTrace();
            return null; // Return null if parsing fails
        }
    }
}
