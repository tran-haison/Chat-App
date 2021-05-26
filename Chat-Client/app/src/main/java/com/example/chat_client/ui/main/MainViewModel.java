package com.example.chat_client.ui.main;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.chat_client.App;
import com.example.chat_client.models.Group;
import com.example.chat_client.models.User;
import com.example.chat_client.socket.Client;
import com.example.chat_client.socket.RequestMessage;

public class MainViewModel extends ViewModel {

    private final Client client;
    public final LiveData<User> userLiveData;

    public MainViewModel() {
        client = Client.getInstance();
        userLiveData = client.userLiveData();
    }

    public LiveData<String> responseMessageLiveData() {
        return App.responseMessage;
    }

    /**********
     * FRIEND *
     **********/

    public void listFriend() {
        client.sendMessage(RequestMessage.listFriend());
    }

    public void listFriendRequest() {
        client.sendMessage(RequestMessage.listFriendRequest());
    }

    /*********
     * GROUP *
     *********/

    public void joinGroup(Group group) {
        client.sendMessage(RequestMessage.joinGroup(group));
    }

    public void listJoinedGroup() {
        client.sendMessage(RequestMessage.listJoinedGroup());
    }

    public void listNotJoinedGroup() {
        client.sendMessage(RequestMessage.listNotJoinedGroup());
    }

    public void createGroup(Group group) {
        client.sendMessage(RequestMessage.createGroup(group));
    }

    /**********
     * PEOPLE *
     **********/

    public void listUser() {
        client.sendMessage(RequestMessage.listUser());
    }

    public void addFriend(User user) {
        client.sendMessage(RequestMessage.addFriend(user));
    }

    public void acceptFriend(User user) {
        client.sendMessage(RequestMessage.acceptFriend(user));
    }

    public void denyRequest(User user) {
        client.sendMessage(RequestMessage.denyRequest(user));
    }
}
