package com.example.chat_client.models;

import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class User implements Serializable {

    private String username;
    private String password;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isUsernameValid() {
        // Username can only contain a-z characters, number and underscore
        String regex = "^[A-Za-z]\\w{0,29}$";
        Pattern pattern = Pattern.compile(regex);

        if (username == null || username.isEmpty()) {
            return false;
        }

        Matcher matcher = pattern.matcher(username);
        return matcher.matches();
    }

    public boolean isPasswordValid() {
        // Password can contain any character except space
        return password != null && !password.isEmpty() && !password.contains(" ");
    }
}
