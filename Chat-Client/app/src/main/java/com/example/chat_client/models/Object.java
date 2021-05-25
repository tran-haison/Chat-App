package com.example.chat_client.models;

import com.example.chat_client.models.validators.NameValidator;
import com.example.chat_client.models.validators.StringValidator;

import java.io.Serializable;

public class Object implements Serializable {

    protected String name;
    protected Integer avatar;

    protected final StringValidator nameValidator = new NameValidator();

    public Object(String name) {
        this.name = name;
        this.avatar = null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAvatar() {
        return avatar;
    }

    public void setAvatar(Integer avatar) {
        this.avatar = avatar;
    }

    public boolean isNameValid() {
        return nameValidator.isValid(name);
    }
}
