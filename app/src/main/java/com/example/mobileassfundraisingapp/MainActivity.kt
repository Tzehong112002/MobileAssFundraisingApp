package com.example.mobileassfundraisingapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // Find the BottomNavigationView in the layout
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)

        // Set the background to null
        bottomNavigationView.background = null

        // Disable the third item in the menu
        bottomNavigationView.menu.getItem(2).isEnabled = false



    }
}