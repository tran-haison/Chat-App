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

import com.example.chat_client.R;
import com.example.chat_client.adapters.GroupAdapter;
import com.example.chat_client.databinding.FragmentGroupBinding;
import com.example.chat_client.dialogs.DialogCreate;
import com.example.chat_client.models.Group;
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
        binding.fabCreateGroup.setOnClickListener(v -> showCreateGroupDialog());
    }

    private void setupViewModel() {
        viewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);

        // Observe response from server
        viewModel.responseMessageLiveData().observe(requireActivity(), this::handleServerResponse);

        // Observe group list
        viewModel.getJoinedGroupListLiveData().observe(requireActivity(), groups -> {
            this.groups = groups;
            setViewVisibility();
        });

        // Request list of joined group
        viewModel.listJoinedGroup();
    }

    private void handleServerResponse(String message) {
        try {
            String responseType = MessageUtil.responseType(message);
            switch (responseType) {
                case SUCCESS_LIST_JOINED_GROUP:
                    viewModel.setJoinedGroupList(MessageUtil.messageToGroup(message));
                    break;
                case SUCCESS_CREATE:
                    viewModel.listJoinedGroup();
                    break;
                case FAIL_GROUP_ALREADY_EXIST:
                    Snackbar.make(binding.getRoot(), FAIL_GROUP_ALREADY_EXIST, Snackbar.LENGTH_SHORT).show();
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
            initGroupList();
        }
    }

    private void initGroupList() {
        GroupAdapter adapter = new GroupAdapter(getActivity(), groups, group ->
                mainActivityUtils.goToGroupChatActivity(group)
        );
        binding.lvGroups.setAdapter(adapter);
    }

    private void showCreateGroupDialog() {
        DialogCreate dialog = new DialogCreate(
                getActivity(),
                requireActivity().getResources().getString(R.string.create_group),
                requireActivity().getResources().getString(R.string.enter_group_name)
        );
        dialog.setButtonListener(new DialogCreate.ButtonListener() {
            @Override
            public void onNegativeClicked() {
            }

            @Override
            public void onPositiveClicked(String name) {
                Group group = new Group(name);
                if (!group.isNameValid()) {
                    Toast.makeText(getActivity(), Constants.GROUP_NAME_INVALID, Toast.LENGTH_SHORT).show();
                } else {
                    dialog.dismiss();
                    viewModel.createGroup(group);
                }
            }
        });
        dialog.show();
    }
}