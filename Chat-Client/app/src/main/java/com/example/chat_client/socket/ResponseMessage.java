package com.example.chat_client.socket;

public class ResponseMessage {

    //public static final String SUCCESS = "SUCCESS";
    public static final String SUCCESS_SIGN_IN = "";
    public static final String SUCCESS_SIGN_UP = "";
    public static final String SUCCESS_SIGN_OUT = "";
    public static final String SUCCESS_UPDATE_USER = "";

    public static final String FAIL_USERNAME_ALREADY_EXIST = "FAIL USERNAME_ALREADY_EXIST";
    public static final String FAIL_USER_ALREADY_SIGN_IN = "FAIL USER_ALREADY_SIGN_IN";
    public static final String FAIL_INCORRECT_USERNAME_OR_PASSWORD = "FAIL INCORRECT_USERNAME_OR_PASSWORD";

    private ResponseMessage() {}
}
