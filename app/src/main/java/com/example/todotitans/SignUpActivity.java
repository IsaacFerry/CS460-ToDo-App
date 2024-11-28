package com.example.todotitans;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.todotitans.databinding.ActivitySignUpBinding;
import com.example.todotitans.utilities.Constants;
import com.example.todotitans.utilities.PreferenceManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import java.util.HashMap;


/**
 * The {@code SignUpActivity} class handles user sign-up functionality.
 * This activity allows new users to register by providing their details,
 * such as first name, last name, email, and password, and saves their information
 * to a Firebase Realtime Database.
 */
public class SignUpActivity extends AppCompatActivity {

    private ActivitySignUpBinding binding;
    private PreferenceManager preferenceManager;


    /**
     * Called when the activity is first created.
     * Initializes the activity and sets up the layout and listeners.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously
     *                           being shut down, this contains the most recent data supplied.
     *                           Otherwise, it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        preferenceManager = new PreferenceManager(getApplicationContext());

        setListeners();

    }

    /**
     * Sets up listeners for the sign-up and login buttons.
     * Handles user interactions for navigating back or starting the registration process.
     */
    private void setListeners() {
        binding.buttonLogin.setOnClickListener(v -> onBackPressed());

        binding.buttonSignUp.setOnClickListener(v -> {
            if(isValidSignUpDetails()){
                registerUser();

            }
        });

    }

    /**
     * Displays a toast message to the user.
     *
     * @param message The message to display in the toast.
     */
    private void showToast(String message){
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    /**
     * Handles user registration by validating and storing user details in the Firebase Realtime Database.
     * Also saves user preferences and navigates to the next activity upon successful registration.
     */
    private void registerUser() {
        loading(true);

        String email = binding.inputEmail.getText().toString();
        String password = binding.inputPassword.getText().toString();

        // Firebase Authentication instance
        FirebaseAuth auth = FirebaseAuth.getInstance();

        // Create user in Firebase Authentication
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    loading(false);
                    if (task.isSuccessful()) {
                        // Successfully created user
                        FirebaseUser firebaseUser = auth.getCurrentUser();
                        if (firebaseUser != null) {
                            String userId = firebaseUser.getUid();

                            // Save additional user data in Realtime Database
                            HashMap<String, String> user = new HashMap<>();
                            user.put(Constants.KEY_FIRST_NAME, binding.inputFirstName.getText().toString());
                            user.put(Constants.KEY_LAST_NAME, binding.inputLastName.getText().toString());
                            user.put(Constants.KEY_EMAIL, email);
                            user.put(Constants.KEY_USER_ID, userId);
                            user.put(Constants.KEY_PASSWORD, binding.inputPassword.getText().toString());
                            user.put(Constants.KEY_SECURE_QUESTION, binding.inputSecurityQuestion.getText().toString());

                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            database.getReference(Constants.KEY_COLLECTION_USERS)
                                    .child(userId)
                                    .setValue(user)
                                    .addOnSuccessListener(unused -> {
                                        preferenceManager.putBoolean(Constants.KEY_IS_SIGNED_IN, true);
                                        preferenceManager.putString(Constants.KEY_FIRST_NAME, binding.inputFirstName.getText().toString());
                                        preferenceManager.putString(Constants.KEY_LAST_NAME, binding.inputLastName.getText().toString());

                                        // Navigate to the next activity
                                        Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                    })
                                    .addOnFailureListener(e -> showToast("Failed to save user: " + e.getMessage()));
                        }
                    } else {
                        // Show error message
                        showToast("Registration Failed: " + task.getException().getMessage());
                    }
                });
    }


    /**
     * Validates the user's input during the sign-up process.
     * Ensures all fields are filled and passwords match.
     *
     * @return {@code true} if the input details are valid; {@code false} otherwise.
     */
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

    /**
     * Displays or hides a loading indicator during the registration process.
     *
     * @param isLoading {@code true} to show the loading indicator; {@code false} to hide it.
     */
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