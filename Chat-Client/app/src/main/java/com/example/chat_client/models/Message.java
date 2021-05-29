package com.example.chat_client.models;

import android.graphics.Bitmap;

import com.example.chat_client.utils.CalendarUtil;

public class Message {

    public enum MessageType {
        SEND,
        RECEIVE,
        JOIN,
        QUIT
    }

    private String message;
    private Object object;
    private final String createAt;
    private final MessageType messageType;
    private Bitmap imageBitmap;

    public Message(String message, Object object, MessageType messageType, Bitmap imageBitmap) {
        this.message = message;
        this.object = object;
        this.messageType = messageType;
        this.createAt = CalendarUtil.getCurrentTime();
        this.imageBitmap = imageBitmap;
    }

    public Message(String message, Object object, MessageType messageType) {
        this.message = message;
        this.object = object;
        this.messageType = messageType;
        this.createAt = CalendarUtil.getCurrentTime();
        this.imageBitmap = null;
    }

    public Message(Object object, MessageType messageType) {
        this.message = "";
        this.object = object;
        this.messageType = messageType;
        this.createAt = CalendarUtil.getCurrentTime();
        this.imageBitmap = null;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(User object) {
        this.object = object;
    }

    public String getCreateAt() {
        return createAt;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public Bitmap getImageBitmap() {
        return imageBitmap;
    }

    public void setImageBitmap(Bitmap imageBitmap) {
        this.imageBitmap = imageBitmap;
    }
}
