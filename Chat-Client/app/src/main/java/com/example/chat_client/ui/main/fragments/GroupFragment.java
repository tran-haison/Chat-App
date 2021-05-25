package com.example.chat_client.ui.main.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.chat_client.adapters.list_view_adapter.ObjectAdapter;
import com.example.chat_client.databinding.FragmentGroupBinding;
import com.example.chat_client.dialogs.DialogButtonListener;
import com.example.chat_client.dialogs.DialogUtils;
import com.example.chat_client.models.Group;
import com.example.chat_client.models.Object;
import com.example.chat_client.ui.main.MainActivityUtils;
import com.example.chat_client.ui.main.MainViewModel;
import com.example.chat_client.utils.Constants;
import com.example.chat_client.utils.MessageUtil;
import com.google.android.material.snackbar.Snackbar;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import static com.example.chat_client.socket.ResponseMessage.FAIL_GROUP_ALREADY_EXIST;
import static com.example.chat_client.socket.ResponseMessage.SUCCESS_CREATE;
import static com.example.chat_client.socket.ResponseMessage.SUCCESS_LIST_JOINED_GROUP;

public class GroupFragment extends Fragment {

    private MainActivityUtils mainActivityUtils;
    private MainViewModel viewModel;
    private FragmentGroupBinding binding;
    private List<Group> groups;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentGroupBinding.inflate(inflater, container, false);
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
        binding.cvSearch.setOnClickListener(v -> mainActivityUtils.goToSearchActivity(Constants.SEARCH_GROUP));
        binding.fabCreateGroup.setOnClickListener(v -> DialogUtils.dialogCreateObject(requireActivity(), new DialogButtonListener() {
            @Override
            public void onNegativeClicked() {
            }

            @Override
            public void onPositiveClicked(Object object) {
                Group group = new Group(object.getName());
                viewModel.createGroup(group);
            }
        }));
    }

    private void setupViewModel() {
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);

        // Observe response from server
        viewModel.responseMessageLiveData().observe(requireActivity(), this::handleServerResponse);

        // Request list of joined group
        viewModel.listJoinedGroup();
    }

    private void handleServerResponse(String message) {
        try {
            String responseType = MessageUtil.responseType(message);
            switch (responseType) {
                case SUCCESS_LIST_JOINED_GROUP:
                    // Set list view of groups
                    groups = MessageUtil.messageToGroups(message);
                    setViewVisibility();
                    break;
                case SUCCESS_CREATE:
                    // Update list of groups
                    viewModel.listJoinedGroup();
                    break;
                case FAIL_GROUP_ALREADY_EXIST:
                    Snackbar.make(binding.getRoot(), FAIL_GROUP_ALREADY_EXIST, Snackbar.LENGTH_LONG).show();
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setViewVisibility() {
        if (groups == null || groups.size() <= 0) {
            binding.lvGroups.setVisibility(View.GONE);
            binding.llGroupPrompt.setVisibility(View.VISIBLE);
            binding.lavGroup.playAnimation();
        } else {
            binding.lvGroups.setVisibility(View.VISIBLE);
            binding.llGroupPrompt.setVisibility(View.GONE);
            initGroupListView();
        }
    }

    private void initGroupListView() {
        List<? extends Object> objects = groups;
        ObjectAdapter adapter = new ObjectAdapter(
                getActivity(), (List<Object>) objects,
                object -> {
                    Group group = new Group(object.getName());
                    mainActivityUtils.goToGroupChatActivity(group);
                }
        );
        binding.lvGroups.setAdapter(adapter);
    }

}