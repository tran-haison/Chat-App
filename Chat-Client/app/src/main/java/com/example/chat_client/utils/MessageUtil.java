package com.example.chat_client.utils;

import com.example.chat_client.models.Group;
import com.example.chat_client.models.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MessageUtil {

    /**
     * Get list of string which can be username or group name
     * after removing response type (first 2 tokens)
     *
     * @param message server response message
     * @return list of tokens
     */
    private static List<String> splitMessage(String message) {
        String[] split = message.split("\\s+");
        return new ArrayList<>(Arrays.asList(split).subList(2, split.length));
    }

    /**
     * Split the server response message into tokens
     * Get the first 2 tokens to get the response type
     *
     * @param message server response
     * @return response type
     */
    public static String responseType(String message) {
        String[] split = message.split("\\s+", 3);
        return split[0] + " " + split[1];
    }

    public static List<User> messageToUsers(String message) {
        List<User> users = new ArrayList<>();
        List<String> tokens = splitMessage(message);

        for (int i = 0; i < tokens.size(); i++) {
            User user = new User(tokens.get(i));
            users.add(user);
        }

        return users;
    }

    public static List<Group> messageToGroups(String message) {
        List<Group> groups = new ArrayList<>();
        List<String> tokens = splitMessage(message);

        for (int i = 0; i < tokens.size(); i++) {
            Group group = new Group(tokens.get(i));
            groups.add(group);
        }

        return groups;
    }

    public static String messageToChat(String message) {
        String[] split = message.split("\\s+", 4);
        return split[3].trim();
    }

    public static String senderOfMessage(String message) {
        String[] split = message.split("\\s+", 4);
        return split[2].trim();
    }

}
