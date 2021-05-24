package com.example.chat_client.adapters.list_view_adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.bumptech.glide.Glide;
import com.example.chat_client.databinding.ItemFriendRequestBinding;
import com.example.chat_client.models.Object;

import java.util.List;
import java.util.Random;

public class FriendRequestAdapter extends BaseAdapter {

    private final Context context;
    private final List<Object> objects;
    private final FriendRequestListener requestListener;

    public FriendRequestAdapter(Context context, List<Object> objects, FriendRequestListener requestListener) {
        this.context = context;
        this.objects = objects;
        this.requestListener = requestListener;
    }

    @Override
    public int getCount() {
        return objects == null ? 0 : objects.size();
    }

    @Override
    public Object getItem(int position) {
        return objects == null ? null : objects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return objects == null ? -1 : position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        // Init view
        if (convertView == null) {
            viewHolder = new ViewHolder();
            viewHolder.binding = ItemFriendRequestBinding.inflate(LayoutInflater.from(context), parent, false);
            convertView = viewHolder.binding.getRoot();
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Object object = objects.get(position);

        // Set value to view
        viewHolder.binding.tvName.setText(object.getName());

        // Set random avatar to object
        List<Integer> avatars = AdapterUtils.userAvatars();
        Random random = new Random();
        int index = random.nextInt(avatars.size());
        Glide.with(context).load(avatars.get(index)).into(viewHolder.binding.ivAva);

        // View events
        viewHolder.binding.cvAccept.setOnClickListener(v -> requestListener.onAccept(object));
        viewHolder.binding.cvDeny.setOnClickListener(v -> requestListener.onDeny(object));

        return convertView;
    }

    private static class ViewHolder {
        ItemFriendRequestBinding binding;
    }

    public interface FriendRequestListener {
        void onAccept(Object object);

        void onDeny(Object object);
    }
}
