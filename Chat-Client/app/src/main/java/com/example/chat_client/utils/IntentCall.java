package com.example.chat_client.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

public class IntentCall {

    public static final int REQUEST_SELECT_IMAGE = 1;
    public static final int REQUEST_SELECT_FILE = 2;

    private IntentCall() {
    }

    public static void selectImage(Activity activity) {
        Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
        getIntent.setType("image/*");

        @SuppressLint("IntentReset") Intent pickIntent = new Intent(Intent.ACTION_PICK);
        pickIntent.setDataAndType(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");

        Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent});
        activity.startActivityForResult(chooserIntent, REQUEST_SELECT_IMAGE);
    }

    public static void selectFile(Activity activity) {
        Intent fileManager = new Intent(Intent.ACTION_GET_CONTENT);
        fileManager.setType("*/*");
        activity.startActivityForResult(fileManager, REQUEST_SELECT_FILE);
    }

//    public String onResultSelectFile(Intent data) {
//        Uri uri = data.getData();
//        String path = FileUtil.getPath(uri);
//        String filename = path.substring(path.lastIndexOf("/") + 1);
//        return path;
//    }
}
