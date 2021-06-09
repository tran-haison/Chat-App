package com.example.chat_client.ui.chat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.example.chat_client.App;
import com.example.chat_client.adapters.recyclerview.MessageAdapter;
import com.example.chat_client.databinding.ActivityGroupChatBinding;
import com.example.chat_client.models.Group;
import com.example.chat_client.models.Message;
import com.example.chat_client.models.User;
import com.example.chat_client.ui.group.GroupInfoActivity;
import com.example.chat_client.utils.Constants;
import com.example.chat_client.socket.MessageUtil;
import com.google.android.material.snackbar.Snackbar;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.example.chat_client.models.Message.MessageType.JOIN;
import static com.example.chat_client.models.Message.MessageType.QUIT;
import static com.example.chat_client.models.Message.MessageType.RECEIVE;
import static com.example.chat_client.models.Message.MessageType.SEND;
import static com.example.chat_client.socket.ResponseMessage.FAIL_GROUPMSG;
import static com.example.chat_client.socket.ResponseMessage.FAIL_LIST_ALL_MEMBER;
import static com.example.chat_client.socket.ResponseMessage.SUCCESS_GROUPMSG;
import static com.example.chat_client.socket.ResponseMessage.SUCCESS_LIST_ALL_MEMBER;
import static com.example.chat_client.socket.ResponseMessage.SUCCESS_MESSAGE_FROM_MEMBER;
import static com.example.chat_client.socket.ResponseMessage.SUCCESS_NEW_JOIN;
import static com.example.chat_client.socket.ResponseMessage.SUCCESS_NEW_QUIT;

public class GroupChatActivity extends AppCompatActivity {

    private ActivityGroupChatBinding binding;
    private ChatViewModel viewModel;

    private final User me = App.user.getValue();
    private Group group;
    private List<User> members;

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

        // View implementation
        initMessageRecyclerView();
        setViewVisibility();
        Glide.with(this).load(group.getAvatar()).into(binding.ivAva);
        binding.tvGroupName.setText(group.getName());

        // View events
        binding.ibBack.setOnClickListener(v -> onBackPressed());
        binding.ibGroupInfo.setOnClickListener(v -> goToGroupInfoActivity());
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
        viewModel.listAllMembers(group);
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
                case SUCCESS_LIST_ALL_MEMBER:
                    onListAllMembers(serverMessage);
                    break;
                case FAIL_LIST_ALL_MEMBER:
                    Snackbar.make(binding.getRoot(), FAIL_LIST_ALL_MEMBER, Snackbar.LENGTH_LONG).show();
                    break;
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
                case SUCCESS_NEW_JOIN:
                    onNewMemberJoined(serverMessage);
                    break;
                case SUCCESS_NEW_QUIT:
                    onNewMemberQuit(serverMessage);
                    break;
            }
            setViewVisibility();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void onListAllMembers(String serverMessage) {
        // Init list view of all members
        if (members == null || members.size() == 0) {
            members = MessageUtil.messageToUsers(serverMessage);
        }
    }

    private void onMessageSent() {
        // Init message instance with chat message and current user
        String messageFromMe = Objects.requireNonNull(binding.etChatMessage.getText()).toString();
        Message myMessage = new Message(messageFromMe, me, SEND);

        // Add new message to recycler view
        addNewMessageToView(myMessage);
        binding.etChatMessage.setText("");
    }

    private void onMessageReceived(String serverMessage) {
        // Init message instance with chat message and sender of it
        User member = getSenderOfMessage(serverMessage);
        String messageFromMember = MessageUtil.messageToChat(serverMessage);
        Message memberMessage = new Message(messageFromMember, member, RECEIVE);

        // Add new message to recycler view
        addNewMessageToView(memberMessage);
    }

    private void onNewMemberJoined(String serverMessage) {
        // Create new user instance and join prompt message
        String member_name = MessageUtil.messageToName(serverMessage);
        User newMember = new User(member_name);
        Message joinMessage = new Message(newMember, JOIN);

        // Add new member
        members.add(newMember);
        addNewMessageToView(joinMessage);
    }

    private void onNewMemberQuit(String serverMessage) {
        String member_name = MessageUtil.messageToName(serverMessage);
        for (User member : members) {
            if (member.getName().equals(member_name)) {
                // Add new quit message to view
                Message quitMessage = new Message(member, QUIT);
                addNewMessageToView(quitMessage);

                // Remove current member
                members.remove(member);

                return;
            }
        }
    }

    private User getSenderOfMessage(String serverMessage) {
        String sender_name = MessageUtil.messageToName(serverMessage);
        for (User member : members) {
            if (member.getName().equals(sender_name)) {
                return member;
            }
        }
        return new User(sender_name);
    }

    private void addNewMessageToView(Message message) {
        adapter.addMessage(message);
        binding.rvChat.smoothScrollToPosition(adapter.getItemCount() - 1);
    }

    private void sendMessage() {
        String chat = Objects.requireNonNull(binding.etChatMessage.getText()).toString();
        if (!chat.isEmpty()) {
            viewModel.groupMessage(group, chat);
        }
    }

    private void goToGroupInfoActivity() {
        // Put group instance and list of members through intent
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.GROUP, group);
        bundle.putSerializable(Constants.MEMBERS, (Serializable) members);

        Intent intent = new Intent(this, GroupInfoActivity.class);
        intent.putExtra(Constants.BUNDLE, bundle);
        startActivity(intent);
    }
}