package com.example.chat_client.dialogs;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;

import androidx.appcompat.app.AlertDialog;

import com.bumptech.glide.Glide;
import com.example.chat_client.R;
import com.example.chat_client.databinding.DialogSendImageBinding;

public class DialogSendImage {

    private DialogSendImageBinding binding;
    private AlertDialog dialog;
    private final Context context;
    private final DialogButtonListener listener;

    public DialogSendImage(Context context, String title, Bitmap bitmap, DialogButtonListener listener) {
        this.context = context;
        this.listener = listener;
        init(title, bitmap);
    }

    private void init(String title, Bitmap bitmap) {
        buildDialog();
        setView(title, bitmap);
        clickButtonNegative();
        clickButtonPositive();
    }

    private void buildDialog() {
        binding = DialogSendImageBinding.inflate(LayoutInflater.from(context));

        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.CustomDialog);
        builder.setView(binding.getRoot());

        dialog = builder.create();
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
    }

    private void setView(String title, Bitmap bitmap) {
        binding.tvTitle.setText(title);
        Glide.with(context).load(bitmap).into(binding.ivImage);
    }

    private void clickButtonNegative() {
        binding.btnNegative.setOnClickListener(v -> {
            dialog.dismiss();
            listener.onNegativeClicked();
        });
    }

    private void clickButtonPositive() {
        binding.btnPositive.setOnClickListener(v -> {
            dialog.dismiss();
            listener.onPositiveClicked(null);
        });
    }

    public void show() {
        dialog.show();
    }
}
