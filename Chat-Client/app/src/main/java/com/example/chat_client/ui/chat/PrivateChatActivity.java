package com.example.chat_client.ui.chat;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.example.chat_client.App;
import com.example.chat_client.adapters.list_view_adapter.MessageAdapter;
import com.example.chat_client.databinding.ActivityPrivateChatBinding;
import com.example.chat_client.models.Message;
import com.example.chat_client.models.User;
import com.example.chat_client.utils.Constants;
import com.example.chat_client.utils.MessageUtil;

import java.util.ArrayList;
import java.util.Objects;

import static com.example.chat_client.socket.ResponseMessage.FAIL_FRIENDMSG;
import static com.example.chat_client.socket.ResponseMessage.SUCCESS_FRIENDMSG;
import static com.example.chat_client.socket.ResponseMessage.SUCCESS_MESSAGE_FROM_FRIEND;

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
        String senderName = MessageUtil.senderOfMessage(serverMessage);
        if (senderName.equals(friend.getName())) {
            String messageFromFriend = MessageUtil.messageToChat(serverMessage);
            Message friendMessage = new Message(messageFromFriend, friend);
            adapter.addMessage(friendMessage);
            binding.rvChat.smoothScrollToPosition(adapter.getItemCount() - 1);
        }
    }

    private void onMessageSent() {
        String messageFromMe = Objects.requireNonNull(binding.etChatMessage.getText()).toString();
        Message myMessage = new Message(messageFromMe, me);
        adapter.addMessage(myMessage);
        binding.rvChat.smoothScrollToPosition(adapter.getItemCount() - 1);
        binding.etChatMessage.setText("");
    }

    private void initMessageRecyclerView() {
        binding.rvChat.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MessageAdapter(this, new ArrayList<>());
        binding.rvChat.setAdapter(adapter);
    }

    private void sendMessage() {
        String chat = Objects.requireNonNull(binding.etChatMessage.getText()).toString();
        if (!chat.isEmpty()) {
            viewModel.friendMessage(friend, chat);
        }
    }
}