package com.example.todotitans;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.todotitans.database.databaseReferences;
import com.example.todotitans.databinding.ActivitySignUpBinding;
import com.example.todotitans.utilities.Constants;
import com.example.todotitans.utilities.PreferenceManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;


public class SignUpActivity extends AppCompatActivity {

    private ActivitySignUpBinding binding;
    private PreferenceManager preferenceManager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        preferenceManager = new PreferenceManager(getApplicationContext());

        setListeners();

    }

    private void setListeners() {
        binding.buttonLogin.setOnClickListener(v -> onBackPressed());

        binding.buttonSignUp.setOnClickListener(v -> {
            if(isValidSignUpDetails()){
                registerUser();
            }
        });

    }

    private void showToast(String message){
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void registerUser() {
        // Show loading indicator (if applicable)
        loading(true);

        // Get instance of Firebase Realtime Database
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        // Create a unique key for the user (or you can use user ID)
        String userId = database.getReference(Constants.KEY_COLLECTION_USERS).push().getKey();

        // Prepare user data as a HashMap
        HashMap<String, String> user = new HashMap<>();
        user.put(Constants.KEY_FIRST_NAME, binding.inputFirstName.getText().toString());
        user.put(Constants.KEY_LAST_NAME, binding.inputLastName.getText().toString());
        user.put(Constants.KEY_EMAIL, binding.inputEmail.getText().toString());
        user.put(Constants.KEY_PASSWORD, binding.inputPassword.getText().toString());

        // Write user data to Realtime Database
        if (userId != null) {
            database.getReference(Constants.KEY_COLLECTION_USERS)
                    .child(userId)
                    .setValue(user)
                    .addOnSuccessListener(unused -> {
                        // Hide loading indicator
                        loading(false);

                        // Save user details in shared preferences
                        preferenceManager.putBoolean(Constants.KEY_IS_SIGNED_IN, true);
                        preferenceManager.putString(Constants.KEY_FIRST_NAME, binding.inputFirstName.getText().toString());
                        preferenceManager.putString(Constants.KEY_LAST_NAME, binding.inputLastName.getText().toString());

                        // Navigate to the next activity
                        Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    })
                    .addOnFailureListener(exception -> {
                        // Hide loading indicator
                        loading(false);

                        // Show error message
                        showToast(exception.getMessage());
                    });
        } else {
            // Handle the case where userId is null
            showToast("Failed to generate user ID. Please try again.");
        }
    }


    private Boolean isValidSignUpDetails(){
        if(binding.inputFirstName.getText().toString().trim().isEmpty()){
            showToast("Please enter your first name");
            return false;
        } else if (binding.inputLastName.getText().toString().trim().isEmpty()){
            showToast("Please enter your last name");
            return false;
        } else if (binding.inputEmail.getText().toString().trim().isEmpty()){
            showToast("Please enter your email");
            return false;
        } else if (binding.inputPassword.getText().toString().trim().isEmpty()){
            showToast("Please enter a password");
            return false;
        } else if (binding.inputConfirmPassword.getText().toString().trim().isEmpty()){
            showToast("Please confirm your password");
            return false;
        } else if (!binding.inputPassword.getText().toString().equals(binding.inputConfirmPassword.getText().toString())){
            showToast("Passwords do not match");
            return false;
        } else {
            return true;
        }
    }

    private void loading (Boolean isLoading){
        if(isLoading){
            binding.buttonLogin.setVisibility(View.INVISIBLE);
            binding.progressBar.setVisibility(View.VISIBLE);
        } else {
            binding.buttonLogin.setVisibility(View.VISIBLE);
            binding.progressBar.setVisibility(View.INVISIBLE);
        }
    }

}