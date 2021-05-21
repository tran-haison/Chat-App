package com.example.chat_client.ui.main.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.chat_client.adapters.UserAdapter;
import com.example.chat_client.databinding.FragmentFriendBinding;
import com.example.chat_client.models.User;
import com.example.chat_client.ui.main.MainActivityUtils;
import com.example.chat_client.ui.main.MainViewModel;
import com.example.chat_client.utils.Constants;
import com.example.chat_client.utils.MessageUtil;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import static com.example.chat_client.socket.ResponseMessage.SUCCESS_LIST_FRIEND;

public class FriendFragment extends Fragment {

    private MainActivityUtils mainActivityUtils;
    private MainViewModel viewModel;
    private FragmentFriendBinding binding;
    private List<User> friends;

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
        viewModel.getResponseMessageLiveData().observe(requireActivity(), this::handleServerResponse);

        // Observe friend list
        viewModel.getFriendListLiveData().observe(requireActivity(), friends -> {
            this.friends = friends;
            setViewVisibility();
        });

        // Request list of friend
        viewModel.listFriend();
    }

    private void handleServerResponse(String message) {
        try {
            String responseType = MessageUtil.responseType(message);
            if (responseType.equals(SUCCESS_LIST_FRIEND)) {
                viewModel.setFriendList(MessageUtil.messageToUser(message));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setViewVisibility() {
        if (friends == null || friends.size() <= 0) {
            binding.lvFriends.setVisibility(View.GONE);
            binding.llFriendPrompt.setVisibility(View.VISIBLE);
            binding.lavFriend.playAnimation();
        } else {
            binding.lvFriends.setVisibility(View.VISIBLE);
            binding.llFriendPrompt.setVisibility(View.GONE);
            initFriendList();
        }
    }

    private void initFriendList() {
        UserAdapter userAdapter = new UserAdapter(getActivity(), friends,
                user -> mainActivityUtils.goToPrivateChatActivity(user)
        );
        binding.lvFriends.setAdapter(userAdapter);
    }
}