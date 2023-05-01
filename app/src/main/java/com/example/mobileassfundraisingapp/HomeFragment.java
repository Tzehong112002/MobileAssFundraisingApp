package com.example.mobileassfundraisingapp;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;



public class HomeFragment extends Fragment {

    private View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        // Do any other initialization or view setup here

        // Get reference to your event card view
        View eventCardView = view.findViewById(R.id.EventCard);
        View profileCardView = view.findViewById(R.id.ProfileCard);
        View settingsCardView = view.findViewById(R.id.SettingCard);
        View logoutCardView = view.findViewById(R.id.LogOutCard);

        // Set an OnClickListener to the event card view
        eventCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create a new instance of EventFragment
                searchFragment searchFragment = new searchFragment();

                // Replace the current fragment with EventFragment
                FragmentManager fragmentManager = getParentFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, searchFragment);
                fragmentTransaction.addToBackStack(null);  // Optional: Add to back stack
                fragmentTransaction.commit();
            }
        });

        profileCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create a new instance of EventFragment
                profile profile = new profile();

                // Replace the current fragment with EventFragment
                FragmentManager fragmentManager = getParentFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, profile);
                fragmentTransaction.addToBackStack(null);  // Optional: Add to back stack
                fragmentTransaction.commit();
            }
        });

        settingsCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create a new instance of EventFragment
                settingFragment settingFragment = new settingFragment();

                // Replace the current fragment with EventFragment
                FragmentManager fragmentManager = getParentFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, settingFragment);
                fragmentTransaction.addToBackStack(null);  // Optional: Add to back stack
                fragmentTransaction.commit();
            }
        });

        logoutCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create a new intent to start the LoginActivity
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                // Clear the activity stack and start the LoginActivity
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        return view;
    }
}

