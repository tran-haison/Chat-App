package com.example.chat_client.ui.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.chat_client.databinding.ActivityUserInfoBinding;
import com.example.chat_client.dialogs.DialogButtonListener;
import com.example.chat_client.dialogs.DialogUtils;
import com.example.chat_client.models.Object;
import com.example.chat_client.models.User;
import com.example.chat_client.utils.Constants;
import com.google.android.material.snackbar.Snackbar;

import java.util.Objects;

import static com.example.chat_client.socket.ResponseMessage.FAIL_USERNAME_ALREADY_EXIST;
import static com.example.chat_client.socket.ResponseMessage.SUCCESS_SIGN_OUT;
import static com.example.chat_client.socket.ResponseMessage.SUCCESS_UPDATE;

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

        // Setup data binding
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);

        // View events
        binding.ibBack.setOnClickListener(v -> onBackPressed());
        binding.ibSignOut.setOnClickListener(v -> showDialogSignOut());
        binding.btnUpdateProfile.setOnClickListener(v -> showDialogUpdate());
    }

    private void setupViewModel() {
        viewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()).create(UserViewModel.class);

        // Observe response from server
        viewModel.responseMessageLiveData().observe(this, this::handleResponseMessage);

        viewModel.userLiveData.observe(this, user -> this.user = user);
    }

    private void handleResponseMessage(String message) {
        switch (message) {
            case FAIL_USERNAME_ALREADY_EXIST:
                Snackbar.make(binding.getRoot(), FAIL_USERNAME_ALREADY_EXIST, Snackbar.LENGTH_SHORT).show();
                break;
            case SUCCESS_UPDATE:
                Toast.makeText(this, SUCCESS_UPDATE, Toast.LENGTH_SHORT).show();
                saveUserInfo();
                break;
            case SUCCESS_SIGN_OUT:
                returnToUserSignInActivity();
                break;
        }
    }

    private void showDialogSignOut() {
        DialogUtils.dialogCustom(
                this,
                user,
                "Sign out now?",
                new DialogButtonListener() {
                    @Override
                    public void onNegativeClicked() {}

                    @Override
                    public void onPositiveClicked(Object object) {
                        viewModel.signOutUser();
                    }
                }
        );
    }

    private void showDialogUpdate() {
        DialogUtils.dialogCustom(
                this,
                user,
                "Do you want to update your information?",
                new DialogButtonListener() {
                    @Override
                    public void onNegativeClicked() {}

                    @Override
                    public void onPositiveClicked(Object object) {
                        updateUser();
                    }
                }
        );
    }

    private void saveUserInfo() {
        if (updatedUser != null) {
            viewModel.setUser(updatedUser);
        }
    }

    private void updateUser() {
        String username = Objects.requireNonNull(binding.etUsername.getText()).toString();
        String password = Objects.requireNonNull(binding.etPassword.getText()).toString();

        if (!user.getName().equals(username) || !user.getPassword().equals(password)) {
            updatedUser = new User(username, password);
            if (!updatedUser.isNameValid()) {
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

    private void returnToUserSignInActivity() {
        Intent intent = new Intent(this, UserSignInActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}