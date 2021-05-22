package com.example.chat_client.dialogs;

import com.example.chat_client.models.Object;

public interface DialogButtonListener {
    void onNegativeClicked();
    void onPositiveClicked(Object object);
}
