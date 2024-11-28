package com.example.todotitans.database;

public class User {
    public String userId, email, password, firstName, lastName, secureQuestion;

    // Default constructor
    public User(){}

    public User(String userId, String email, String password, String firstName, String lastName, String secureQuestion){
        this.userId = userId;
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.secureQuestion = secureQuestion;
    }

}
