package com.example.chat_client.socket;

import com.example.chat_client.models.User;

public class RequestMessage {

    private RequestMessage() {}

    public static String signUp(User user) {
        return "SIGNUP " + user.getUsername() + " " + user.getPassword();
    }

    public static String signIn(User user) {
        return "SIGNIN " + user.getUsername() + " " + user.getPassword();
    }
}
