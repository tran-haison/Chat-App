package com.example.chat_client.ui.main.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.chat_client.adapters.listview.FriendRequestAdapter;
import com.example.chat_client.databinding.FragmentFriendRequestBinding;
import com.example.chat_client.models.Object;
import com.example.chat_client.models.User;
import com.example.chat_client.ui.main.MainViewModel;
import com.example.chat_client.socket.MessageUtil;
import com.google.android.material.snackbar.Snackbar;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import static com.example.chat_client.socket.ResponseMessage.FAIL_ACCEPT_FRIEND;
import static com.example.chat_client.socket.ResponseMessage.FAIL_DENY_REQUEST;
import static com.example.chat_client.socket.ResponseMessage.SUCCESS_ACCEPT_FRIEND;
import static com.example.chat_client.socket.ResponseMessage.SUCCESS_DENY_REQUEST;
import static com.example.chat_client.socket.ResponseMessage.SUCCESS_LIST_FRIEND_REQUEST;

public class FriendRequestFragment extends Fragment {

    private FragmentFriendRequestBinding binding;
    private MainViewModel viewModel;
    private List<User> friendRequests;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentFriendRequestBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Setup view model
        setupViewModel();
    }

    private void setupViewModel() {
        viewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);

        // Observe response from server
        viewModel.responseMessageLiveData().observe(requireActivity(), this::handleServerResponse);

        // Request to server for list of friend requests
        viewModel.listFriendRequest();
    }

    private void handleServerResponse(String message) {
        try {
            String responseType = MessageUtil.responseType(message);
            switch (responseType) {
                case SUCCESS_LIST_FRIEND_REQUEST:
                    friendRequests = MessageUtil.messageToUsers(message);
                    setViewVisibility();
                    break;
                case SUCCESS_ACCEPT_FRIEND:
                    Toast.makeText(getActivity(), SUCCESS_ACCEPT_FRIEND, Toast.LENGTH_LONG).show();
                    viewModel.listFriendRequest();
                    break;
                case SUCCESS_DENY_REQUEST:
                    Toast.makeText(getActivity(), SUCCESS_DENY_REQUEST, Toast.LENGTH_LONG).show();
                    viewModel.listFriendRequest();
                    break;
                case FAIL_ACCEPT_FRIEND:
                    Snackbar.make(binding.getRoot(), FAIL_ACCEPT_FRIEND, Snackbar.LENGTH_LONG).show();
                    break;
                case FAIL_DENY_REQUEST:
                    Snackbar.make(binding.getRoot(), FAIL_DENY_REQUEST, Snackbar.LENGTH_LONG).show();
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setViewVisibility() {
        if (friendRequests == null || friendRequests.size() <= 0) {
            binding.lvFriendsRequest.setVisibility(View.GONE);
            binding.llFriendRequestPrompt.setVisibility(View.VISIBLE);
        } else {
            binding.lvFriendsRequest.setVisibility(View.VISIBLE);
            binding.llFriendRequestPrompt.setVisibility(View.GONE);
            initFriendRequestListView();
        }
    }

    private void initFriendRequestListView() {
        List<? extends Object> objects = friendRequests;
        FriendRequestAdapter adapter = new FriendRequestAdapter(getActivity(), (List<Object>) objects,
                new FriendRequestAdapter.FriendRequestListener() {
                    @Override
                    public void onAccept(Object object) {
                        User user = new User(object.getName());
                        viewModel.acceptFriend(user);
                    }

                    @Override
                    public void onDeny(Object object) {
                        User user = new User(object.getName());
                        viewModel.denyRequest(user);
                    }
                });
        binding.lvFriendsRequest.setAdapter(adapter);
    }
}