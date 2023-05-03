package com.example.mobileassfundraisingapp.admin

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import com.example.mobileassfundraisingapp.databinding.ActivityAdminUpdateBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream
import java.io.File

private const val REQUEST_CODE = 42
class AdminUpdate : AppCompatActivity() {

    private lateinit var binding: ActivityAdminUpdateBinding
    private lateinit var database: DatabaseReference
    private lateinit var Dialog: ProgressDialog
    private lateinit var username: String
    private lateinit var fileID: String
    private var ImageUri : Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        // Binding declaration
        binding = ActivityAdminUpdateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val nameAdmin: TextView = binding.editAdminName
        val emailAdmin: TextView = binding.editAdminEmail
        val phoneAdmin: TextView = binding.editAdminPhone

        val bundle: Bundle? = intent.extras
        username = intent.getStringExtra("username").toString()


        database = FirebaseDatabase.getInstance().getReference("users")
        database.child(username).get().addOnSuccessListener {
            val retrieved = it.value as Map<String, String>
            nameAdmin.text = retrieved.get("name")
            emailAdmin.text = retrieved.get("email")
            phoneAdmin.text = retrieved.get("phone")
            fileID = retrieved.get("profilePic").toString()
            val storageReference = FirebaseStorage.getInstance().getReference("images/"+retrieved.get("profilePic"))
            val localFile = File.createTempFile("images","jpg")
            storageReference.getFile(localFile).addOnSuccessListener {

                binding.editImage.setImageURI(localFile.toUri())
            }
        }

        binding.editImage.setOnClickListener{

            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT

            startActivityForResult(intent, 100)
        }

        binding.textView2.visibility = View.GONE

        binding.btnUpdate.setOnClickListener {
            var error: Boolean = false
            // Get data
            val getName = binding.editAdminName.text.toString()
            if (getName.length == 0){
                binding.textView2.visibility = View.VISIBLE
                binding.textView2.text = "Empty"
                error = true
            }else{
                binding.textView2.visibility = View.GONE
            }

            val getEmail = binding.editAdminEmail.text.toString()
            val getPhone = binding.editAdminPhone.text.toString()

            if (!error)
                updateData(getName, getEmail, getPhone, username)

        }

        binding.btnDelete.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setMessage("Are you sure you want to Delete?")
                .setCancelable(true)
                .setPositiveButton("Yes"){
                        dialog, id ->
                    deleteData(username)
                }.setNegativeButton("No"){
                        dialog, id ->
                    Toast.makeText(this,"Cancel Delete", Toast.LENGTH_SHORT).show()
                }
            val alert = builder.create()
            alert.show()
        }



        Dialog = ProgressDialog(this)
        Dialog.setTitle("Please wait")
        Dialog.setCanceledOnTouchOutside(false)

    }

    private fun updateData(name: String, email: String, phone: String, uid: String) {

        database = FirebaseDatabase.getInstance().getReference("users")
        val user = mapOf<String, String>(
            "name" to name,
            "email" to email,
            "phone" to phone,
        )
        Dialog.setMessage("Updating Users...")
        Dialog.show()
        if (ImageUri != null){
            val storageReference = FirebaseStorage.getInstance().getReference("images/$fileID")

            storageReference.delete()
            storageReference.putFile(ImageUri!!)
        }
        database.child(username).updateChildren(user).addOnSuccessListener {
            binding.editAdminName.text.clear()
            binding.editAdminEmail.text.clear()
            binding.editAdminPhone.text.clear()
            Dialog.dismiss()
            finish()
            Toast.makeText(this, "Successfully Update", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {
            Dialog.dismiss()
            Toast.makeText(this, "Failed to update", Toast.LENGTH_SHORT).show()
        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 100 && resultCode == RESULT_OK){
            ImageUri = data?.data!!
            binding.editImage.setImageURI(ImageUri)
        }
    }

    fun getImageUri(inContext: Context, inImage: Bitmap): Uri {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(
            inContext.getContentResolver(),
            inImage,
            "Title",
            null
        )
        return Uri.parse(path)
    }

    private fun deleteData(uid: String) {

        database = FirebaseDatabase.getInstance().getReference("users")

        database.child(username).removeValue().addOnSuccessListener {
            binding.editAdminName.text.clear()
            binding.editAdminEmail.text.clear()
            binding.editAdminPhone.text.clear()
            Dialog.dismiss()
            finish()
            Toast.makeText(this, "Successfully Delete", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {
            Dialog.dismiss()
            Toast.makeText(this, "Failed to Delete", Toast.LENGTH_SHORT).show()
        }
    }


    }
