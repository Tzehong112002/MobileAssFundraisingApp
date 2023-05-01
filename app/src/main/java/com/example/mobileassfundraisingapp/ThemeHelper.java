package com.example.mobileassfundraisingapp;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.appcompat.app.AppCompatDelegate;

public class ThemeHelper {
    private static final String THEME_PREFERENCES = "theme_preferences";
    private static final String CURRENT_THEME = "current_theme";

    public static void applyTheme(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(THEME_PREFERENCES, Context.MODE_PRIVATE);
        String currentTheme = sharedPreferences.getString(CURRENT_THEME, "light");
        switch (currentTheme) {
            case "light":
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                break;
            case "dark":
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                break;
            case "system_default":
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                break;
        }
    }

    public static void setTheme(Context context, String theme) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(THEME_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(CURRENT_THEME, theme);
        editor.apply();
    }

    public static String getCurrentTheme(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(THEME_PREFERENCES, Context.MODE_PRIVATE);
        return sharedPreferences.getString(CURRENT_THEME, "light");
    }
}

