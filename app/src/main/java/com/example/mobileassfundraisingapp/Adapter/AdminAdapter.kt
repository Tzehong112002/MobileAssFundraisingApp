package com.example.mobileassfundraisingapp.Adapter

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mobileassfundraisingapp.DataClass.admin
import com.example.mobileassfundraisingapp.R
import com.google.firebase.storage.FirebaseStorage
import java.io.File

class AdminAdapter (private val uList : ArrayList<admin>) : RecyclerView.Adapter<AdminAdapter.MyViewHolder>(){
    private lateinit var mList : onItemClickListener

    interface onItemClickListener {
        fun onItemClick (position: Int)
    }

    fun setOnClickListener(listener: onItemClickListener){
        mList = listener
    }

    fun deleteItem(i : Int){
        var size = uList.size
        uList.removeAt(i)

        uList.clear()
        notifyDataSetChanged()
    }

    fun addItem(int : Int, admin : admin){
        uList.add(int, admin)
        notifyDataSetChanged()
    }

    public class MyViewHolder(iv : View, listener: AdminAdapter.onItemClickListener) : RecyclerView.ViewHolder(iv) {
        val name : TextView = iv.findViewById(R.id.nameTv)
        val email : TextView = iv.findViewById(R.id.emailTv)
        val phone : TextView = iv.findViewById(R.id.phoneTv)
        val picture: ImageView = iv.findViewById(R.id.userImage)

        init {
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.activity_admin_record_layout ,parent,false)
        return AdminAdapter.MyViewHolder(itemView, mList)
    }

    override fun onBindViewHolder(hld: MyViewHolder, position: Int) {
        val user : admin = uList[position]
        hld.name.text = user.name
        hld.email.text = user.email
        hld.phone.text = user.phone.toString()

        val storageRef = FirebaseStorage.getInstance().reference.child("images/${user.profilePic}")
        val localfile = File.createTempFile("tempImage", "jpeg")
        storageRef.getFile(localfile).addOnSuccessListener {
            val bitmap = BitmapFactory.decodeFile(localfile.absolutePath)
            hld.picture.setImageBitmap(bitmap)
        }
    }

    override fun getItemCount(): Int {
        return uList.size
    }


}