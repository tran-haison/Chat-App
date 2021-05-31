package com.example.chat_client.receivers;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.navigation.NavDeepLinkBuilder;

import com.example.chat_client.R;
import com.example.chat_client.models.Object;
import com.example.chat_client.ui.chat.PrivateChatActivity;
import com.example.chat_client.ui.main.MainActivity;
import com.example.chat_client.utils.Constants;

import static com.example.chat_client.App.CHANNEL_ID;

public class NotificationReceiver extends BroadcastReceiver {

    public static final int FRIEND_REQUEST = 1;
    public static final int FRIEND_ACCEPT = 2;
    public static final int FRIEND_MESSAGE = 3;

    @Override
    public void onReceive(Context context, Intent intent) {
        // Get intent data
        Bundle bundle = intent.getBundleExtra(Constants.BUNDLE);
        int notificationType = bundle.getInt(Constants.NOTIFICATION_TYPE);
        Object object = (Object) bundle.getSerializable(Constants.OBJECT);

        // Create notification builder
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_round_notifications_24)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        // Get pending intent based on notification type
        PendingIntent pendingIntent;
        switch (notificationType) {
            case FRIEND_REQUEST:
                builder.setContentTitle("NEW FRIEND REQUEST");
                builder.setContentText(object.getName() + " wants to be your friend!");
                pendingIntent = seeFriendRequest(context);
                break;
            case FRIEND_ACCEPT:
                builder.setContentTitle("NEW FRIEND");
                builder.setContentText(object.getName() + " has accepted your friend request!");
                pendingIntent = chatWithFriend(context, object);
                break;
            case FRIEND_MESSAGE:
                builder.setContentTitle("NEW MESSAGE");
                builder.setContentText("New message from " + object.getName());
                pendingIntent = chatWithFriend(context, object);
                break;
            default:
                return;
        }

        builder.setContentIntent(pendingIntent);
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(0, builder.build());
    }

    private PendingIntent seeFriendRequest(Context context) {
        return new NavDeepLinkBuilder(context)
                .setComponentName(MainActivity.class)
                .setGraph(R.navigation.main_nav_graph)
                .setDestination(R.id.friendRequestFragment)
                .createPendingIntent();
    }

    private PendingIntent chatWithFriend(Context context, Object object) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.FRIEND, object);
        Intent intent = new Intent(context, PrivateChatActivity.class);
        intent.putExtra(Constants.BUNDLE, bundle);
        return PendingIntent.getActivity(context, 0, intent, 0);
    }

}
