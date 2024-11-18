package com.example.todotitans.database;

public class User {
    public String userId, email, password, firstName, lastName;

    // Default constructor
    public User(){}

    public User(String userId, String email, String password, String firstName, String lastName){
        this.userId = userId;
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
    }

}
