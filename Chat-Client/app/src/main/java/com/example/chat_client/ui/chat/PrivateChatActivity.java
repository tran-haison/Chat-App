package com.example.chat_client.ui.chat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.example.chat_client.App;
import com.example.chat_client.adapters.list_view_adapter.MessageAdapter;
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
import com.example.chat_client.utils.MessageUtil;
import com.example.chat_client.utils.PermissionManager;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

import static com.example.chat_client.models.Message.MessageType.RECEIVE;
import static com.example.chat_client.models.Message.MessageType.SEND;
import static com.example.chat_client.socket.ResponseMessage.FAIL_FRIENDMSG;
import static com.example.chat_client.socket.ResponseMessage.SUCCESS_FRIENDMSG;
import static com.example.chat_client.socket.ResponseMessage.SUCCESS_MESSAGE_FROM_FRIEND;
import static com.example.chat_client.utils.IntentCall.REQUEST_SELECT_IMAGE;

public class PrivateChatActivity extends AppCompatActivity {

    private ActivityPrivateChatBinding binding;
    private ChatViewModel viewModel;

    private User friend;
    private final User me = App.user.getValue();

    private MessageAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPrivateChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Get friend info
        getIntentFriendInfo();

        // Setup ViewModel
        setupViewModel();

        // View init
        initMessageRecyclerView();
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
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void onMessageReceived(String serverMessage) {
        String senderName = MessageUtil.messageToName(serverMessage);
        if (senderName.equals(friend.getName())) {
            String messageFromFriend = MessageUtil.messageToChat(serverMessage);
            Message friendMessage = new Message(messageFromFriend, friend, RECEIVE);
            adapter.addMessage(friendMessage);
            binding.rvChat.smoothScrollToPosition(adapter.getItemCount() - 1);
        }
    }

    private void onMessageSent() {
        String messageFromMe = Objects.requireNonNull(binding.etChatMessage.getText()).toString();
        Message myMessage = new Message(messageFromMe, me, SEND);
        adapter.addMessage(myMessage);
        binding.rvChat.smoothScrollToPosition(adapter.getItemCount() - 1);
        binding.etChatMessage.setText("");
    }

    private void sendMessage() {
        String chat = Objects.requireNonNull(binding.etChatMessage.getText()).toString();
        if (!chat.isEmpty()) {
            viewModel.friendMessage(friend, chat);
        }
    }

    private void getImageData(Uri uri) {
        // Get image path and name
        String path = FileUtil.getPath(this, uri);
        assert path != null;
        String filename = path.substring(path.lastIndexOf("/") + 1);

        // Convert image from bitmap to byte[]
        Bitmap bitmap = ImageUtil.getBitmapFromPath(path);
        byte[] image_bytes = ImageUtil.getBytesFromBitmap(bitmap);

        showImageSentDialog(filename, bitmap, image_bytes);
    }

    private void showImageSentDialog(String filename, Bitmap bitmap, byte[] image_bytes) {
        DialogUtils.dialogSendImage(
                this,
                filename,
                bitmap,
                new DialogButtonListener() {
                    @Override
                    public void onNegativeClicked() {
                    }

                    @Override
                    public void onPositiveClicked(Object object) {
                        viewModel.friendFile(me, image_bytes);
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
                // Get image path and filename from uri
                Uri uri = data.getData();
                try {
                    getImageData(uri);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }
}