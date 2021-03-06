package com.example.chat_client;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

import androidx.lifecycle.LiveData;

import com.example.chat_client.models.User;
import com.example.chat_client.socket.Client;

public class App extends Application {

    public static final String CHANNEL_ID = "NOTIFICATION CHAT CHANNEL";

    public static LiveData<String> responseMessage;
    public static LiveData<User> user;

    @Override
    public void onCreate() {
        super.onCreate();

        // Open socket
        Client client = Client.getInstance();
        responseMessage = client.responseMessageLiveData();
        user = client.userLiveData();

        // Create notification channel
        createNotificationChannel();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(
                    CHANNEL_ID,
                    getResources().getString(R.string.app_name),
                    NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(notificationChannel);
        }
    }

}
