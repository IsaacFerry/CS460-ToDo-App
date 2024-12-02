package com.example.todotitans;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.example.todotitans.databinding.ActivityForgotPasswordBinding;
import com.example.todotitans.utilities.Constants;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


/**
 * The {@code ForgotPasswordActivity} class handles the forgot password functionality for users.
 * <p>
 * This activity allows users to recover their passwords by providing their registered email
 * and the answer to their security question. Users can also navigate to the Sign In or Sign Up activities.
 * </p>
 */
public class ForgotPasswordActivity extends Activity {
    private ActivityForgotPasswordBinding binding;

    /**
     * Called when the activity is starting. Initializes the layout, sets up navigation buttons,
     * and defines the forgot password functionality.
     *
     * @param savedInstanceState If the activity is being reinitialized after being previously shut down,
     *                           this Bundle contains the most recent data supplied; otherwise, it is null.
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityForgotPasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        MaterialButton signInButton = binding.buttonBackToSignIn;
        MaterialButton signUpButton = binding.buttonSignUp;
        MaterialButton forgotPasswordButton = binding.buttonResetPassword;

        // Navigation to SignIn Activity
        signInButton.setOnClickListener(v -> {
            Intent intent = new Intent(ForgotPasswordActivity.this, SignInActivity.class);
            startActivity(intent);
        });

        // Navigation to SignUp Activity
        signUpButton.setOnClickListener(v -> {
            Intent intent = new Intent(ForgotPasswordActivity.this, SignUpActivity.class);
            startActivity(intent);
        });

        forgotPasswordButton.setOnClickListener(v -> forgotPassword());
    }

    /**
     * Handles the forgot password functionality.
     * <p>
     * This method validates the user's input, queries the Firebase Realtime Database to find the user
     * by their email, and compares the entered security question answer with the stored value.
     * If successful, the user's password is revealed; otherwise, an error message is displayed.
     * </p>
     */
    private void forgotPassword() {
        String email = binding.inputEmail.getText().toString().trim();
        String secureQuestion = binding.inputSecurityQuestion.getText().toString().trim();

        if (email.isEmpty() || secureQuestion.isEmpty()) {
            Toast.makeText(getApplicationContext(), "please fill in both fields", Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference usersRef = database.getReference(Constants.KEY_COLLECTION_USERS);

        // Query to find the user by email and security question
        usersRef.orderByChild(Constants.KEY_EMAIL).equalTo(email)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                                String storedSecureQuestion = userSnapshot.child(Constants.KEY_SECURE_QUESTION).getValue(String.class);
                                String password = userSnapshot.child(Constants.KEY_PASSWORD).getValue(String.class);

                                if (secureQuestion.equals(storedSecureQuestion)) {
                                    // Password is revealed in a toast message
                                    Toast.makeText(getApplicationContext(), "Your Password: " + password, Toast.LENGTH_SHORT).show();
                                } else {
                                    // Incorrect security question response
                                    Toast.makeText(getApplicationContext(), "Incorrect Security Question Response", Toast.LENGTH_SHORT).show();
                                }
                            }
                        } else {
                            // User not found
                            Toast.makeText(getApplicationContext(), "Email not found", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Handle error if database query fails
                        Toast.makeText(getApplicationContext(), "Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}