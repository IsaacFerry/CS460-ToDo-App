package com.example.todotitans.database;

public class User {
    public String userId, email, password;

    // Default constructor
    public User(){}

    public User(String userId, String email, String password){
        this.userId = userId;
        this.email = email;
        this.password = password;
    }

}
