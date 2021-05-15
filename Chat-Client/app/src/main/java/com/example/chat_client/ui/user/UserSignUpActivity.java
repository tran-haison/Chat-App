package com.example.chat_client.ui.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.chat_client.databinding.ActivitySignUpBinding;
import com.example.chat_client.models.User;
import com.example.chat_client.socket.ResponseMessage;
import com.example.chat_client.utils.Constants;
import com.google.android.material.snackbar.Snackbar;

import java.util.Objects;

import static com.example.chat_client.socket.ResponseMessage.FAIL_USERNAME_ALREADY_EXIST;
import static com.example.chat_client.socket.ResponseMessage.SUCCESS;
import static com.example.chat_client.utils.Constants.BUNDLE;
import static com.example.chat_client.utils.Constants.USER;

public class UserSignUpActivity extends AppCompatActivity {

    private ActivitySignUpBinding binding;
    private UserViewModel viewModel;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        // Setup ViewModel
        setupViewModel();

        // Button clicked events
        binding.btnSignUp.setOnClickListener(v -> signUp());
        binding.tvSignIn.setOnClickListener(v -> onBackPressed());
    }

    private void setupViewModel() {
        viewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()).create(UserViewModel.class);
        viewModel.getResponseMessageLiveData().observe(this, message -> {
            if (message.equals(FAIL_USERNAME_ALREADY_EXIST)) {
                Snackbar.make(binding.getRoot(), FAIL_USERNAME_ALREADY_EXIST, Snackbar.LENGTH_SHORT).show();
            } else if (message.equals(SUCCESS)) {
                Toast.makeText(this, SUCCESS, Toast.LENGTH_SHORT).show();
                goToSignInActivity(user);
            }
        });
    }

    private void signUp() {
        // Reset error of text input layout
        binding.tilUsername.setError(null);
        binding.tilPassword.setError(null);
        binding.tilRetypePassword.setError(null);

        // Get text from edit text
        String username = Objects.requireNonNull(binding.tilUsername.getEditText())
                .getText().toString();
        String password = Objects.requireNonNull(binding.tilPassword.getEditText())
                .getText().toString();
        String passwordRetype = Objects.requireNonNull(binding.tilRetypePassword.getEditText())
                .getText().toString();

        // Check user info
        user = new User(username, password);
        if (!user.isUsernameValid()) {
            binding.tilUsername.setError(Constants.USERNAME_INVALID);
        } else if (!user.isPasswordValid()) {
            binding.tilPassword.setError(Constants.PASSWORD_INVALID);
        } else if (!password.equals(passwordRetype)) {
            binding.tilRetypePassword.setError("Re-typed password must be same!");
        } else {
            viewModel.signUpUser(user);
        }
    }

    private void goToSignInActivity(User user) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(USER, user);
        Intent intent = new Intent(UserSignUpActivity.this, UserSignInActivity.class);
        intent.putExtra(BUNDLE, bundle);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

}