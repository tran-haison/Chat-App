package com.example.chat_client.ui.user;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.chat_client.App;
import com.example.chat_client.models.User;
import com.example.chat_client.socket.Client;
import com.example.chat_client.socket.RequestMessage;

public class UserViewModel extends ViewModel {

    private final Client client;
    private final MutableLiveData<User> userMutableLiveData = new MutableLiveData<>();

    public UserViewModel() {
        client = Client.getInstance();
    }

    public void signUpUser(User user) {
        client.sendMessage(RequestMessage.signUp(user));
    }

    public void signInUser(User user) {
        client.sendMessage(RequestMessage.signIn(user));
    }

    public void signOutUser() {
        client.sendMessage(RequestMessage.signOut());
    }

    public void updateUser(User user) {
        client.sendMessage(RequestMessage.updateUser(user));
    }

    public void setUser(User user) {
        userMutableLiveData.setValue(user);
    }

    public LiveData<User> getUserLiveData() {
        return userMutableLiveData;
    }

    public LiveData<String> getResponseMessageLiveData() {
        return App.responseMessage;
    }

}
