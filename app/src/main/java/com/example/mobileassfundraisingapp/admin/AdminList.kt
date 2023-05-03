package com.example.mobileassfundraisingapp.admin

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mobileassfundraisingapp.Adapter.AdminAdapter
import com.example.mobileassfundraisingapp.DataClass.admin
import com.example.mobileassfundraisingapp.R
import com.example.mobileassfundraisingapp.databinding.ActivityAdminListBinding
import com.google.firebase.database.*
import java.io.File

class AdminList : AppCompatActivity() {
    // binding
    private lateinit var binding: ActivityAdminListBinding


    private lateinit var uAList: ArrayList<admin>
    private lateinit var dbref : DatabaseReference
    private lateinit var uRView: RecyclerView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Declare binding
        binding = ActivityAdminListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        uRView = binding.adminList
        uRView.layoutManager = LinearLayoutManager(this)
        uRView.setHasFixedSize(true)

        uAList = arrayListOf<admin>()

        binding.addUserBtn.setOnClickListener {
            startActivity(Intent(this@AdminList, RegisterAdmin ::class.java))
        }

        getRData()
    }

    private fun getRData() {
        dbref = FirebaseDatabase.getInstance().getReference("users")

        dbref.orderByChild("role").equalTo("admin").addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    uAList.clear()
                    for (newsSnapshot in snapshot.children) {
                        val user = newsSnapshot.getValue(admin::class.java)
                        uAList.add(user!!)
                    }

                    var adapter = AdminAdapter(uAList)

                    uRView.adapter = adapter
                    adapter.setOnClickListener(object : AdminAdapter.onItemClickListener{
                        override fun onItemClick(set: Int) {

                            var username = uAList[set].username
                            var name = uAList[set].name
                            var email = uAList[set].email
                            var phone = uAList[set].phone
                            var picture = uAList[set].profilePic

                            val intent = Intent(this@AdminList, AdminUpdate::class.java)
                            intent.putExtra("username", username)
                            intent.putExtra("name", name)
                            intent.putExtra("email", email)
                            intent.putExtra("phone", phone)
                            intent.putExtra("picture", picture)
                            startActivity(intent)

                        }
                    })
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

}