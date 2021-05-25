package com.example.chat_client.models;

import com.example.chat_client.R;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Group extends Object implements Serializable {

    private int numberOfMember;

    private static final Random random = new Random();

    // List of group avatars
    public static final List<Integer> avatars = Arrays.asList(
            R.drawable.img_group1,
            R.drawable.img_group2,
            R.drawable.img_group3,
            R.drawable.img_group4
    );

    public Group(String name) {
        super(name);
        this.numberOfMember = 0;
        setAvatar(getRandomAvatar());
    }

    public Group(String name, int numberOfMember) {
        super(name);
        this.numberOfMember = numberOfMember;
        setAvatar(getRandomAvatar());
    }

    public int getNumberOfMember() {
        return numberOfMember;
    }

    public void setNumberOfMember(int numberOfMember) {
        this.numberOfMember = numberOfMember;
    }

    public static Integer getRandomAvatar() {
        int index = random.nextInt(avatars.size());
        return avatars.get(index);
    }
}
