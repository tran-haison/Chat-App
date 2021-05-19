package com.example.chat_client.models;

import java.io.Serializable;

public class Group implements Serializable {

    private String name;
    private int numberOfMember;

    public Group(String name, int numberOfMember) {
        this.name = name;
        this.numberOfMember = numberOfMember;
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
}
