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

public class MainActivity extends AppCompatActivity {

    private MainViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        // Setup ViewModel
        setupViewModel();

        // Setup data binding
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);

        // View implementation
        NavigationUI.setupWithNavController(binding.bottomNavigationView, Navigation.findNavController(this, R.id.mainNavHostFragment));

        // View events
        binding.llUser.setOnClickListener(v -> goToUserInfoActivity());
    }

    private void setupViewModel() {
        viewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()).create(MainViewModel.class);
    }

    private void goToUserInfoActivity() {
        Intent intent = new Intent(this, UserInfoActivity.class);
        startActivity(intent);
    }
}