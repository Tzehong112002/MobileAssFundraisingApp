package com.example.mobileassfundraisingapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import androidx.fragment.app.Fragment
import com.example.mobileassfundraisingapp.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var binding :ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set up initial fragment
        replaceFragment(Home())

        // Find the BottomNavigationView in the layout
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)

        // Set the background to null
        bottomNavigationView.background = null

        // Disable the third item in the menu
        bottomNavigationView.menu.getItem(2).isEnabled = false

        // Set up listener for BottomNavigationView items
        bottomNavigationView.setOnItemSelectedListener { menuItem ->
            when(menuItem.itemId){
                R.id.miHome -> replaceFragment(Home())
                R.id.miProfile -> replaceFragment(profile())
                R.id.miSettings -> replaceFragment(settings())
                R.id.miSearch -> replaceFragment(search())
            }
            true
        }

        // Set up listener for FAB click
        binding.fab.setOnClickListener {
            replaceFragment(add())
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_container, fragment)
        fragmentTransaction.commit()
    }
}
