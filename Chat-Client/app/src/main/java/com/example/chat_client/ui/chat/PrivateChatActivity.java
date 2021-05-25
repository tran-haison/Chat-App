package com.example.chat_client.ui.chat;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.example.chat_client.App;
import com.example.chat_client.adapters.list_view_adapter.MessageAdapter;
import com.example.chat_client.databinding.ActivityPrivateChatBinding;
import com.example.chat_client.models.Message;
import com.example.chat_client.models.User;
import com.example.chat_client.utils.CalendarUtil;
import com.example.chat_client.utils.Constants;
import com.example.chat_client.utils.MessageUtil;

import java.util.ArrayList;
import java.util.List;

import static com.example.chat_client.socket.ResponseMessage.FAIL_FRIENDMSG;
import static com.example.chat_client.socket.ResponseMessage.SUCCESS_FRIENDMSG;
import static com.example.chat_client.socket.ResponseMessage.SUCCESS_MESSAGE_FROM_FRIEND;

public class PrivateChatActivity extends AppCompatActivity {

    private ActivityPrivateChatBinding binding;
    private ChatViewModel viewModel;

    private User friend;
    private final User me = App.user.getValue();

    private List<Message> messages = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPrivateChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Get friend info
        getIntentChatFriend();

        // Setup ViewModel
        setupViewModel();

        // View init
        Glide.with(this).load(friend.getAvatar()).into(binding.ivAva);
        binding.tvUsername.setText(friend.getName());

        // View events
        binding.ibBack.setOnClickListener(v -> onBackPressed());
        binding.cvSendMessage.setOnClickListener(v -> sendMessage());
    }

    private void getIntentChatFriend() {
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra(Constants.BUNDLE);
        if (bundle != null) {
            friend = (User) bundle.getSerializable(Constants.FRIEND);
        }
    }

    private void setupViewModel() {
        viewModel = new ViewModelProvider(this).get(ChatViewModel.class);
        viewModel.responseLiveData().observe(this, this::handleServerResponse);
        viewModel.getMessageLiveData().observe(this, messages -> {
            this.messages = messages;
            initMessageRecyclerView();
        });
    }

    private void handleServerResponse(String serverMessage) {
        try {
            String responseType = MessageUtil.responseType(serverMessage);
            switch (responseType) {

                case SUCCESS_MESSAGE_FROM_FRIEND:
                    // TODO: check for message from current friend first
                    String messageFromFriend = MessageUtil.messageToChat(serverMessage);
                    Message friendMessage = new Message(messageFromFriend, friend, CalendarUtil.getCurrentTime());
                    messages.add(friendMessage);
                    viewModel.setMessages(messages);
                    break;

                case SUCCESS_FRIENDMSG:
                    String messageToFriend = binding.etChatMessage.getText().toString();
                    Message meMessage = new Message(messageToFriend, me, CalendarUtil.getCurrentTime());
                    messages.add(meMessage);
                    viewModel.setMessages(messages);
                    binding.etChatMessage.setText("");
                    break;

                case FAIL_FRIENDMSG:
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initMessageRecyclerView() {
        MessageAdapter messageAdapter = new MessageAdapter(this, messages);
        binding.rvChat.setLayoutManager(new LinearLayoutManager(this));
        binding.rvChat.setAdapter(messageAdapter);
    }

    private void sendMessage() {
        String chat = binding.etChatMessage.getText().toString();
        if (!chat.isEmpty()) {
            viewModel.friendMessage(friend, chat);
        }
    }
}