package com.example.chat_client.models.validators;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NameValidator implements StringValidator {

    @Override
    public boolean isValid(String value) {
        // Username can only contain a-z characters, number and underscore
        String regex = "^[A-Za-z]\\w{0,29}$";
        Pattern pattern = Pattern.compile(regex);

        if (value == null || value.isEmpty() || value.length() > 15) {
            return false;
        }

        Matcher matcher = pattern.matcher(value);
        return matcher.matches();
    }
}
