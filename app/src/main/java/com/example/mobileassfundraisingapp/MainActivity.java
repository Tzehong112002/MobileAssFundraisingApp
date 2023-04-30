package com.example.mobileassfundraisingapp;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.Telephony.Mms.Draft;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import com.example.mobileassfundraisingapp.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

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
                case R.id.miSettings:
                    replaceFragment(new settingFragment());
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

        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navView = findViewById(R.id.nav_view);

        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        navView.setNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.nav_home:
                    Toast.makeText(getApplicationContext(), "Clicked Home", Toast.LENGTH_SHORT).show();
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
                case R.id.nav_login:
                    Toast.makeText(getApplicationContext(), "Clicked Login", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.nav_share:
                    Toast.makeText(getApplicationContext(), "Clicked Share", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.nav_rate_us:
                    Toast.makeText(getApplicationContext(), "Clicked Rate Us", Toast.LENGTH_SHORT).show();
                    break;
            }
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
