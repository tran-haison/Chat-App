package com.example.chat_client.ui.user;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.chat_client.App;
import com.example.chat_client.models.User;
import com.example.chat_client.socket.Client;
import com.example.chat_client.socket.RequestMessage;

public class UserViewModel extends ViewModel {

    private final Client client;

    public UserViewModel() {
        client = Client.getInstance();
    }

    public void signUpUser(User user) {
        client.sendMessage(RequestMessage.signUp(user));
    }

    public void signInUser(User user) {
        client.sendMessage(RequestMessage.signIn(user));
    }

    public LiveData<String> getResponseMessageLiveData() {
        return App.responseMessage;
    }

}
