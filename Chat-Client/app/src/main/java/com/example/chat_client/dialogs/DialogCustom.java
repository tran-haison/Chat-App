package com.example.chat_client.dialogs;

import android.content.Context;
import android.view.LayoutInflater;

import androidx.appcompat.app.AlertDialog;

import com.example.chat_client.R;
import com.example.chat_client.databinding.DialogCustomBinding;
import com.example.chat_client.models.Object;

public class DialogCustom {

    private DialogCustomBinding binding;
    private AlertDialog dialog;
    private final Object object;
    private final Context context;
    private final DialogButtonListener listener;

    public DialogCustom(Context context, String title, String content, DialogButtonListener listener) {
        this.context = context;
        this.listener = listener;
        this.object = null;
        init(title, content);
    }

    public DialogCustom(Context context, Object object, String title, String content, DialogButtonListener listener) {
        this.context = context;
        this.listener = listener;
        this.object = object;
        init(title, content);
    }

    private void init(String title, String content) {
        buildDialog();
        setTextView(title, content);
        clickButtonNegative();
        clickButtonPositive();
    }

    private void buildDialog() {
        binding = DialogCustomBinding.inflate(LayoutInflater.from(context));

        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.CustomDialog);
        builder.setView(binding.getRoot());

        dialog = builder.create();
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
    }

    private void setTextView(String title, String content) {
        binding.tvTitle.setText(title);
        binding.tvContent.setText(content);
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
            listener.onPositiveClicked(object);
        });
    }

    public void show() {
        dialog.show();
    }
}
