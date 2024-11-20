package com.example.todotitans;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import com.google.android.material.button.MaterialButton;

public class ForgotPasswordActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        MaterialButton signInButton = findViewById(R.id.buttonBackToSignIn);
        MaterialButton signUpButton = findViewById(R.id.buttonSignUp);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // navigation to sign in
                Intent intent = new Intent(ForgotPasswordActivity.this, SignInActivity.class);
                startActivity(intent);
            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // navigation to sign up
                Intent intent = new Intent(ForgotPasswordActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
    }

    private void forgotPassword() {
        return;
    }
}
