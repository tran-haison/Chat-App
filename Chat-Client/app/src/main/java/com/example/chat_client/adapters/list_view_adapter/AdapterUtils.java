package com.example.chat_client.adapters.list_view_adapter;

import com.example.chat_client.R;

import java.util.Arrays;
import java.util.List;

public class AdapterUtils {

    public static List<Integer> userAvatars() {
        Integer[] userAvatars = {
                R.drawable.img_user1,
                R.drawable.img_user2,
                R.drawable.img_user3,
                R.drawable.img_user4
        };
        return Arrays.asList(userAvatars);
    }

    public static List<Integer> groupAvatars() {
        Integer[] groupAvatars = {
                R.drawable.img_group1,
                R.drawable.img_group2,
                R.drawable.img_group3,
                R.drawable.img_group4
        };
        return Arrays.asList(groupAvatars);
    }
}
