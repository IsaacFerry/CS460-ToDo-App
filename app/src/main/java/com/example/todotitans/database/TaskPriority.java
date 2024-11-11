package com.example.todotitans.database;

public class TaskPriority {
    public String priorityId, priorityLevel;

    // Default constructor
    public TaskPriority(){}

    public TaskPriority(String priorityId, String priorityLevel){
        this.priorityId = priorityId;
        this.priorityLevel = priorityLevel;

    }
}
