package com.example.todotitans.database;

public class User {
    public String userId, email, password, firstName, lastName, secureQuestion;

    // Default constructor
    public User(){}

    /**
     * Constructs a new {@code User} object with the specified details.
     *
     * @param userId         The unique identifier for the user.
     * @param email          The email address of the user.
     * @param password       The password for the user (should be securely handled in production).
     * @param firstName      The first name of the user.
     * @param lastName       The last name of the user.
     * @param secureQuestion A security question associated with the user for account recovery purposes.
     */
    public User(String userId, String email, String password, String firstName, String lastName, String secureQuestion) {
        this.userId = userId;
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.secureQuestion = secureQuestion;
    }

}
