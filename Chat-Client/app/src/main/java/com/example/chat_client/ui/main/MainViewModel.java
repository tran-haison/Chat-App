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

    private final MutableLiveData<List<User>> friendListMutableLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<Group>> groupListMutableLiveData = new MutableLiveData<>();

    public MainViewModel() {
        client = Client.getInstance();
        userLiveData = client.getUserLiveData();
    }

    /*********************
     * Request to server *
     *********************/

    public void listFriend() {
        client.sendMessage(RequestMessage.listFriend());
    }

    public void listActiveFriend() {
        client.sendMessage(RequestMessage.listActiveFriend());
    }

    public void listFriendRequest() {
        client.sendMessage(RequestMessage.listFriendRequest());
    }

    public LiveData<String> getResponseMessageLiveData() {
        return App.responseMessage;
    }

    /**********************************************************************************************/

    public void setFriendList(List<User> friendList) {
        friendListMutableLiveData.setValue(friendList);
    }

    public LiveData<List<User>> getFriendListLiveData() {
        return friendListMutableLiveData;
    }

    public void setGroupList(List<Group> groupList) {
        groupListMutableLiveData.setValue(groupList);
    }

    public LiveData<List<Group>> getGroupListLiveData() {
        return groupListMutableLiveData;
    }


}
