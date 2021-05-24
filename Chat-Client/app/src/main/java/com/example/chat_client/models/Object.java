package com.example.chat_client.models;

import com.example.chat_client.models.validators.NameValidator;
import com.example.chat_client.models.validators.StringValidator;

public class Object {

    protected String name;
    protected final StringValidator nameValidator = new NameValidator();

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
        return nameValidator.isValid(name);
    }
}
