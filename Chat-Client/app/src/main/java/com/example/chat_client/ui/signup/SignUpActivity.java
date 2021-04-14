package com.example.chat_client.ui.signup;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.example.chat_client.MainActivity;
import com.example.chat_client.databinding.ActivitySignUpBinding;
import com.example.chat_client.models.User;

import static com.example.chat_client.utils.Constants.KEY_USER;

public class SignUpActivity extends AppCompatActivity {

    private ActivitySignUpBinding binding;
    private SignUpViewModel viewModel;

    private String username = "";
    private String password = "";
    private String passwordRetype = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        // ViewModel handler
        initViewModel();

        // Sign up user
        binding.btnSignUp.setOnClickListener(v -> {
            username = binding.tilUsername.getEditText().getText().toString();
            password = binding.tilPassword.getEditText().getText().toString();
            passwordRetype = binding.tilRetypePassword.getEditText().getText().toString();
            viewModel.checkInputText(username, password, passwordRetype);
        });

        // Sign in user
        binding.tvSignIn.setOnClickListener(v -> onBackPressed());
    }

    private void initViewModel() {
        viewModel = ViewModelProviders.of(this).get(SignUpViewModel.class);
        viewModel.getInputTextState().observe(this, isInputTextValid -> {
            if (isInputTextValid) {
                User user = new User(username, password);
                viewModel.signUpUser(user);
            } else {
                Toast.makeText(this, "Please retype username/password", Toast.LENGTH_SHORT).show();
            }
        });
        viewModel.getUserState().observe(this, isUserExisted -> {
            if (isUserExisted) {
                Toast.makeText(this, "User already exist", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Sign up user successfully", Toast.LENGTH_SHORT).show();
                goToMainActivity(new User(username, password));
            }
        });
    }

    private void goToMainActivity(User user) {
        Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
        intent.putExtra(KEY_USER, user);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

}