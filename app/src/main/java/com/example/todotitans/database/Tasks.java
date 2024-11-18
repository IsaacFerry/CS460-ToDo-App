package com.example.todotitans.database;

public class Tasks {

    public String taskId, userId, categoryId, priorityId, title, description, dueDate, status;

    // Default constructor
    public Tasks(){}

    public Tasks(String taskId, String userId, String categoryId, String priorityId, String title, String description, String dueDate, String status){
        this.taskId = taskId;
        this.userId = userId;
        this.categoryId = categoryId;
        this.priorityId = priorityId;
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.status = status;

    }
}
