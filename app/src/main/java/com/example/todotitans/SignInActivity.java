package com.example.todotitans;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignInActivity extends AppCompatActivity {
    private EditText inputEmail, inputPassword;
    private Button buttonLogin;
    private FirebaseAuth authentication;
    private ProgressBar progressBar;

    @Override
    protected  void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        authentication = FirebaseAuth.getInstance();

        // provided a link for UI elements
        inputEmail = findViewById(R.id.inputEmail);
        inputPassword = findViewById(R.id.inputPassword);
        buttonLogin = findViewById(R.id.buttonLogin);
        progressBar = findViewById(R.id.progressBar);

        buttonLogin.setOnClickListener(v -> signInUser());
        

    }



    private void signInUser() {
        loading(true);
        String email = inputEmail.getText().toString().trim();
        String password = inputPassword.getText().toString().trim();

        // Check for valid input
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill in both fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Sign in the user with Firebase Authentication
        authentication.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    loading(false);
                    if (task.isSuccessful()) {
                        FirebaseUser user = authentication.getCurrentUser();
                        if (user != null) {
                            Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    } else {
                        // login failure
                        Toast.makeText(this, "Login Failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });


    }

    private void loading(Boolean isLoading) {
        if (isLoading) {
            buttonLogin.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.VISIBLE);
        } else {
            buttonLogin.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.INVISIBLE);
        }
    }

}
