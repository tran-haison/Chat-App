package com.example.chat_client.dialogs;

import android.content.Context;
import android.graphics.Bitmap;

import com.example.chat_client.R;
import com.example.chat_client.models.Object;

public class DialogUtils {

    public static void dialogCreateObject(
            Context context,
            DialogButtonListener listener
    ) {
        DialogCreateObject dialog = new DialogCreateObject(
                context,
                context.getResources().getString(R.string.create_group),
                context.getResources().getString(R.string.enter_group_name),
                listener
        );
        dialog.show();
    }

    public static void dialogCustom(
            Context context,
            Object object,
            String content,
            DialogButtonListener listener
    ) {
        DialogCustom dialog = new DialogCustom(
                context,
                object,
                object.getName(),
                content,
                listener
        );
        dialog.show();
    }

    public static void dialogSendImage(
            Context context,
            String title,
            Bitmap bitmap,
            DialogButtonListener listener
    ) {
        DialogSendImage dialog = new DialogSendImage(
                context,
                title,
                bitmap,
                listener
        );
        dialog.show();
    }
}
