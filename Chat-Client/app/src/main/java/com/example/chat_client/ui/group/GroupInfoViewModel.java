package com.example.chat_client.ui.group;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.chat_client.App;
import com.example.chat_client.models.Group;
import com.example.chat_client.socket.Client;
import com.example.chat_client.socket.RequestMessage;

public class GroupInfoViewModel extends ViewModel {

    private final Client client;

    public GroupInfoViewModel() {
        client = Client.getInstance();
    }

    public void quitGroup(Group group) {
        client.sendMessage(RequestMessage.quitGroup(group));
    }

    public LiveData<String> responseMessageLiveData() {
        return App.responseMessage;
    }
}
