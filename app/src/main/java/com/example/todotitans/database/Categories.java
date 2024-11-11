package com.example.todotitans.database;

public class Categories {

    public String userId, categoryId, categoryName;

    // Default constructor
    public Categories(){}

    public Categories(String userId, String categoryId, String categoryName){
        this.userId = userId;
        this.categoryId = categoryId;
        this.categoryName = categoryName;

    }
}
