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

import com.example.chat_client.adapters.list_view_adapter.ObjectAdapter;
import com.example.chat_client.databinding.FragmentNetworkGroupBinding;
import com.example.chat_client.dialogs.DialogButtonListener;
import com.example.chat_client.dialogs.DialogUtils;
import com.example.chat_client.models.Group;
import com.example.chat_client.models.Object;
import com.example.chat_client.ui.main.MainViewModel;
import com.example.chat_client.utils.MessageUtil;
import com.google.android.material.snackbar.Snackbar;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import static com.example.chat_client.socket.ResponseMessage.FAIL_JOIN;
import static com.example.chat_client.socket.ResponseMessage.SUCCESS_JOIN;
import static com.example.chat_client.socket.ResponseMessage.SUCCESS_LIST_NOT_JOINED_GROUP;

public class NetworkGroupFragment extends Fragment {

    private FragmentNetworkGroupBinding binding;
    private MainViewModel viewModel;
    private List<Group> groups;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentNetworkGroupBinding.inflate(inflater, container, false);
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
        viewModel.listNotJoinedGroup();
    }

    private void handleServerResponse(String message) {
        try {
            String responseType = MessageUtil.responseType(message);
            switch (responseType) {
                case SUCCESS_LIST_NOT_JOINED_GROUP:
                    groups = MessageUtil.messageToGroups(message);
                    setViewVisibility();
                    break;
                case SUCCESS_JOIN:
                    Toast.makeText(getActivity(), SUCCESS_JOIN, Toast.LENGTH_LONG).show();
                    viewModel.listNotJoinedGroup();
                    break;
                case FAIL_JOIN:
                    Snackbar.make(binding.getRoot(), FAIL_JOIN, Snackbar.LENGTH_LONG).show();
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setViewVisibility() {
        if (groups == null || groups.size() <= 0) {
            binding.lvNetworkGroup.setVisibility(View.GONE);
            binding.llGroupPrompt.setVisibility(View.VISIBLE);
            binding.lavGroup.playAnimation();
        } else {
            binding.lvNetworkGroup.setVisibility(View.VISIBLE);
            binding.llGroupPrompt.setVisibility(View.GONE);
            initGroupListView();
        }
    }

    private void initGroupListView() {
        List<? extends Object> objects = groups;
        ObjectAdapter adapter = new ObjectAdapter(
                getActivity(), (List<Object>) objects,
                this::showDialogJoinGroup
        );
        binding.lvNetworkGroup.setAdapter(adapter);
    }

    private void showDialogJoinGroup(Object object) {
        Group group = new Group(object.getName());
        DialogUtils.dialogCustom(
                getActivity(), group, "Do you want to join " + group.getName() + " ?",
                new DialogButtonListener() {
                    @Override
                    public void onNegativeClicked() {
                    }

                    @Override
                    public void onPositiveClicked(Object object) {
                        if (object != null) {
                            viewModel.joinGroup(group);
                        }
                    }
                });
    }
}