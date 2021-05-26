package com.example.chat_client.adapters.list_view_adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.chat_client.App;
import com.example.chat_client.databinding.ItemGroupMemberChangeBinding;
import com.example.chat_client.databinding.ItemMessageReceiveBinding;
import com.example.chat_client.databinding.ItemMessageSendBinding;
import com.example.chat_client.models.Message;
import com.example.chat_client.models.User;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter {

    private final Context context;
    private final List<Message> messages;

    private static final int VIEW_TYPE_MESSAGE_SENT = 1;
    private static final int VIEW_TYPE_MESSAGE_RECEIVED = 2;

    public MessageAdapter(Context context, List<Message> messages) {
        this.context = context;
        this.messages = messages;
    }

    public void addMessage(Message message) {
        messages.add(message);
        notifyItemInserted(messages.size());
    }

    @Override
    public long getItemId(int position) {
        return messages == null ? -1 : position;
    }

    @Override
    public int getItemCount() {
        return messages == null ? 0 : messages.size();
    }

    @Override
    public int getItemViewType(int position) {
        Message message = messages.get(position);
        User currentUser = App.user.getValue();

        assert currentUser != null;
        if (message.getObject().getName().equals(currentUser.getName())) {
            return VIEW_TYPE_MESSAGE_SENT;
        } else {
            return VIEW_TYPE_MESSAGE_RECEIVED;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_MESSAGE_SENT) {
            return new MessageSentViewHolder(ItemMessageSendBinding.inflate(
                    LayoutInflater.from(context),
                    parent,
                    false
            ));
        } else if (viewType == VIEW_TYPE_MESSAGE_RECEIVED) {
            return new MessageReceivedViewHolder(ItemMessageReceiveBinding.inflate(
                    LayoutInflater.from(context),
                    parent,
                    false
            ));
        }

        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Message message = messages.get(position);

        switch (holder.getItemViewType()) {
            case VIEW_TYPE_MESSAGE_SENT:
                ((MessageSentViewHolder) holder).bind(message);
                break;
            case VIEW_TYPE_MESSAGE_RECEIVED:
                ((MessageReceivedViewHolder) holder).bind(context, message);
                break;
        }
    }

    private static class MessageReceivedViewHolder extends RecyclerView.ViewHolder {
        ItemMessageReceiveBinding receiveBinding;

        public MessageReceivedViewHolder(@NonNull ItemMessageReceiveBinding receiveBinding) {
            super(receiveBinding.getRoot());
            this.receiveBinding = receiveBinding;
        }

        void bind(Context context, Message message) {
            receiveBinding.tvUsername.setText(message.getObject().getName());
            receiveBinding.tvMessageReceive.setText(message.getMessage());
            receiveBinding.tvTime.setText(message.getCreateAt());
            Glide.with(context).load(message.getObject().getAvatar()).into(receiveBinding.ivAva);
        }
    }

    private static class MessageSentViewHolder extends RecyclerView.ViewHolder {
        ItemMessageSendBinding sendBinding;

        public MessageSentViewHolder(@NonNull ItemMessageSendBinding sendBinding) {
            super(sendBinding.getRoot());
            this.sendBinding = sendBinding;
        }

        void bind(Message message) {
            sendBinding.tvMessageSend.setText(message.getMessage());
            sendBinding.tvTime.setText(message.getCreateAt());
        }
    }

    private static class GroupMemberChangeViewHolder extends RecyclerView.ViewHolder {
        ItemGroupMemberChangeBinding memberChangeBinding;

        public GroupMemberChangeViewHolder(@NonNull ItemGroupMemberChangeBinding memberChangeBinding) {
            super(memberChangeBinding.getRoot());
            this.memberChangeBinding = memberChangeBinding;
        }

        void bind(Message message) {
            memberChangeBinding.tvGroupJoinQuit.setText(message.getObject().getName() + "");
        }
    }
}
