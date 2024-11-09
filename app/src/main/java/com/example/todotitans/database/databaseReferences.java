package com.example.todotitans.database;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class databaseReferences {

    // Initialize Firebase Database
    FirebaseDatabase database = FirebaseDatabase.getInstance();


    // Define references for each table node
    // creating temporary "tables"
    public DatabaseReference usersRef = database.getReference("users");
    DatabaseReference tasksRef = database.getReference("tasks");
    DatabaseReference categoriesRef = database.getReference("categories");
    DatabaseReference priorityRef = database.getReference("priority");


    public databaseReferences() {
        // Set empty values to create the nodes
        usersRef.setValue("test");         // Creates "users" node
        tasksRef.setValue("test");         // Creates "tasks" node
        categoriesRef.setValue("test");    // Creates "categories" node
        priorityRef.setValue("test");      // Creates "priority" node

    }

    public void addUser(String userId, String email, String password) {
        // Create an instance of the User model class
        User user = new User(userId, email, password);

        // Add the user to the "users" node with userId as the key
        usersRef.child(userId).setValue(user)
                .addOnSuccessListener(aVoid -> {
                    // Successfully added user data
                })
                .addOnFailureListener(e -> {
                    // Failed to add user data
                });
    }





}
