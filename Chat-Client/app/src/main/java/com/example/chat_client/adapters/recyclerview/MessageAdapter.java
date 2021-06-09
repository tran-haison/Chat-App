package com.example.chat_client.adapters.recyclerview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.chat_client.databinding.ItemGroupMemberChangeBinding;
import com.example.chat_client.databinding.ItemMessageReceiveBinding;
import com.example.chat_client.databinding.ItemMessageSendBinding;
import com.example.chat_client.models.Message;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter {

    private final Context context;
    private final List<Message> messages;

    private final int VIEW_TYPE_SEND = 1;
    private final int VIEW_TYPE_RECEIVE = 2;
    private final int VIEW_TYPE_JOIN = 3;
    private final int VIEW_TYPE_QUIT = 4;

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

        switch (message.getMessageType()) {
            case SEND:
                return VIEW_TYPE_SEND;
            case RECEIVE:
                return VIEW_TYPE_RECEIVE;
            case JOIN:
                return VIEW_TYPE_JOIN;
            case QUIT:
                return VIEW_TYPE_QUIT;
            default:
                return -1;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_SEND:
                return new MessageSentViewHolder(ItemMessageSendBinding.inflate(
                        LayoutInflater.from(context),
                        parent,
                        false
                ));
            case VIEW_TYPE_RECEIVE:
                return new MessageReceivedViewHolder(ItemMessageReceiveBinding.inflate(
                        LayoutInflater.from(context),
                        parent,
                        false
                ));
            case VIEW_TYPE_JOIN:
            case VIEW_TYPE_QUIT:
                return new GroupMemberChangeViewHolder(ItemGroupMemberChangeBinding.inflate(
                        LayoutInflater.from(context),
                        parent,
                        false
                ));
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Message message = messages.get(position);

        switch (holder.getItemViewType()) {
            case VIEW_TYPE_SEND:
                ((MessageSentViewHolder) holder).bind(context, message);
                break;
            case VIEW_TYPE_RECEIVE:
                ((MessageReceivedViewHolder) holder).bind(context, message);
                break;
            case VIEW_TYPE_JOIN:
            case VIEW_TYPE_QUIT:
                ((GroupMemberChangeViewHolder) holder).bind(message);
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
            if (message.getImageBitmap() != null) {
                receiveBinding.cvImage.setVisibility(View.VISIBLE);
                Glide.with(context).load(message.getImageBitmap()).into(receiveBinding.ivImage);
            }
        }
    }

    private static class MessageSentViewHolder extends RecyclerView.ViewHolder {
        ItemMessageSendBinding sendBinding;

        public MessageSentViewHolder(@NonNull ItemMessageSendBinding sendBinding) {
            super(sendBinding.getRoot());
            this.sendBinding = sendBinding;
        }

        void bind(Context context, Message message) {
            sendBinding.tvMessageSend.setText(message.getMessage());
            sendBinding.tvTime.setText(message.getCreateAt());
            if (message.getImageBitmap() != null) {
                sendBinding.cvImage.setVisibility(View.VISIBLE);
                Glide.with(context).load(message.getImageBitmap()).into(sendBinding.ivImage);
            }
        }
    }

    private static class GroupMemberChangeViewHolder extends RecyclerView.ViewHolder {
        ItemGroupMemberChangeBinding memberChangeBinding;

        public GroupMemberChangeViewHolder(@NonNull ItemGroupMemberChangeBinding memberChangeBinding) {
            super(memberChangeBinding.getRoot());
            this.memberChangeBinding = memberChangeBinding;
        }

        void bind(Message message) {
            switch (message.getMessageType()) {
                case JOIN:
                    String join_prompt = message.getObject().getName() + " has joined the group";
                    memberChangeBinding.tvGroupMemberChange.setText(join_prompt);
                    break;
                case QUIT:
                    String quit_prompt = message.getObject().getName() + " has left the group";
                    memberChangeBinding.tvGroupMemberChange.setText(quit_prompt);
                    break;
            }
        }
    }
}
