package com.example.chat_client.ui.chat;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.chat_client.App;
import com.example.chat_client.models.Group;
import com.example.chat_client.models.User;
import com.example.chat_client.socket.Client;
import com.example.chat_client.socket.RequestMessage;

public class ChatViewModel extends ViewModel {

    private final Client client;

    public ChatViewModel() {
        client = Client.getInstance();
    }

    public void friendMessage(User user, String message) {
        client.sendMessage(RequestMessage.friendMessage(user, message));
    }

    public void groupMessage(Group group, String message) {
        client.sendMessage(RequestMessage.groupMessage(group, message));
    }

    public void listAllMembers(Group group) {
        client.sendMessage(RequestMessage.listAllMember(group));
    }

    public void friendFile(User user, byte[] file) {
        client.sendFile(RequestMessage.friendFile(user, file));
    }

    public LiveData<String> responseLiveData() {
        return App.responseMessage;
    }

}
