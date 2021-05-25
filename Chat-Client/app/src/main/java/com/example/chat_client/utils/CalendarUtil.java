package com.example.chat_client.utils;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CalendarUtil {

    public static String getCurrentTime() {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm");
        Date date = new Date();
        return timeFormat.format(date);
    }

}
