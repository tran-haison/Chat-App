package com.example.chat_client.dialogs;

import android.content.Context;

import com.example.chat_client.R;

public class DialogUtils {

    public static void dialogCreateGroup(
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
}
