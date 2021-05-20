package com.example.chat_client.ui.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.chat_client.databinding.ActivitySignUpBinding;
import com.example.chat_client.models.User;
import com.example.chat_client.ui.main.MainActivity;
import com.example.chat_client.utils.Constants;
import com.google.android.material.snackbar.Snackbar;

import java.util.Objects;

import static com.example.chat_client.socket.ResponseMessage.FAIL_USERNAME_ALREADY_EXIST;
import static com.example.chat_client.socket.ResponseMessage.SUCCESS_SIGN_UP;
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
            } else if (message.equals(SUCCESS_SIGN_UP)) {
                Toast.makeText(this, SUCCESS_SIGN_UP, Toast.LENGTH_SHORT).show();
                viewModel.setUser(user);
                goToMainActivity();
            }
        });
    }

    private void signUp() {
        // Get text from edit text
        String username = Objects.requireNonNull(binding.etUsername.getText()).toString();
        String password = Objects.requireNonNull(binding.etPassword.getText()).toString();
        String passwordRetype = Objects.requireNonNull(binding.etPasswordRetype.getText()).toString();

        // Check user info
        user = new User(username, password);
        if (!user.isUsernameValid()) {
            Snackbar.make(binding.getRoot(), Constants.USERNAME_INVALID, Snackbar.LENGTH_SHORT).show();
        } else if (!user.isPasswordValid()) {
            Snackbar.make(binding.getRoot(), Constants.PASSWORD_INVALID, Snackbar.LENGTH_SHORT).show();
        } else if (!password.equals(passwordRetype)) {
            Snackbar.make(binding.getRoot(), "Retyped password must be same", Snackbar.LENGTH_SHORT).show();
        } else {
            viewModel.signUpUser(user);
        }
    }

    private void goToMainActivity() {
        Bundle bundle = new Bundle();
        bundle.putSerializable(USER, user);
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(BUNDLE, bundle);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}