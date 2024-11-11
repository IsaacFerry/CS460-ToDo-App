package com.example.todotitans;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.todotitans.database.databaseReferences;

public class MainActivity extends AppCompatActivity {

    databaseReferences dbReferences = new databaseReferences();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Generate a unique ID for the user
        String userId = dbReferences.usersRef.push().getKey();

        // Add a new user with the unique userId for testing
        dbReferences.addUser(userId, "example@example.com", "securepassword123");
    }
}