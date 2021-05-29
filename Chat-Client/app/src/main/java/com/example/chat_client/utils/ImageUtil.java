package com.example.chat_client.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class ImageUtil {

    public static byte[] getBytesFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    public static Bitmap getBitmapFromUri(Context context, Uri imageUri) throws IOException {
        return MediaStore.Images.Media.getBitmap(context.getContentResolver(), imageUri);
    }

    public static Bitmap getBitmapFromPath(String file_path) {
        File imageFile = new File(file_path);
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(imageFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return BitmapFactory.decodeStream(fileInputStream);
    }

}
