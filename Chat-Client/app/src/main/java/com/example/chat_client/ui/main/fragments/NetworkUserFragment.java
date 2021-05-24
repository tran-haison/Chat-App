package com.example.chat_client.ui.main.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.chat_client.adapters.list_view_adapter.AdapterUtils;
import com.example.chat_client.adapters.list_view_adapter.ObjectAdapter;
import com.example.chat_client.databinding.FragmentNetworkUserBinding;
import com.example.chat_client.dialogs.DialogButtonListener;
import com.example.chat_client.dialogs.DialogUtils;
import com.example.chat_client.models.Object;
import com.example.chat_client.models.User;
import com.example.chat_client.ui.main.MainViewModel;
import com.example.chat_client.utils.MessageUtil;
import com.google.android.material.snackbar.Snackbar;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import static com.example.chat_client.socket.ResponseMessage.FAIL_ADD_FRIEND;
import static com.example.chat_client.socket.ResponseMessage.FAIL_JOIN;
import static com.example.chat_client.socket.ResponseMessage.SUCCESS_ADD_FRIEND;
import static com.example.chat_client.socket.ResponseMessage.SUCCESS_LIST_USER;

public class NetworkUserFragment extends Fragment {

    private FragmentNetworkUserBinding binding;
    private MainViewModel viewModel;
    private List<Object> users;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentNetworkUserBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Setup ViewModel
        setupViewModel();
    }

    private void setupViewModel() {
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);

        // Observe response from server
        viewModel.responseMessageLiveData().observe(requireActivity(), this::handleServerResponse);

        // Request to server for list of all users
        viewModel.listUser();
    }

    private void handleServerResponse(String message) {
        try {
            String responseType = MessageUtil.responseType(message);
            switch (responseType) {
                case SUCCESS_LIST_USER:
                    users = MessageUtil.messageToObjects(message);
                    setViewVisibility();
                    break;
                case SUCCESS_ADD_FRIEND:
                    Toast.makeText(getActivity(), SUCCESS_ADD_FRIEND, Toast.LENGTH_LONG).show();
                    viewModel.listUser();
                    break;
                case FAIL_ADD_FRIEND:
                    Snackbar.make(binding.getRoot(), FAIL_ADD_FRIEND, Snackbar.LENGTH_LONG).show();
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setViewVisibility() {
        if (users == null || users.size() <= 0) {
            binding.lvNetworkUser.setVisibility(View.GONE);
            binding.llUserPrompt.setVisibility(View.VISIBLE);
            binding.lavUser.playAnimation();
        } else {
            binding.lvNetworkUser.setVisibility(View.VISIBLE);
            binding.llUserPrompt.setVisibility(View.GONE);
            initUserListView();
        }
    }

    private void initUserListView() {
        ObjectAdapter adapter = new ObjectAdapter(
                getActivity(), users, AdapterUtils.userAvatars(),
                this::showDialogAddFriend
        );
        binding.lvNetworkUser.setAdapter(adapter);
    }

    private void showDialogAddFriend(Object object) {
        User user = new User(object.getName());
        DialogUtils.dialogCustom(
                getActivity(), user, "Do you want to add " + user.getName() + " as a new friend?",
                new DialogButtonListener() {
                    @Override
                    public void onNegativeClicked() {
                    }

                    @Override
                    public void onPositiveClicked(Object object) {
                        if (object != null) {
                            viewModel.addFriend(user);
                        }
                    }
                });
    }
}