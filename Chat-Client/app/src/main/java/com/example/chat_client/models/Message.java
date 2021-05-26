package com.example.chat_client.models;

import com.example.chat_client.utils.CalendarUtil;

public class Message {

    private String message;
    private Object object;
    private String createAt;

    public Message(String message, Object object) {
        this.message = message;
        this.object = object;
        this.createAt = CalendarUtil.getCurrentTime();
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

    public void setCreateAt(String createAt) {
        this.createAt = createAt;
    }
}
