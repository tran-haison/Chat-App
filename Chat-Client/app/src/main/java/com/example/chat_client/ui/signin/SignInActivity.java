package com.example.chat_client.ui.signin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.chat_client.databinding.ActivitySigninBinding;
import com.example.chat_client.ui.signup.SignUpActivity;
import com.example.chat_client.ui.signup.SignUpViewModel;

public class SignInActivity extends AppCompatActivity {

    private ActivitySigninBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySigninBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        // Sign up user
        binding.btnSignUp.setOnClickListener(v -> startActivity(
                new Intent(SignInActivity.this, SignUpActivity.class)));
    }


}