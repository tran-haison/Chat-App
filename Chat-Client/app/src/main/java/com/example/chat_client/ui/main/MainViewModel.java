package com.example.chat_client.ui.main;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.chat_client.models.User;

public class MainViewModel extends ViewModel {

    private final MutableLiveData<User> userMutableLiveData = new MutableLiveData<>();

    public void setUser(User user) {
        userMutableLiveData.setValue(user);
    }

    public LiveData<User> getUserLiveData() {
        return userMutableLiveData;
    }
}
