package com.example.chat_client.models.validators;

public class PasswordValidator implements StringValidator {
    @Override
    public boolean isValid(String value) {
        // Password can contain any character except space
        return value != null && !value.isEmpty() && !value.contains(" ");
    }
}
