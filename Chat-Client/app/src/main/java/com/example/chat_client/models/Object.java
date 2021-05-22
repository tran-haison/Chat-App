package com.example.chat_client.models;

import com.example.chat_client.models.validators.NameAndPasswordValidator;

public class Object {

    protected String name;
    protected final NameAndPasswordValidator validator = new NameAndPasswordValidator();

    public Object(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isNameValid() {
        return validator.isNameValid(name);
    }
}
