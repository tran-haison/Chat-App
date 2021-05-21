package com.example.chat_client.models;

import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class User implements Serializable {

    private final String username;
    private final String password;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public boolean isUsernameValid() {
        // Username can only contain a-z characters, number and underscore
        String regex = "^[A-Za-z]\\w{0,29}$";
        Pattern pattern = Pattern.compile(regex);

        if (username == null || username.isEmpty() || username.length() > 15) {
            return false;
        }

        Matcher matcher = pattern.matcher(username);
        return matcher.matches();
    }

    public boolean isPasswordValid() {
        // Password can contain any character except space
        return password != null && !password.isEmpty() && !password.contains(" ");
    }

    @Override
    public String toString() {
        return username + " " + password;
    }
}
