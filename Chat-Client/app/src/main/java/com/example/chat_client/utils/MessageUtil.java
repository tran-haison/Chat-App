package com.example.chat_client.utils;

import com.example.chat_client.models.Object;
import com.example.chat_client.models.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MessageUtil {

    /**
     * Split the server response message into tokens
     * Get the first 2 tokens to get the response type
     * @param message server response
     * @return response type
     */
    public static String responseType(String message) {
        String[] split = message.split("\\s+");
        return split[0] + " " + split[1];
    }

    /**
     * Convert list of string to list of objects
     * @param message server response message
     * @return list of objects
     */
    public static List<Object> messageToObjects(String message) {
        List<Object> objects = new ArrayList<>();
        List<String> tokens = splitMessage(message);

        for (int i = 0; i < tokens.size(); i++) {
            User user = new User(tokens.get(i), "");
            objects.add(user);
        }

        return objects;
    }

    /**
     * Get list of string which can be username or group name
     * after removing response type (first 2 tokens)
     * @param message server response message
     * @return list of tokens
     */
    private static List<String> splitMessage(String message) {
        String[] split = message.split("\\s+");
        return new ArrayList<>(Arrays.asList(split).subList(2, split.length));
    }
}
