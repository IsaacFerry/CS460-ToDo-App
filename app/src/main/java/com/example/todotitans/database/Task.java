package com.example.todotitans.database;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.io.Serializable;



public class Task implements Serializable {

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

    /**
     * Constructs a new {@code Task} object with the specified details.
     *
     * @param taskId        The unique identifier for the task.
     * @param userId        The ID of the user associated with the task.
     * @param priorityLevel The priority level of the task (e.g., High, Medium, Low).
     * @param title         The title of the task.
     * @param description   A detailed description of the task.
     * @param dueDate       The due date of the task as a string (e.g., "MMMM dd, yyyy HH:mm").
     * @param status        The current status of the task (e.g., Pending, Completed).
     */
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

    /**
     * Converts the task's due date string into a {@link Date} object.
     *
     * @return The {@link Date} representation of the task's due date, or {@code null}
     *         if the parsing fails.
     * @throws NullPointerException if {@code dueDateString} is not initialized or null.
     */
    public Date getDueDateAsDate() {
        // Retrieve the due date as a string (replace with the correct field in your class)
        String dueDateString = this.dueDate; // Replace "this.dueDate" with your actual field

        // Define the date format that matches the format of the dueDateString
        SimpleDateFormat sdf = new SimpleDateFormat("MMMM dd, yyyy HH:mm", Locale.getDefault());

        try {
            // Parse the string into a Date object and return it
            return sdf.parse(dueDateString);
        } catch (Exception e) {
            // Log the error and return null if parsing fails
            e.printStackTrace();
            return null;
        }
    }

}
