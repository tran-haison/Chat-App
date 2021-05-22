package com.example.chat_client.models;

import java.io.Serializable;

public class User extends Object implements Serializable {

    private final String password;

    public User(String name, String password) {
        super(name);
        this.password = password;
    }

    public String getUsername() {
        return getName();
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return name + " " + password;
    }

    public boolean isPasswordValid() {
        return validator.isPasswordValid(password);
    }
}
