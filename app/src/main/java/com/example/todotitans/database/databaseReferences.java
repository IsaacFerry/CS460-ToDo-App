package com.example.todotitans.database;

import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class databaseReferences {

    private final FirebaseDatabase database;
    public final DatabaseReference usersRef;

    public databaseReferences() {
        // Initialize Firebase Database and users node reference
        database = FirebaseDatabase.getInstance();
        usersRef = database.getReference("users");
    }

    public void addUser(String userId, String email, String password, String firstName, String lastName) {
        // Create a User object without storing the password for security
        User user = new User(userId, email, password, firstName, lastName);

        // Add the user data to the "users" node with userId as the key
        usersRef.child(userId).setValue(user)
                .addOnSuccessListener(aVoid -> {
                    // Optional: Log success
                    Log.d("DatabaseReferences", "User added successfully to Firebase Database");
                })
                .addOnFailureListener(e -> {
                    // Optional: Log failure
                    Log.e("DatabaseReferences", "Failed to add user", e);
                });
    }
}