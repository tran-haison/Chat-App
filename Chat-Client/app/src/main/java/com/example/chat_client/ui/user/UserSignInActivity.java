package com.example.chat_client.ui.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.chat_client.databinding.ActivitySignInBinding;
import com.example.chat_client.models.User;
import com.example.chat_client.ui.main.MainActivity;
import com.example.chat_client.utils.Constants;
import com.google.android.material.snackbar.Snackbar;

import java.util.Objects;

import static com.example.chat_client.socket.ResponseMessage.FAIL_INCORRECT_USERNAME_OR_PASSWORD;
import static com.example.chat_client.socket.ResponseMessage.FAIL_USER_ALREADY_SIGN_IN;
import static com.example.chat_client.socket.ResponseMessage.SUCCESS;
import static com.example.chat_client.utils.Constants.BUNDLE;
import static com.example.chat_client.utils.Constants.USER;

public class UserSignInActivity extends AppCompatActivity {

    private ActivitySignInBinding binding;
    private UserViewModel viewModel;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignInBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        // Setup ViewModel
        setupViewModel();

        // Get data from UserSignUpActivity
        getUserFromSignUp();

        // Button clicked events
        binding.btnSignIn.setOnClickListener(v -> signIn());
        binding.btnSignUp.setOnClickListener(v -> startActivity(
                new Intent(UserSignInActivity.this, UserSignUpActivity.class))
        );
    }

    private void setupViewModel() {
        viewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()).create(UserViewModel.class);
        viewModel.getResponseMessageLiveData().observe(this, message -> {
            switch (message) {
                case FAIL_USER_ALREADY_SIGN_IN:
                    Snackbar.make(binding.getRoot(), FAIL_USER_ALREADY_SIGN_IN, Snackbar.LENGTH_SHORT).show();
                    break;
                case FAIL_INCORRECT_USERNAME_OR_PASSWORD:
                    Snackbar.make(binding.getRoot(), FAIL_INCORRECT_USERNAME_OR_PASSWORD, Snackbar.LENGTH_SHORT).show();
                    break;
                case SUCCESS:
                    Toast.makeText(this, SUCCESS, Toast.LENGTH_SHORT).show();
                    goToMainActivity(user);
                    break;
            }
        });
    }

    private void getUserFromSignUp() {
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra(Constants.BUNDLE);

        if (bundle != null) {
            user = (User) bundle.getSerializable(Constants.USER);
            Objects.requireNonNull(binding.tilUsername.getEditText()).setText(user.getUsername());
            Objects.requireNonNull(binding.tilPassword.getEditText()).setText(user.getPassword());
        }
    }

    private void signIn() {
        // Remove error of text input layout
        binding.tilUsername.setError(null);
        binding.tilPassword.setError(null);

        // Get text from edit text
        String username = Objects.requireNonNull(binding.tilUsername.getEditText())
                .getText().toString();
        String password = Objects.requireNonNull(binding.tilPassword.getEditText())
                .getText().toString();

        user = new User(username, password);
        if (!user.isUsernameValid()) {
            binding.tilUsername.setError(Constants.USERNAME_INVALID);
        } else if (!user.isPasswordValid()) {
            binding.tilPassword.setError(Constants.PASSWORD_INVALID);
        } else {
            viewModel.signInUser(user);
        }
    }

    private void goToMainActivity(User user) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(USER, user);
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(BUNDLE, bundle);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}