package com.example.chat_client.ui.chat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.example.chat_client.App;
import com.example.chat_client.adapters.recyclerview.MessageAdapter;
import com.example.chat_client.databinding.ActivityPrivateChatBinding;
import com.example.chat_client.dialogs.DialogButtonListener;
import com.example.chat_client.dialogs.DialogUtils;
import com.example.chat_client.models.Message;
import com.example.chat_client.models.Object;
import com.example.chat_client.models.User;
import com.example.chat_client.utils.Constants;
import com.example.chat_client.utils.FileUtil;
import com.example.chat_client.utils.ImageUtil;
import com.example.chat_client.utils.IntentCall;
import com.example.chat_client.socket.MessageUtil;
import com.example.chat_client.utils.PermissionManager;
import com.google.android.material.snackbar.Snackbar;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Objects;

import static com.example.chat_client.models.Message.MessageType.RECEIVE;
import static com.example.chat_client.models.Message.MessageType.SEND;
import static com.example.chat_client.socket.Client.MESSAGE_SIZE_BUFFER;
import static com.example.chat_client.socket.ResponseMessage.FAIL_FRIENDMSG;
import static com.example.chat_client.socket.ResponseMessage.SUCCESS_FILE_FROM_FRIEND;
import static com.example.chat_client.socket.ResponseMessage.SUCCESS_FRIENDMSG;
import static com.example.chat_client.socket.ResponseMessage.SUCCESS_FRIEND_FILE;
import static com.example.chat_client.socket.ResponseMessage.SUCCESS_MESSAGE_FROM_FRIEND;
import static com.example.chat_client.utils.IntentCall.REQUEST_SELECT_IMAGE;

public class PrivateChatActivity extends AppCompatActivity {

    private ActivityPrivateChatBinding binding;
    private ChatViewModel viewModel;

    private User friend;
    private final User me = App.user.getValue();

    private MessageAdapter adapter;
    private Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPrivateChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Get friend info
        getIntentFriendInfo();

        // Setup ViewModel
        setupViewModel();

        // View implementation
        initMessageRecyclerView();
        setViewVisibility();
        Glide.with(this).load(friend.getAvatar()).into(binding.ivAva);
        binding.tvUsername.setText(friend.getName());

        // View events
        binding.ibBack.setOnClickListener(v -> onBackPressed());
        binding.cvSendMessage.setOnClickListener(v -> sendMessage());
        binding.ibImage.setOnClickListener(v -> checkReadPermission());
    }

    private void getIntentFriendInfo() {
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra(Constants.BUNDLE);
        if (bundle != null) {
            friend = (User) bundle.getSerializable(Constants.FRIEND);
        }
    }

    private void setupViewModel() {
        viewModel = new ViewModelProvider(this).get(ChatViewModel.class);
        viewModel.responseLiveData().observe(this, this::handleServerResponse);
    }

    private void initMessageRecyclerView() {
        binding.rvChat.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MessageAdapter(this, new ArrayList<>());
        binding.rvChat.setAdapter(adapter);
    }

    private void setViewVisibility() {
        if (adapter.getItemCount() == 0) {
            binding.rvChat.setVisibility(View.GONE);
            binding.llChatPrompt.setVisibility(View.VISIBLE);
            binding.lavGreet.playAnimation();
        } else {
            binding.rvChat.setVisibility(View.VISIBLE);
            binding.llChatPrompt.setVisibility(View.GONE);
        }
    }

    private void handleServerResponse(String serverMessage) {
        try {
            String responseType = MessageUtil.responseType(serverMessage);
            switch (responseType) {
                case SUCCESS_MESSAGE_FROM_FRIEND:
                    onMessageReceived(serverMessage);
                    break;
                case SUCCESS_FRIENDMSG:
                    onMessageSent();
                    break;
                case FAIL_FRIENDMSG:
                    Toast.makeText(this, FAIL_FRIENDMSG, Toast.LENGTH_LONG).show();
                    binding.etChatMessage.setText("");
                    break;
                case SUCCESS_FRIEND_FILE:
                    onFileSent();
                    break;
                case SUCCESS_FILE_FROM_FRIEND:
                    onFileReceived(serverMessage);
                    break;
            }
            setViewVisibility();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void onMessageSent() {
        String messageFromMe = Objects.requireNonNull(binding.etChatMessage.getText()).toString();
        Message myMessage = new Message(messageFromMe, me, SEND);
        addNewMessageToView(myMessage);
        binding.etChatMessage.setText("");
    }

    private void onMessageReceived(String serverMessage) {
        String senderName = MessageUtil.messageToName(serverMessage);
        if (senderName.equals(friend.getName())) {
            String messageFromFriend = MessageUtil.messageToChat(serverMessage);
            Message friendMessage = new Message(messageFromFriend, friend, RECEIVE);
            addNewMessageToView(friendMessage);
        }
    }

    private void onFileSent() {
        try {
            Bitmap imageBitmap = ImageUtil.getBitmapFromUri(this, uri);
            Message myMessage = new Message(me, SEND, imageBitmap);
            addNewMessageToView(myMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void onFileReceived(String serverMessage) {
        String senderName = MessageUtil.messageToName(serverMessage);
        if (senderName.equals(friend.getName())) {
            String imageBase64 = MessageUtil.messageToFile(serverMessage);
            Bitmap bitmap = ImageUtil.decodeBase64ToBitmap(imageBase64);
            Message friendMessage = new Message(friend, RECEIVE, bitmap);
            addNewMessageToView(friendMessage);
        }
    }

    private void addNewMessageToView(Message message) {
        adapter.addMessage(message);
        binding.rvChat.smoothScrollToPosition(adapter.getItemCount() - 1);
    }

    private void sendMessage() {
        String chat = Objects.requireNonNull(binding.etChatMessage.getText()).toString();
        if (!chat.isEmpty()) {
            viewModel.friendMessage(friend, chat);
        }
    }

    private void showImageSentDialog(Bitmap bitmap, String imageBase64) {
        DialogUtils.dialogSendImage(
                this,
                "Do you want to send this image?",
                bitmap,
                new DialogButtonListener() {
                    @Override
                    public void onNegativeClicked() {
                    }

                    @Override
                    public void onPositiveClicked(Object object) {
                        viewModel.friendFile(friend, imageBase64);
                    }
                });
    }

    private void checkReadPermission() {
        if (!PermissionManager.isReadPermissionGranted(this)) {
            PermissionManager.requestReadPermission(this);
        } else {
            IntentCall.selectImage(this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NotNull String[] permissions, @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (requestCode == PermissionManager.REQUEST_READ_EXTERNAL_STORAGE) {
                // Permission granted.
                IntentCall.selectImage(this);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            if (requestCode == REQUEST_SELECT_IMAGE) {
                uri = data.getData();
                try {
                    Bitmap bitmap = ImageUtil.getBitmapFromUri(this, uri);
                    byte[] imageBytes = ImageUtil.getBytesFromBitmap(bitmap);
                    long imageLength = imageBytes.length;
                    if (imageLength >= MESSAGE_SIZE_BUFFER) {
                        Snackbar.make(binding.getRoot(), "Please select image with size less than 100KB", Snackbar.LENGTH_LONG).show();
                    } else {
                        String imageBase64 = ImageUtil.encodeImageBytesToBase64(imageBytes);
                        showImageSentDialog(bitmap, imageBase64);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }
}