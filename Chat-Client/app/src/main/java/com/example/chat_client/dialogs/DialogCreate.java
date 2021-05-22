package com.example.chat_client.dialogs;

import android.content.Context;
import android.view.LayoutInflater;

import androidx.appcompat.app.AlertDialog;

import com.example.chat_client.R;
import com.example.chat_client.databinding.DialogCreateBinding;

import java.util.Objects;

public class DialogCreate {

    private DialogCreateBinding binding;
    private AlertDialog dialog;
    private final Context context;
    private ButtonListener listener;

    public DialogCreate(Context context, String title, String content) {
        this.context = context;
        buildDialog();
        setTextView(title, content);
        clickButtonNegative();
        clickButtonPositive();
    }

    private void buildDialog() {
        binding = DialogCreateBinding.inflate(LayoutInflater.from(context));

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
            String name = Objects.requireNonNull(binding.etName.getText()).toString();
            listener.onPositiveClicked(name);
        });
    }

    public void show() {
        dialog.show();
    }

    public void dismiss() {
        dialog.dismiss();
    }

    public void setButtonListener(ButtonListener listener) {
        this.listener = listener;
    }

    public interface ButtonListener {
        void onNegativeClicked();
        void onPositiveClicked(String name);
    }

}
