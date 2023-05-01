package com.example.mobileassfundraisingapp.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.mobileassfundraisingapp.LoginActivity;
import com.example.mobileassfundraisingapp.R;

public class mainPageAdmin extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page_admin);

        // Get reference to your event card view
        View eventCardView = findViewById(R.id.EventCard);
        View AdminAccountCardView = findViewById(R.id.AdminAccountCard);
        View DonorAccountCardView = findViewById(R.id.DonorAccountCard);
        View logoutCardView = findViewById(R.id.LogOutCard);

        eventCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create a new intent to start the LoginActivity
                Intent intent = new Intent(mainPageAdmin.this, adminActivity.class);
                startActivity(intent);
            }
        });

        AdminAccountCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create a new intent to start the LoginActivity
                Intent intent = new Intent(mainPageAdmin.this, adminActivity.class);
                startActivity(intent);
            }
        });

        DonorAccountCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create a new intent to start the LoginActivity
                Intent intent = new Intent(mainPageAdmin.this, adminActivity.class);
                startActivity(intent);
            }
        });


        logoutCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create a new intent to start the LoginActivity
                Intent intent = new Intent(mainPageAdmin.this, LoginActivity.class);
                // Clear the activity stack and start the LoginActivity
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
    }
}
