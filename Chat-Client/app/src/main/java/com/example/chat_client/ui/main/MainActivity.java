package com.example.chat_client.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.example.chat_client.R;
import com.example.chat_client.databinding.ActivityMainBinding;
import com.example.chat_client.models.User;
import com.example.chat_client.ui.user.UserInfoActivity;
import com.example.chat_client.utils.Constants;

import static com.example.chat_client.utils.Constants.BUNDLE;
import static com.example.chat_client.utils.Constants.USER;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private MainViewModel viewModel;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        // Setup
        setupViewModel();
        getIntentValue();

        // View implementation
        NavigationUI.setupWithNavController(binding.bottomNavigationView, Navigation.findNavController(this, R.id.mainNavHostFragment));

        // View events
        binding.llUser.setOnClickListener(v -> goToUserInfoActivity());
    }

    private void setupViewModel() {
        viewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()).create(MainViewModel.class);
        viewModel.getUserLiveData().observe(this, user -> {
            this.user = user;

            // TODO: update view
        });
    }

    private void getIntentValue() {
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra(Constants.BUNDLE);
        if (bundle != null) {
            user = (User) bundle.getSerializable(Constants.USER);
            viewModel.setUser(user);
        }
    }

    private void goToUserInfoActivity() {
        Bundle bundle = new Bundle();
        bundle.putSerializable(USER, user);
        Intent intent = new Intent(this, UserInfoActivity.class);
        intent.putExtra(BUNDLE, bundle);
        startActivity(intent);
    }
}