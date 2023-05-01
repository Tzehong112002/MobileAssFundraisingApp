package com.example.mobileassfundraisingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ResolveInfo;
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

import com.example.mobileassfundraisingapp.PaymentHistoryFragment;
import com.example.mobileassfundraisingapp.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import android.content.Intent;
import com.example.mobileassfundraisingapp.PaymentActivity;

import android.content.pm.PackageManager;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private ActionBarDrawerToggle toggle;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Get the username from the intent
        username = getIntent().getStringExtra("username");

        // Set up initial fragment
        replaceFragment(new HomeFragment(), username);

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
                    replaceFragment(new HomeFragment(), username);
                    break;
                case R.id.miProfile:
                    replaceFragment(new profile(), username);
                    break;
                case R.id.miHistory:
                    replaceFragment(PaymentHistoryFragment.newInstance(username), username);
                    break;
                case R.id.miSearch:
                    replaceFragment(new searchFragment(), username);
                    break;
            }
            return true;
        });

        // Set up listener for FAB click
        binding.fab.setOnClickListener(view -> replaceFragment(new AddFragment(), username));

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
                    replaceFragment(new HomeFragment(), username);
                    break;
                case R.id.nav_Event:
                    replaceFragment(new searchFragment());
                    break;
                case R.id.nav_Profile:
                    replaceFragment(new profile());
                    break;
                case R.id.nav_maps:
                    //getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            //new Map()).commit();
                    replaceFragment(new MapsFragment());
                    break;
                case R.id.Settings:
                    //replaceFragment(new settingFragment());
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
                    // Find all the activities that can handle the rateIntent
                    List<ResolveInfo> activities = packageManager.queryIntentActivities(rateIntent, 0);
                    boolean isIntentSafe = activities.size() > 0;

                    // If there are activities that can handle the rateIntent, start the intent
                    if (isIntentSafe) {
                        startActivity(rateIntent);
                    } else {
                        // If there are no activities that can handle the rateIntent, open the web browser
                        Intent webIntent = new Intent(Intent.ACTION_VIEW,
                                Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName()));
                        startActivity(webIntent);
                    }
                    break;
            }

            // Close the drawer after an item is clicked
            DrawerLayout drawer = findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);

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

    /**
     * Replaces the current fragment with the given fragment and passes the username as an argument.
     *
     * @param fragment the fragment to replace the current fragment with
     * @param username the username to pass as an argument
     */
    private void replaceFragment(Fragment fragment, String username) {
        Bundle args = new Bundle();
        args.putString("username", username);
        fragment.setArguments(args);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }
}
