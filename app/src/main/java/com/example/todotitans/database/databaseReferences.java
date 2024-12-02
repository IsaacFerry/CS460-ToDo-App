package com.example.todotitans.database;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * This class provides a convenient way to manage Firebase Database references.
 * It initializes a connection to the Firebase Realtime Database and sets up a
 * reference to the "users" node in the database.
 */
public class databaseReferences {

    private final FirebaseDatabase database;
    public final DatabaseReference usersRef;

    /**
     * Constructs a new {@code databaseReferences} object.
     * <p>
     * This constructor initializes the Firebase Realtime Database instance and
     * sets up the {@code usersRef} to point to the "users" node in the database.
     * </p>
     */
    public databaseReferences() {
        // Initialize Firebase Database and users node reference
        database = FirebaseDatabase.getInstance();
        usersRef = database.getReference("users");
    }

}