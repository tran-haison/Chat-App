package com.example.chat_client.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

public class IntentCall {

    public static final int REQUEST_SELECT_IMAGE = 1;

    private IntentCall() {}

    public static void selectImage(Activity activity) {
        Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
        getIntent.setType("image/*");

        @SuppressLint("IntentReset") Intent pickIntent = new Intent(Intent.ACTION_PICK);
        pickIntent.setDataAndType(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");

        Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent});
        activity.startActivityForResult(chooserIntent, REQUEST_SELECT_IMAGE);
    }
}
