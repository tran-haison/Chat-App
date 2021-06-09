package com.example.chat_client.socket;

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

    /**
     * Get list of users from server response message
     * @param message server message
     * @return list of users
     */
    public static List<User> messageToUsers(String message) {
        List<User> users = new ArrayList<>();
        List<String> tokens = splitMessage(message);

        for (int i = 0; i < tokens.size(); i++) {
            User user = new User(tokens.get(i));
            users.add(user);
        }

        return users;
    }

    /**
     * Get list of groups from server response message
     * @param message server message
     * @return list of groups
     */
    public static List<Group> messageToGroups(String message) {
        List<Group> groups = new ArrayList<>();
        List<String> tokens = splitMessage(message);

        for (int i = 0; i < tokens.size(); i++) {
            Group group = new Group(tokens.get(i));
            groups.add(group);
        }

        return groups;
    }

    /**
     * Get the chat string from server response message
     * @param message server message
     * @return chat
     */
    public static String messageToChat(String message) {
        String[] split = message.split("\\s+", 4);
        return split[3].trim();
    }

    /**
     * Get the file as base 64 string from server
     * @param message server message
     * @return file as base 64
     */
    public static String messageToFile(String message) {
        String[] split = message.split("\\s+", 4);
        return split[3].trim();
    }

    /**
     * Get the name of user or group
     * @param message server message
     * @return name of an user or a group
     */
    public static String messageToName(String message) {
        String[] split = message.split("\\s+", 4);
        return split[2].trim();
    }

}
