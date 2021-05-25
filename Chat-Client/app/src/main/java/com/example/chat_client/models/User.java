package com.example.chat_client.models;

import com.example.chat_client.R;
import com.example.chat_client.models.validators.PasswordValidator;
import com.example.chat_client.models.validators.StringValidator;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class User extends Object implements Serializable {

    private final String password;

    private final StringValidator passwordValidator = new PasswordValidator();
    private static final Random random = new Random();

    // List of user avatars
    public static final List<Integer> avatars = Arrays.asList(
            R.drawable.img_user1,
            R.drawable.img_user2,
            R.drawable.img_user3,
            R.drawable.img_user4
    );

    public User(String name) {
        super(name);
        this.password = "";
        setAvatar(getRandomAvatar());
    }

    public User(String name, String password) {
        super(name);
        this.password = password;
        setAvatar(getRandomAvatar());
    }

    public String getPassword() {
        return password;
    }

    public boolean isPasswordValid() {
        return passwordValidator.isValid(password);
    }

    public static Integer getRandomAvatar() {
        int index = random.nextInt(avatars.size());
        return avatars.get(index);
    }

    @Override
    public String toString() {
        return name + " " + password;
    }
}
