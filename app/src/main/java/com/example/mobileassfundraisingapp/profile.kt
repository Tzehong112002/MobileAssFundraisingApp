package com.example.mobileassfundraisingapp

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener

class profile : Fragment() {

    private lateinit var profileName: TextView
    private lateinit var profileEmail: TextView
    private lateinit var profileUsername: TextView
    private lateinit var profilePassword: TextView
    private lateinit var titleName: TextView
    private lateinit var titleUsername: TextView
    private lateinit var editProfile: Button



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        profileName = view.findViewById(R.id.profileName)
        profileEmail = view.findViewById(R.id.profileEmail)
        profileUsername = view.findViewById(R.id.profileUsername)
        profilePassword = view.findViewById(R.id.profilePassword)
        titleName = view.findViewById(R.id.titleName)
        titleUsername = view.findViewById(R.id.titleUsername)
        editProfile = view.findViewById(R.id.editButton)

        showAllUserData()

        editProfile.setOnClickListener {
            passUserData()
        }

        return view
    }



    private fun showAllUserData() {
        val intent = activity?.intent
        val userUsername = intent?.getStringExtra("username")


        val reference: DatabaseReference = FirebaseDatabase.getInstance().getReference("users")
        val checkUserDatabase: Query = reference.orderByChild("username").equalTo(userUsername)

        checkUserDatabase.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {

                    val nameFromDB = snapshot.child(userUsername.toString()).child("name").getValue(String::class.java)
                    val emailFromDB = snapshot.child(userUsername.toString()).child("email").getValue(String::class.java)
                    val usernameFromDB = snapshot.child(userUsername.toString()).child("username").getValue(String::class.java)
                    val passwordFromDB = snapshot.child(userUsername.toString()).child("password").getValue(String::class.java)

                    titleName.text = nameFromDB
                    titleUsername.text = usernameFromDB
                    profileName.text = nameFromDB
                    profileEmail.text = emailFromDB
                    profileUsername.text = usernameFromDB
                    profilePassword.text = passwordFromDB
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }


    private fun passUserData() {
        val userUsername = profileUsername.text.toString().trim()
        val reference: DatabaseReference = FirebaseDatabase.getInstance().getReference("users")
        val checkUserDatabase: Query = reference.orderByChild("username").equalTo(userUsername)
        checkUserDatabase.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val nameFromDB = snapshot.child(userUsername).child("name").getValue(String::class.java)
                    val emailFromDB = snapshot.child(userUsername).child("email").getValue(String::class.java)
                    val usernameFromDB = snapshot.child(userUsername).child("username").getValue(String::class.java)
                    val passwordFromDB = snapshot.child(userUsername).child("password").getValue(String::class.java)


                    val bundle = Bundle()
                    bundle.putString("name", nameFromDB)
                    bundle.putString("email", emailFromDB)
                    bundle.putString("username", usernameFromDB)
                    bundle.putString("password", passwordFromDB)


                    val editProfileFragment = edit_profile()
                    editProfileFragment.arguments = bundle

                    val fragmentManager = activity?.supportFragmentManager
                    val fragmentTransaction = fragmentManager?.beginTransaction()
                    fragmentTransaction?.replace(R.id.fragment_container, editProfileFragment)
                    fragmentTransaction?.addToBackStack(null)
                    fragmentTransaction?.commit()
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }
}
