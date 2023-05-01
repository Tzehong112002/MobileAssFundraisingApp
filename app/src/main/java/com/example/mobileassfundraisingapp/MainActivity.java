package com.example.mobileassfundraisingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.Telephony.Mms.Draft;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import com.example.mobileassfundraisingapp.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import android.content.pm.PackageManager;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Set up initial fragment
        replaceFragment(new HomeFragment());

        // Find the BottomNavigationView in the layout
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);

        // Set the background to null
        bottomNavigationView.setBackground(null);

        // Disable the third item in the menu
        bottomNavigationView.getMenu().getItem(2).setEnabled(false);

        // Set up listener for BottomNavigationView items
        bottomNavigationView.setOnItemSelectedListener(menuItem -> {
            switch (menuItem.getItemId()) {
                case R.id.miHome:
                    replaceFragment(new HomeFragment());
                    break;
                case R.id.miProfile:
                    replaceFragment(new profile());
                    break;
                case R.id.miHistory:
                    //replaceFragment(new settingFragment());
                    replaceFragment(new history());
                    break;
                case R.id.miSearch:
                    replaceFragment(new searchFragment());
                    break;
            }
            return true;
        });

        // Set up listener for FAB click
        binding.fab.setOnClickListener(view -> replaceFragment(new AddFragment()));

        //drawer //
        //DrawerLayout drawerLayout = binding.drawerLayout;
        //NavigationView navView = binding.navView;

        //Intent intent2 = getIntent();
        //TextView userNameTextView = findViewById(R.id.user_name);
        //String userUsername = intent2.getStringExtra("username");
        //userNameTextView.setText(userUsername);




        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navView = findViewById(R.id.nav_view);

        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Declare the PackageManager variable
        PackageManager packageManager = getPackageManager();

        navView.setNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.nav_home:
                    replaceFragment(new HomeFragment());
                    break;
                case R.id.nav_message:
                    Toast.makeText(getApplicationContext(), "Clicked Message", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.nav_sync:
                    Toast.makeText(getApplicationContext(), "Clicked Sync", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.nav_trash:
                    Toast.makeText(getApplicationContext(), "Clicked Trash", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.Settings:
                    replaceFragment(new settingFragment());
                    break;
                case R.id.nav_logout:
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    // Clear the activity stack and start the LoginActivity
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    break;
                case R.id.nav_share:
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    String shareSubject = "Check out this awesome app!";
                    String shareBody = "I've been using this app and it's really cool. You should give it a try!";
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, shareSubject);
                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
                    startActivity(Intent.createChooser(shareIntent, "Share via"));
                    break;
                case R.id.nav_rate_us:

                    // Launch Google Play Store to rate the app
                    Intent rateIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getPackageName()));
                    boolean isIntentSafe = packageManager.queryIntentActivities(rateIntent, 0).size() > 0;
                    if (isIntentSafe) {
                        startActivity(rateIntent);
                    } else {
                        // If the Google Play Store app is not installed, open the website
                        Intent websiteIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName()));
                        startActivity(websiteIntent);
                    }

                    break;
            }

            // Close the drawer
            drawerLayout.closeDrawer(GravityCompat.START);

            return true;
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void replaceFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }


}
