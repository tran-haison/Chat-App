package com.example.chat_client.models;

import java.io.Serializable;

public class Group extends Object implements Serializable {

    private int numberOfMember;

    public Group(String name, int numberOfMember) {
        super(name);
        this.numberOfMember = numberOfMember;
    }

    public Group(String name) {
        super(name);
        this.numberOfMember = 0;
    }

    public int getNumberOfMember() {
        return numberOfMember;
    }

    public void setNumberOfMember(int numberOfMember) {
        this.numberOfMember = numberOfMember;
    }
}
