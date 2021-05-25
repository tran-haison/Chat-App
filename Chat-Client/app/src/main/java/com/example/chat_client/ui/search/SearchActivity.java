package com.example.chat_client.ui.search;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.EditorInfo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.chat_client.adapters.list_view_adapter.ObjectAdapter;
import com.example.chat_client.databinding.ActivitySearchBinding;
import com.example.chat_client.models.Group;
import com.example.chat_client.models.Object;
import com.example.chat_client.models.User;
import com.example.chat_client.ui.chat.GroupChatActivity;
import com.example.chat_client.ui.chat.PrivateChatActivity;
import com.example.chat_client.utils.Constants;
import com.example.chat_client.utils.MessageUtil;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import static com.example.chat_client.socket.ResponseMessage.SUCCESS_SEARCH_FRIEND;
import static com.example.chat_client.socket.ResponseMessage.SUCCESS_SEARCH_JOINED_GROUP;

public class SearchActivity extends AppCompatActivity {

    private ActivitySearchBinding binding;
    private SearchViewModel viewModel;

    private List<Object> objects;
    private String searchType;

    private Timer timer = new Timer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySearchBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        // Get search type
        getIntentSearchType();

        // Setup ViewModel
        setupViewModel();

        // View events
        binding.ibBack.setOnClickListener(v -> onBackPressed());

        binding.etSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                onQueryTextChanged(s.toString());
            }
        });

        binding.etSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                onSearchClicked();
                return true;
            }
            return false;
        });
    }

    private void getIntentSearchType() {
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra(Constants.BUNDLE);
        if (bundle != null) {
            searchType = bundle.getString(Constants.SEARCH_TYPE);
        }
    }

    private void setupViewModel() {
        viewModel = new ViewModelProvider(this).get(SearchViewModel.class);
        // Observe response from server
        viewModel.responseMessageLiveData().observe(this, this::handleServerMessage);
    }

    private void handleServerMessage(String message) {
        try {
            String responseType = MessageUtil.responseType(message);
            switch (responseType) {

                case SUCCESS_SEARCH_FRIEND:
                    List<? extends Object> users = MessageUtil.messageToUsers(message);
                    this.objects = (List<Object>) users;
                    setViewVisibility();
                    break;

                case SUCCESS_SEARCH_JOINED_GROUP:
                    List<? extends Object> groups = MessageUtil.messageToGroups(message);
                    this.objects = (List<Object>) groups;
                    setViewVisibility();
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setViewVisibility() {
        if (objects == null || objects.size() <= 0) {
            binding.lvSearch.setVisibility(View.GONE);
            binding.llSearchPrompt.setVisibility(View.VISIBLE);
            binding.lavSearch.playAnimation();
        } else {
            binding.lvSearch.setVisibility(View.VISIBLE);
            binding.llSearchPrompt.setVisibility(View.GONE);
            initObjectListView();
        }
    }

    private void initObjectListView() {
        ObjectAdapter adapter = new ObjectAdapter(this, objects, this::goToChatActivity);
        binding.lvSearch.setAdapter(adapter);
    }

    private void goToChatActivity(Object object) {
        Class activity = searchType.equals(Constants.SEARCH_GROUP)
                ? GroupChatActivity.class
                : PrivateChatActivity.class;

        // Put object into bundle
        Bundle bundle = new Bundle();
        if (searchType.equals(Constants.SEARCH_USER)) {
            bundle.putSerializable(Constants.FRIEND, new User(object.getName()));
        } else if (searchType.equals(Constants.SEARCH_GROUP)) {
            bundle.putSerializable(Constants.GROUP, new Group(object.getName()));
        }

        Intent intent = new Intent(this, activity);
        intent.putExtra(Constants.BUNDLE, bundle);
        startActivity(intent);
    }

    private void onQueryTextChanged(String query) {
        final long DELAY = 1000;
        timer.cancel();
        timer = new Timer();
        timer.schedule(
                new TimerTask() {
                    @Override
                    public void run() {
                        runOnUiThread(() -> search(query));
                    }
                },
                DELAY
        );
    }

    private void onSearchClicked() {
        String query = Objects.requireNonNull(binding.etSearch.getText()).toString();
        if (query.isEmpty()) {
            Snackbar.make(binding.getRoot(), "Empty search field", Snackbar.LENGTH_SHORT).show();
        } else {
            search(query);
        }
    }

    private void search(String query) {
        if (searchType.equals(Constants.SEARCH_USER)) {
            viewModel.searchFriend(query);
        } else if (searchType.equals(Constants.SEARCH_GROUP)) {
            viewModel.searchJoinedGroup(query);
        } else {
            Snackbar.make(binding.getRoot(), "Error searching!", Snackbar.LENGTH_SHORT).show();
        }
    }
}