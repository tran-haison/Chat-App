package com.example.chat_client.models;

import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Group implements Serializable, Validator {

    private String name;
    private int numberOfMember;

    public Group(String name, int numberOfMember) {
        this.name = name;
        this.numberOfMember = numberOfMember;
    }

    public Group(String name) {
        this.name = name;
        this.numberOfMember = 0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumberOfMember() {
        return numberOfMember;
    }

    public void setNumberOfMember(int numberOfMember) {
        this.numberOfMember = numberOfMember;
    }

    @Override
    public boolean isNameValid() {
        // Username can only contain a-z characters, number and underscore
        String regex = "^[A-Za-z]\\w{0,29}$";
        Pattern pattern = Pattern.compile(regex);

        if (name == null || name.isEmpty() || name.length() > 15) {
            return false;
        }

        Matcher matcher = pattern.matcher(name);
        return matcher.matches();
    }
}
