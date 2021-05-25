package com.example.chat_client.adapters.list_view_adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.bumptech.glide.Glide;
import com.example.chat_client.databinding.ItemObjectBinding;
import com.example.chat_client.models.Object;

import java.util.List;

public class ObjectAdapter extends BaseAdapter {

    private final Context context;
    private final List<Object> objects;
    private final ItemListener itemListener;

    public ObjectAdapter(
            Context context,
            List<Object> objects,
            ItemListener itemListener
    ) {
        this.context = context;
        this.objects = objects;
        this.itemListener = itemListener;
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
            viewHolder.binding = ItemObjectBinding.inflate(LayoutInflater.from(context), parent, false);
            convertView = viewHolder.binding.getRoot();
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Object object = objects.get(position);

        // Set value to view
        viewHolder.binding.tvName.setText(object.getName());
        Glide.with(context).load(object.getAvatar()).into(viewHolder.binding.ivAva);

        // View event
        viewHolder.binding.getRoot().setOnClickListener(v -> itemListener.onItemClicked(object));

        return convertView;
    }

    private static class ViewHolder {
        ItemObjectBinding binding;
    }

    public interface ItemListener {
        void onItemClicked(Object object);
    }

}
