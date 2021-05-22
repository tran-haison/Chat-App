package com.example.chat_client.dialogs;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.example.chat_client.R;
import com.example.chat_client.databinding.DialogCreateBinding;
import com.example.chat_client.models.Object;
import com.example.chat_client.utils.Constants;

import java.util.Objects;

public class DialogCreateObject {

    private DialogCreateBinding binding;
    private AlertDialog dialog;
    private final Context context;
    private final DialogButtonListener listener;

    public DialogCreateObject(Context context, String title, String content, DialogButtonListener listener) {
        this.context = context;
        this.listener = listener;
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
            Object object = new Object(name);
            if (object.isNameValid()) {
                dialog.dismiss();
                listener.onPositiveClicked(object);
            } else {
                Toast.makeText(context, Constants.NAME_INVALID, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void show() {
        dialog.show();
    }

}
