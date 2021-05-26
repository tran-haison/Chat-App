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
import com.example.chat_client.databinding.ActivityGroupChatBinding;
import com.example.chat_client.models.Group;
import com.example.chat_client.models.Message;
import com.example.chat_client.models.User;
import com.example.chat_client.utils.Constants;
import com.example.chat_client.utils.MessageUtil;

import java.util.ArrayList;
import java.util.Objects;

import static com.example.chat_client.socket.ResponseMessage.FAIL_GROUPMSG;
import static com.example.chat_client.socket.ResponseMessage.SUCCESS_GROUPMSG;
import static com.example.chat_client.socket.ResponseMessage.SUCCESS_MESSAGE_FROM_MEMBER;

public class GroupChatActivity extends AppCompatActivity {

    private ActivityGroupChatBinding binding;
    private ChatViewModel viewModel;

    private final User me = App.user.getValue();
    private Group group;

    private MessageAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGroupChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Get group info
        getIntentGroupInfo();

        // Setup ViewModel
        setupViewModel();

        // View init
        initMessageRecyclerView();
        Glide.with(this).load(group.getAvatar()).into(binding.ivAva);
        binding.tvGroupName.setText(group.getName());

        // View events
        binding.ibBack.setOnClickListener(v -> onBackPressed());
        binding.ibGroupInfo.setOnClickListener(v -> showGroupInfo());
        binding.cvSendMessage.setOnClickListener(v -> sendMessage());
    }

    private void getIntentGroupInfo() {
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra(Constants.BUNDLE);
        if (bundle != null) {
            group = (Group) bundle.getSerializable(Constants.GROUP);
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
                case SUCCESS_MESSAGE_FROM_MEMBER:
                    onMessageReceived(serverMessage);
                    break;
                case SUCCESS_GROUPMSG:
                    onMessageSent();
                    break;
                case FAIL_GROUPMSG:
                    Toast.makeText(this, FAIL_GROUPMSG, Toast.LENGTH_LONG).show();
                    binding.etChatMessage.setText("");
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void onMessageReceived(String serverMessage) {
        User member = new User(MessageUtil.senderOfMessage(serverMessage));
        String messageFromMember = MessageUtil.messageToChat(serverMessage);
        Message memberMessage = new Message(messageFromMember, member);
        adapter.addMessage(memberMessage);
    }

    private void onMessageSent() {
        String messageFromMe = Objects.requireNonNull(binding.etChatMessage.getText()).toString();
        Message myMessage = new Message(messageFromMe, me);
        adapter.addMessage(myMessage);
        binding.etChatMessage.setText("");
    }

    private void initMessageRecyclerView() {
        // Set reverse linear layout manager
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        binding.rvChat.setLayoutManager(linearLayoutManager);

        // Set adapter
        adapter = new MessageAdapter(this, new ArrayList<>());
        binding.rvChat.setAdapter(adapter);
    }

    private void showGroupInfo() {
        Toast.makeText(this, "Nothing", Toast.LENGTH_SHORT).show();
        // TODO
    }

    private void sendMessage() {
        String chat = Objects.requireNonNull(binding.etChatMessage.getText()).toString();
        if (!chat.isEmpty()) {
            viewModel.groupMessage(group, chat);
        }
    }
}