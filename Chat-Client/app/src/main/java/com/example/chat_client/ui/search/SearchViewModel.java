package com.example.chat_client.ui.search;

import androidx.lifecycle.ViewModel;

import com.example.chat_client.socket.Client;
import com.example.chat_client.socket.RequestMessage;

public class SearchViewModel extends ViewModel {

    private final Client client;

    public SearchViewModel() {
        client = Client.getInstance();
    }

    public void searchFriend(String query) {
        client.sendMessage(RequestMessage.searchFriend(query));
    }
}
