package com.example.chat_client.ui.group;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.chat_client.adapters.listview.ObjectAdapter;
import com.example.chat_client.databinding.ActivityGroupInfoBinding;
import com.example.chat_client.dialogs.DialogButtonListener;
import com.example.chat_client.dialogs.DialogUtils;
import com.example.chat_client.models.Group;
import com.example.chat_client.models.Object;
import com.example.chat_client.models.User;
import com.example.chat_client.ui.main.MainActivity;
import com.example.chat_client.utils.Constants;
import com.example.chat_client.socket.MessageUtil;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import static com.example.chat_client.socket.ResponseMessage.FAIL_QUIT;
import static com.example.chat_client.socket.ResponseMessage.SUCCESS_QUIT;

public class GroupInfoActivity extends AppCompatActivity {

    private ActivityGroupInfoBinding binding;
    private GroupInfoViewModel viewModel;

    private List<User> members;
    private Group group;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGroupInfoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Get intent group info
        getIntentGroupInfo();

        // Setup ViewModel
        setupViewModel();

        // View implementation
        initMembersListView();
        Glide.with(this).load(group.getAvatar()).into(binding.ivGroupAva);
        binding.tvGroupName.setText(group.getName());

        // View event
        binding.ibBack.setOnClickListener(v -> onBackPressed());
        binding.ibQuit.setOnClickListener(v -> showDialogQuitGroup());
    }

    private void getIntentGroupInfo() {
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra(Constants.BUNDLE);
        if (bundle != null) {
            group = (Group) bundle.getSerializable(Constants.GROUP);
            members = (List<User>) bundle.getSerializable(Constants.MEMBERS);
        }
    }

    private void setupViewModel() {
        viewModel = new ViewModelProvider(this).get(GroupInfoViewModel.class);
        viewModel.responseMessageLiveData().observe(this, this::handleServerResponse);
    }

    private void initMembersListView() {
        // Init list view of all members
        List<? extends Object> objects = members;
        ObjectAdapter adapter = new ObjectAdapter(this, (List<Object>) objects, object -> {
            // TODO: do something with member, eg: chat
            Toast.makeText(this, object.getName(), Toast.LENGTH_SHORT).show();
        });
        binding.lvMembers.setAdapter(adapter);
        binding.tvNumberOfMembers.setText(String.valueOf(adapter.getCount()));
    }

    private void handleServerResponse(String serverMessage) {
        try {
            String responseType = MessageUtil.responseType(serverMessage);
            switch (responseType) {
                case SUCCESS_QUIT:
                    onSuccessQuitGroup();
                    break;
                case FAIL_QUIT:
                    Snackbar.make(binding.getRoot(), FAIL_QUIT, Snackbar.LENGTH_LONG).show();
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void onSuccessQuitGroup() {
        // Return to Main Activity
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void showDialogQuitGroup() {
        DialogUtils.dialogCustom(
                this,
                group,
                "Do you want to quit this group?",
                new DialogButtonListener() {
                    @Override
                    public void onNegativeClicked() {
                    }

                    @Override
                    public void onPositiveClicked(Object object) {
                        viewModel.quitGroup(group);
                    }
                }
        );
    }

}