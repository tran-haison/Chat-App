package com.example.chat_client.socket;

import com.example.chat_client.models.Group;
import com.example.chat_client.models.User;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class RequestMessage {

    /***********
     * ACCOUNT *
     ***********/

    public static String signUp(User user) {
        return "SIGNUP " + user.toString();
    }

    public static String signIn(User user) {
        return "SIGNIN " + user.toString();
    }

    public static String signOut() {
        return "SIGNOUT";
    }

    public static String updateUser(User user) {
        return "UPDATE " + user.toString();
    }

    /**********
     * FRIEND *
     **********/

    public static String listFriend() {
        return "LIST_FRIEND";
    }

    public static String listActiveFriend() {
        return "LIST_ACTIVE_FRIEND";
    }

    public static String listFriendRequest() {
        return "LIST_FRIEND_REQUEST";
    }

    public static String searchFriend(String query) {
        return "SEARCH_FRIEND " + query;
    }

    public static String friendMessage(User user, String message) {
        return "FRIENDMSG " + user.getName() + " " + message;
    }

    public static byte[] friendFile(User user, byte[] file) {
        // Convert string to byte[]
        String protocol = "FRIEND_FILE " + user.getName() + " ";
        byte[] protocolBytes = protocol.getBytes(StandardCharsets.UTF_8);

        // Create new byte[] and copy 2 protocolBytes and file into messageByte
        byte[] messageBytes = new byte[protocolBytes.length + file.length];
        System.arraycopy(protocolBytes, 0, messageBytes, 0, protocolBytes.length);
        System.arraycopy(file, 0, messageBytes, protocolBytes.length, file.length);

        return messageBytes;
    }

    /*********
     * GROUP *
     *********/

    public static String createGroup(Group group) {
        return "CREATE " + group.getName();
    }

    public static String joinGroup(Group group) {
        return "JOIN " + group.getName();
    }

    public static String quitGroup(Group group) {
        return "QUIT " + group.getName();
    }

    public static String listNotJoinedGroup() {
        return "LIST_NOT_JOINED_GROUP";
    }

    public static String listJoinedGroup() {
        return "LIST_JOINED_GROUP";
    }

    public static String searchJoinedGroup(String query) {
        return "SEARCH_JOINED_GROUP " + query;
    }

    public static String searchNotJoinedGroup(String query) {
        return "SEARCH_NOT_JOINED_GROUP " + query;
    }

    public static String listAllMember(Group group) {
        return "LIST_ALL_MEMBER " + group.getName();
    }

    public static String groupMessage(Group group, String message) {
        return "GROUPMSG " + group.getName() + " " + message;
    }

    public static byte[] groupFile(Group group, byte[] file) {
        // Convert protocol string to byte[]
        String protocol = "GROUP_FILE " + group.getName() + " ";
        byte[] protocolBytes = protocol.getBytes(StandardCharsets.UTF_8);

        // Create new byte[] and copy 2 protocolBytes and file into messageByte
        byte[] messageBytes = new byte[protocolBytes.length + file.length];
        System.arraycopy(protocolBytes, 0, messageBytes, 0, protocolBytes.length);
        System.arraycopy(file, 0, messageBytes, protocolBytes.length, file.length);

        return messageBytes;
    }

    /**********
     * PEOPLE *
     **********/

    public static String listUser() {
        return "LIST_USER";
    }

    public static String searchUser(String query) {
        return "SEARCH_USER " + query;
    }

    public static String addFriend(User user) {
        return "ADD_FRIEND " + user.getName();
    }

    public static String acceptFriend(User user) {
        return "ACCEPT_FRIEND " + user.getName();
    }

    public static String denyRequest(User user) {
        return "DENY_REQUEST " + user.getName();
    }
}
