package com.example.chat_client.ui.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.chat_client.models.User;
import com.example.chat_client.ui.chat.PrivateChatActivity;
import com.example.chat_client.ui.search.SearchActivity;
import com.example.chat_client.utils.Constants;

public class MainActivityUtils {

    private final Context context;

    public MainActivityUtils(Context context) {
        this.context = context;
    }

    public void goToPrivateChatActivity(User chatFriend) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.FRIEND, chatFriend);
        Intent intent = new Intent(context, PrivateChatActivity.class);
        intent.putExtra(Constants.BUNDLE, bundle);
        context.startActivity(intent);
    }

    public void goToSearchActivity(String searchType) {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.SEARCH_TYPE, searchType);
        Intent intent = new Intent(context, SearchActivity.class);
        intent.putExtra(Constants.BUNDLE, bundle);
        context.startActivity(intent);
    }
}
