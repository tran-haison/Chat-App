package com.example.chat_client.ui.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.chat_client.databinding.ActivityUserInfoBinding;
import com.example.chat_client.models.User;
import com.example.chat_client.utils.Constants;
import com.google.android.material.snackbar.Snackbar;

import java.util.Objects;

import static com.example.chat_client.socket.ResponseMessage.FAIL_USERNAME_ALREADY_EXIST;
import static com.example.chat_client.socket.ResponseMessage.SUCCESS_SIGN_OUT;
import static com.example.chat_client.socket.ResponseMessage.SUCCESS_UPDATE_USER;

public class UserInfoActivity extends AppCompatActivity {

    private ActivityUserInfoBinding binding;
    private UserViewModel viewModel;
    private User user, updatedUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserInfoBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        // Setup ViewModel
        setupViewModel();

        // View events
        binding.ibBack.setOnClickListener(v -> onBackPressed());
        binding.ibSignOut.setOnClickListener(v -> signOut());
        binding.btnUpdateProfile.setOnClickListener(v -> updateUser());
    }

    private void setupViewModel() {
        viewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()).create(UserViewModel.class);

        // Observe response from server
        viewModel.getResponseMessageLiveData().observe(this, message -> {
            if (message.equals(FAIL_USERNAME_ALREADY_EXIST)) {
                Snackbar.make(binding.getRoot(), FAIL_USERNAME_ALREADY_EXIST, Snackbar.LENGTH_SHORT).show();
            } else if (message.equals(SUCCESS_UPDATE_USER)) {
                Toast.makeText(this, SUCCESS_UPDATE_USER, Toast.LENGTH_SHORT).show();
                viewModel.setUser(updatedUser);
            } else if (message.equals(SUCCESS_SIGN_OUT)) {
                Intent intent = new Intent(this, UserSignInActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        // Observe user updated
        viewModel.getUserLiveData().observe(this, user -> {
            this.user = user;
            setUserInfo();
        });
    }

    private void setUserInfo() {
        binding.tvUsername.setText(user.getUsername());
        binding.etUsername.setText(user.getUsername());
        binding.etPassword.setText(user.getPassword());
    }

    private void signOut() {
        viewModel.setUser(null);
        viewModel.signOutUser();
    }

    private void updateUser() {
        String username = Objects.requireNonNull(binding.etUsername.getText()).toString();
        String password = Objects.requireNonNull(binding.etPassword.getText()).toString();

        if (!user.getUsername().equals(username) || !user.getPassword().equals(password)) {
            updatedUser = new User(username, password);
            if (!updatedUser.isUsernameValid()) {
                Snackbar.make(binding.getRoot(), Constants.USERNAME_INVALID, Snackbar.LENGTH_SHORT).show();
            } else if (!updatedUser.isPasswordValid()) {
                Snackbar.make(binding.getRoot(), Constants.PASSWORD_INVALID, Snackbar.LENGTH_SHORT).show();
            } else {
                viewModel.updateUser(updatedUser);
            }
        } else {
            Snackbar.make(binding.getRoot(), "Username or password must be different!", Snackbar.LENGTH_SHORT).show();
        }
    }

}