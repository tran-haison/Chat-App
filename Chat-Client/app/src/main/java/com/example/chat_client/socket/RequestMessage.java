package com.example.chat_client.socket;

import com.example.chat_client.models.Group;
import com.example.chat_client.models.User;

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

    public static String friendFile(User user, String file) {
        return "FRIEND_FILE " + user.getName() + " " + file;
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
