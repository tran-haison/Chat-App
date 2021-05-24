package com.example.chat_client.models;

import com.example.chat_client.models.validators.PasswordValidator;
import com.example.chat_client.models.validators.StringValidator;

import java.io.Serializable;

public class User extends Object implements Serializable {

    private final String password;
    private final StringValidator passwordValidator = new PasswordValidator();

    public User(String name, String password) {
        super(name);
        this.password = password;
    }

    public User(String name) {
        super(name);
        this.password = "";
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return name + " " + password;
    }

    public boolean isPasswordValid() {
        return passwordValidator.isValid(password);
    }
}
