package com.example.mobileassfundraisingapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.preference.SwitchPreference;



import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

public class settingFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);

        SwitchPreference themePreference = findPreference("theme_preference");

        if (themePreference != null) {
            themePreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    String theme = newValue.toString();
                    ThemeHelper.setTheme(requireContext(), theme);
                    ThemeHelper.applyTheme(requireContext());
                    return true;
                }
            });
        }
    }
}

