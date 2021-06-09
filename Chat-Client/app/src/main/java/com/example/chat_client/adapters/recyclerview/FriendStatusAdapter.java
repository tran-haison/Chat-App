package com.example.chat_client.adapters.recyclerview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.chat_client.databinding.ItemFriendStatusBinding;
import com.example.chat_client.models.User;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class FriendStatusAdapter extends RecyclerView.Adapter {

    private final Context context;
    private final List<User> users;
    private final ItemListener listener;

    public FriendStatusAdapter(Context context, List<User> users, ItemListener listener) {
        this.context = context;
        this.users = users;
        this.listener = listener;
    }

    @NonNull
    @NotNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new ViewHolder(ItemFriendStatusBinding.inflate(LayoutInflater.from(context),
                parent,
                false));
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull RecyclerView.ViewHolder holder, int position) {
        User user = users.get(position);
        ((ViewHolder) holder).bind(context, user);
    }

    @Override
    public int getItemCount() {
        return users == null ? 0 : users.size();
    }

    private class ViewHolder extends RecyclerView.ViewHolder {

        ItemFriendStatusBinding binding;

        public ViewHolder(@NonNull @NotNull ItemFriendStatusBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(Context context, User user) {
            Glide.with(context).load(user.getAvatar()).into(binding.ivAva);
            binding.tvUsername.setText(user.getName());
            binding.getRoot().setOnClickListener(v -> listener.onClicked(user));
        }
    }

    public interface ItemListener {
        void onClicked(User user);
    }
}
