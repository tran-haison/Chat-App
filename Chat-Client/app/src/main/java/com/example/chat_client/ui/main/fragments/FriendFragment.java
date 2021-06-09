package com.example.chat_client.ui.main.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.chat_client.adapters.listview.ObjectAdapter;
import com.example.chat_client.adapters.recyclerview.FriendStatusAdapter;
import com.example.chat_client.databinding.FragmentFriendBinding;
import com.example.chat_client.models.Object;
import com.example.chat_client.models.User;
import com.example.chat_client.socket.MessageUtil;
import com.example.chat_client.ui.main.MainActivityUtils;
import com.example.chat_client.ui.main.MainViewModel;
import com.example.chat_client.utils.Constants;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import static com.example.chat_client.socket.ResponseMessage.SUCCESS_LIST_ACTIVE_FRIEND;
import static com.example.chat_client.socket.ResponseMessage.SUCCESS_LIST_FRIEND;

public class FriendFragment extends Fragment {

    private MainViewModel viewModel;
    private MainActivityUtils mainActivityUtils;
    private FragmentFriendBinding binding;
    private List<User> friends;
    private List<User> activeFriends;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentFriendBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Init util
        mainActivityUtils = new MainActivityUtils(getActivity());

        // Setup view model
        setupViewModel();

        // View events
        binding.cvSearch.setOnClickListener(v -> mainActivityUtils.goToSearchActivity(Constants.SEARCH_USER));
    }

    private void setupViewModel() {
        viewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);

        // Observe response from server
        viewModel.responseMessageLiveData().observe(requireActivity(), this::handleServerResponse);

        // Request list of friends
        viewModel.listFriend();
    }

    private void handleServerResponse(String message) {
        try {
            String responseType = MessageUtil.responseType(message);
            switch (responseType) {
                case SUCCESS_LIST_FRIEND:
                    friends = MessageUtil.messageToUsers(message);
                    setupListFriends();
                    break;
                case SUCCESS_LIST_ACTIVE_FRIEND:
                    activeFriends = MessageUtil.messageToUsers(message);
                    initActiveFriendsRecyclerView();
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setupListFriends() {
        if (friends == null || friends.size() <= 0) {
            binding.llFriendsLists.setVisibility(View.GONE);
            binding.llFriendPrompt.setVisibility(View.VISIBLE);
            binding.lavFriend.playAnimation();
        } else {
            // Request list of active friends
            viewModel.listActiveFriend();

            // Init list of all friends
            binding.llFriendsLists.setVisibility(View.VISIBLE);
            binding.llFriendPrompt.setVisibility(View.GONE);
            binding.tvFriendsCount.setText(String.valueOf(friends.size()));
            initAllFriendsListView();
        }
    }

    private void initAllFriendsListView() {
        List<? extends Object> objects = friends;
        ObjectAdapter objectAdapter = new ObjectAdapter(
                getActivity(), (List<Object>) objects,
                object -> {
                    User user = new User(object.getName());
                    mainActivityUtils.goToPrivateChatActivity(user);
                });
        binding.lvFriends.setAdapter(objectAdapter);
    }

    private void initActiveFriendsRecyclerView() {
        if (activeFriends != null && activeFriends.size() > 0) {
            binding.tvActiveFriendsCount.setText(String.valueOf(activeFriends.size()));
            binding.rvOnlineFriends.setVisibility(View.VISIBLE);

            binding.rvOnlineFriends.setLayoutManager(
                    new LinearLayoutManager(getActivity(),
                            LinearLayoutManager.HORIZONTAL,
                            false));
            FriendStatusAdapter adapter = new FriendStatusAdapter(
                    getActivity(),
                    activeFriends,
                    user -> mainActivityUtils.goToPrivateChatActivity(user));
            binding.rvOnlineFriends.setAdapter(adapter);
        } else {
            binding.tvActiveFriendsCount.setText(String.valueOf(0));
            binding.rvOnlineFriends.setVisibility(View.GONE);
        }
    }
}