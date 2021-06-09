package com.example.chat_client.ui.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.example.chat_client.models.Group;
import com.example.chat_client.models.Object;
import com.example.chat_client.models.User;
import com.example.chat_client.receivers.NotificationReceiver;
import com.example.chat_client.ui.chat.GroupChatActivity;
import com.example.chat_client.ui.chat.PrivateChatActivity;
import com.example.chat_client.ui.search.SearchActivity;
import com.example.chat_client.utils.Constants;
import com.example.chat_client.socket.MessageUtil;

import static com.example.chat_client.receivers.NotificationReceiver.FRIEND_ACCEPT;
import static com.example.chat_client.receivers.NotificationReceiver.FRIEND_MESSAGE;
import static com.example.chat_client.receivers.NotificationReceiver.FRIEND_REQUEST;
import static com.example.chat_client.socket.ResponseMessage.SUCCESS_ACCEPT_FROM;
import static com.example.chat_client.socket.ResponseMessage.SUCCESS_MESSAGE_FROM_FRIEND;
import static com.example.chat_client.socket.ResponseMessage.SUCCESS_REQUEST_FROM;

public class MainActivityUtils {

    private final Activity activity;

    public MainActivityUtils(Activity activity) {
        this.activity = activity;
    }

    public void goToPrivateChatActivity(User user) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.FRIEND, user);
        Intent intent = new Intent(activity, PrivateChatActivity.class);
        intent.putExtra(Constants.BUNDLE, bundle);
        activity.startActivity(intent);
    }

    public void goToGroupChatActivity(Group group) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.GROUP, group);
        Intent intent = new Intent(activity, GroupChatActivity.class);
        intent.putExtra(Constants.BUNDLE, bundle);
        activity.startActivity(intent);
    }

    public void goToSearchActivity(String searchType) {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.SEARCH_TYPE, searchType);
        Intent intent = new Intent(activity, SearchActivity.class);
        intent.putExtra(Constants.BUNDLE, bundle);
        activity.startActivity(intent);
    }

    public void handleServerResponse(String serverResponse) {
        try {
            // Create object instance
            String username = MessageUtil.messageToName(serverResponse);
            Object object = new User(username);

            // Create bundle to store data
            Bundle bundle = new Bundle();
            bundle.putSerializable(Constants.OBJECT, object);

            // Handle server response type
            String responseType = MessageUtil.responseType(serverResponse);
            switch (responseType) {
                case SUCCESS_REQUEST_FROM:
                    bundle.putInt(Constants.NOTIFICATION_TYPE, FRIEND_REQUEST);
                    break;
                case SUCCESS_ACCEPT_FROM:
                    bundle.putInt(Constants.NOTIFICATION_TYPE, FRIEND_ACCEPT);
                    break;
                case SUCCESS_MESSAGE_FROM_FRIEND:
                    bundle.putInt(Constants.NOTIFICATION_TYPE, FRIEND_MESSAGE);
                    break;
            }

            Intent intent = new Intent(activity, NotificationReceiver.class);
            intent.putExtra(Constants.BUNDLE, bundle);
            activity.sendBroadcast(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }}
