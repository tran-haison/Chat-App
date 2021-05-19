package com.example.chat_client.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.bumptech.glide.Glide;
import com.example.chat_client.R;
import com.example.chat_client.databinding.ItemGroupBinding;
import com.example.chat_client.models.Group;

import java.util.List;
import java.util.Random;

public class GroupAdapter extends BaseAdapter {

    private final Context context;
    private final List<Group> groups;
    private final ItemListener<Group> itemListener;

    public GroupAdapter(Context context, List<Group> groups, ItemListener<Group> itemListener) {
        this.context = context;
        this.groups = groups;
        this.itemListener = itemListener;
    }

    @Override
    public int getCount() {
        return groups == null ? 0 : groups.size();
    }

    @Override
    public Object getItem(int position) {
        return groups == null ? -1 : groups.get(position);
    }

    @Override
    public long getItemId(int position) {
        return groups == null ? -1 : position;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        // Init view
        if (convertView == null) {
            viewHolder = new ViewHolder();
            viewHolder.binding = ItemGroupBinding.inflate(LayoutInflater.from(context), parent, false);
            convertView = viewHolder.binding.getRoot();
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // Set value to view
        Group group = groups.get(position);
        viewHolder.binding.tvGroupName.setText(group.getName());
        viewHolder.binding.tvGroupMember.setText(group.getNumberOfMember() + " member(s)");
        viewHolder.binding.getRoot().setOnClickListener(v -> itemListener.onItemClicked(group));

        // Load random ava into image view
        int[] groupAvatars = {
                R.drawable.img_group1,
                R.drawable.img_group2,
                R.drawable.img_group3,
                R.drawable.img_group4
        };
        Random random = new Random();
        int ava = random.nextInt(groupAvatars.length);
        Glide.with(context).load(groupAvatars[ava]).into(viewHolder.binding.ivGroupAva);

        return convertView;
    }

    private static class ViewHolder{
        ItemGroupBinding binding;
    }
}
