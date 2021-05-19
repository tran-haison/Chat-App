package com.example.chat_client.ui.user;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.chat_client.R;
import com.example.chat_client.databinding.ActivityUserInfoBinding;
import com.example.chat_client.models.User;
import com.example.chat_client.utils.Constants;
import com.google.android.material.snackbar.Snackbar;

import static com.example.chat_client.socket.ResponseMessage.FAIL_INCORRECT_USERNAME_OR_PASSWORD;
import static com.example.chat_client.socket.ResponseMessage.FAIL_USERNAME_ALREADY_EXIST;
import static com.example.chat_client.socket.ResponseMessage.FAIL_USER_ALREADY_SIGN_IN;
import static com.example.chat_client.socket.ResponseMessage.SUCCESS;

public class UserInfoActivity extends AppCompatActivity {

    private ActivityUserInfoBinding binding;
    private UserViewModel viewModel;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserInfoBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        // Setup
        setupViewModel();
        getIntentValue();

        // View implementation

        // View events
        binding.ibBack.setOnClickListener(v -> onBackPressed());
        binding.ibSignOut.setOnClickListener(v -> signOut());
        binding.btnUpdateProfile.setOnClickListener(v -> updateUser());
    }

    private void setupViewModel() {
        viewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()).create(UserViewModel.class);
        viewModel.getResponseMessageLiveData().observe(this, message -> {
            if (message.equals(FAIL_USERNAME_ALREADY_EXIST)) {
                Snackbar.make(binding.getRoot(), FAIL_USERNAME_ALREADY_EXIST, Snackbar.LENGTH_SHORT).show();
            } else if (message.equals(SUCCESS)) {
                Toast.makeText(this, SUCCESS, Toast.LENGTH_SHORT).show();
                // TODO: update current user
            }
        });
    }

    private void getIntentValue() {
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra(Constants.BUNDLE);
        if (bundle != null) {
            user = (User) bundle.getSerializable(Constants.USER);
        }
    }

    private void signOut() {
        viewModel.signOutUser();
        // Call intent to UserSignInActivity
        Intent intent = new Intent(this, UserSignInActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void updateUser() {
        String username = binding.etUsername.getText().toString();
        String password = binding.etPassword.getText().toString();

        if (user.getUsername().equals(username) && user.getPassword().equals(password)) {

        }
    }

}