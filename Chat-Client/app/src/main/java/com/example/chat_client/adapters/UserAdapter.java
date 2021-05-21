package com.example.chat_client.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.bumptech.glide.Glide;
import com.example.chat_client.R;
import com.example.chat_client.databinding.ItemObjectBinding;
import com.example.chat_client.models.User;

import java.util.List;
import java.util.Random;

public class UserAdapter extends BaseAdapter {

    private final Context context;
    private final List<User> users;
    private final ItemListener<User> itemListener;

    public UserAdapter(Context context, List<User> users, ItemListener<User> itemListener) {
        this.context = context;
        this.users = users;
        this.itemListener = itemListener;
    }

    @Override
    public int getCount() {
        return users == null ? 0 : users.size();
    }

    @Override
    public Object getItem(int position) {
        return users == null ? -1 : users.get(position);
    }

    @Override
    public long getItemId(int position) {
        return users == null ? -1 : position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        // Init view
        if (convertView == null) {
            viewHolder = new ViewHolder();
            viewHolder.binding = ItemObjectBinding.inflate(LayoutInflater.from(context), parent, false);
            convertView = viewHolder.binding.getRoot();
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // Set value to view
        User user = users.get(position);
        viewHolder.binding.tvName.setText(user.getUsername());
        viewHolder.binding.getRoot().setOnClickListener(v -> itemListener.onItemClicked(user));

        // Load random ava into image view
        int[] userAvatars = {
                R.drawable.img_user1,
                R.drawable.img_user2,
                R.drawable.img_user3,
                R.drawable.img_user4
        };
        Random random = new Random();
        int ava = random.nextInt(userAvatars.length);
        Glide.with(context).load(userAvatars[ava]).into(viewHolder.binding.ivAva);

        return convertView;
    }

    private static class ViewHolder {
        ItemObjectBinding binding;
    }

}
