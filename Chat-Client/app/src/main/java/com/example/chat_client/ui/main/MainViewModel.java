package com.example.chat_client.ui.main;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.chat_client.App;
import com.example.chat_client.models.Group;
import com.example.chat_client.models.User;
import com.example.chat_client.socket.Client;
import com.example.chat_client.socket.RequestMessage;

import java.util.List;

public class MainViewModel extends ViewModel {

    private final Client client;
    public final LiveData<User> userLiveData;

    public MainViewModel() {
        client = Client.getInstance();
        userLiveData = client.getUserLiveData();
    }

    public LiveData<String> responseMessageLiveData() {
        return App.responseMessage;
    }

    /***************
     * FRIEND LIST *
     ***************/

    private final MutableLiveData<List<User>> friendList = new MutableLiveData<>();

    public void setFriendList(List<User> friendList) {
        this.friendList.setValue(friendList);
    }

    public LiveData<List<User>> getFriendListLiveData() {
        return friendList;
    }

    public void listFriend() {
        client.sendMessage(RequestMessage.listFriend());
    }

    /*********************
     * JOINED GROUP LIST *
     *********************/

    private final MutableLiveData<List<Group>> joinedGroupList = new MutableLiveData<>();

    public void setJoinedGroupList(List<Group> groupList) {
        joinedGroupList.setValue(groupList);
    }

    public LiveData<List<Group>> getJoinedGroupListLiveData() {
        return joinedGroupList;
    }

    public void listJoinedGroup() {
        client.sendMessage(RequestMessage.listJoinedGroup());
    }

    public void createGroup(Group group) {
        client.sendMessage(RequestMessage.createGroup(group));
    }
}
