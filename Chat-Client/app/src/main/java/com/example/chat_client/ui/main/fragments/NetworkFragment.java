package com.example.chat_client.ui.main.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.chat_client.adapters.view_pager_adapter.NetworkViewPagerAdapter;
import com.example.chat_client.databinding.FragmentNetworkBinding;

import org.jetbrains.annotations.NotNull;

public class NetworkFragment extends Fragment {

    private FragmentNetworkBinding binding;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentNetworkBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Create ViewPager adapter
        setupViewPager();
    }

    private void setupViewPager() {
        NetworkViewPagerAdapter pagerAdapter = new NetworkViewPagerAdapter(
                requireActivity().getSupportFragmentManager(),
                FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
        );
        pagerAdapter.populateFragment(new NetworkUserFragment(), "Users");
        pagerAdapter.populateFragment(new NetworkGroupFragment(), "Groups");
        binding.vpContainer.setAdapter(pagerAdapter);
        binding.tlNetwork.setupWithViewPager(binding.vpContainer);
    }
}