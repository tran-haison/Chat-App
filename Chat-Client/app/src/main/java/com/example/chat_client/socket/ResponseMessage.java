package com.example.chat_client.socket;

public class ResponseMessage {

    /***********
     * ACCOUNT *
     ***********/

    public static final String SUCCESS_SIGN_IN = "SUCCESS SIGNIN";
    public static final String SUCCESS_SIGN_UP = "SUCCESS SIGNUP";
    public static final String SUCCESS_SIGN_OUT = "SUCCESS SIGNOUT";
    public static final String SUCCESS_UPDATE = "SUCCESS UPDATE";
    public static final String FAIL_USERNAME_ALREADY_EXIST = "FAIL USERNAME_ALREADY_EXIST";
    public static final String FAIL_USER_ALREADY_SIGN_IN = "FAIL USER_ALREADY_SIGN_IN";
    public static final String FAIL_INCORRECT_USERNAME_OR_PASSWORD = "FAIL INCORRECT_USERNAME_OR_PASSWORD";

    /**********
     * FRIEND *
     **********/

    public static final String SUCCESS_LIST_FRIEND = "SUCCESS LIST_FRIEND";
    public static final String SUCCESS_LIST_ACTIVE_FRIEND = "SUCCESS LIST_ACTIVE_FRIEND";
    public static final String SUCCESS_LIST_FRIEND_REQUEST = "SUCCESS LIST_FRIEND_REQUEST";
    public static final String SUCCESS_SEARCH_FRIEND = "SUCCESS SEARCH_FRIEND";
    public static final String SUCCESS_FRIENDMSG = "SUCCESS FRIENDMSG";
    public static final String SUCCESS_MESSAGE_FROM_FRIEND = "SUCCESS MESSAGE_FROM_FRIEND";
    public static final String FAIL_FRIENDMSG = "FAIL FRIENDMSG";

    /*********
     * GROUP *
     *********/

    public static final String SUCCESS_CREATE = "SUCCESS CREATE";
    public static final String SUCCESS_JOIN = "SUCCESS JOIN";
    public static final String SUCCESS_NEW_JOIN = "SUCCESS NEW_JOIN";
    public static final String SUCCESS_QUIT = "SUCCESS QUIT";
    public static final String SUCCESS_NEW_QUIT = "SUCCESS NEW_QUIT";
    public static final String SUCCESS_LIST_NOT_JOINED_GROUP = "SUCCESS LIST_NOT_JOINED_GROUP";
    public static final String SUCCESS_LIST_JOINED_GROUP = "SUCCESS LIST_JOINED_GROUP";
    public static final String SUCCESS_SEARCH_JOINED_GROUP = "SUCCESS SEARCH_JOINED_GROUP";
    public static final String SUCCESS_SEARCH_NOT_JOINED_GROUP = "SUCCESS SEARCH_NOT_JOINED_GROUP";
    public static final String SUCCESS_LIST_ALL_MEMBER = "SUCCESS LIST_ALL_MEMBER";
    public static final String SUCCESS_GROUPMSG = "SUCCESS GROUPMSG";
    public static final String SUCCESS_MESSAGE_FROM_MEMBER = "SUCCESS MESSEAGE_FROM_MEMBER";
    public static final String FAIL_GROUP_ALREADY_EXIST = "FAIL GROUP_ALREADY_EXIST";
    public static final String FAIL_JOIN = "FAIL JOIN";
    public static final String FAIL_QUIT = "FAIL QUIT";
    public static final String FAIL_LIST_ALL_MEMBER = "FAIL LIST_ALL_MEMBER";
    public static final String FAIL_GROUPMSG = "FAIL GROUPMSG";

    /**********
     * PEOPLE *
     **********/

    public static final String SUCCESS_LIST_USER = "SUCCESS LIST_USER";
    public static final String SUCCESS_SEARCH_USER = "SUCCESS SEARCH_USER";
    public static final String SUCCESS_ADD_FRIEND = "SUCCESS ADD_FRIEND";
    public static final String SUCCESS_REQUEST_FROM = "SUCCESS REQUEST_FROM";
    public static final String SUCCESS_ACCEPT_FRIEND = "SUCCESS ACCEPT_FRIEND";
    public static final String SUCCESS_ACCEPT_FROM = "SUCCESS ACCEPT_FROM";
    public static final String SUCCESS_DENY_REQUEST = "SUCCESS DENY_REQUEST";
    public static final String FAIL_ADD_FRIEND = "FAIL ADD_FRIEND";
    public static final String FAIL_ACCEPT_FRIEND = "FAIL ACCEPT_FRIEND";
    public static final String FAIL_DENY_REQUEST = "FAIL DENY_REQUEST";
}
