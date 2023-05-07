package com.example.mobileassfundraisingapp.admin

import android.app.Activity
import android.app.ProgressDialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mobileassfundraisingapp.databinding.ActivityAdminRegisterBinding
import com.example.mobileassfundraisingapp.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

private const val REQUEST_CODE = 42
private lateinit var photoFile: File

class RegisterAdmin : AppCompatActivity() {

    private lateinit var  binding: ActivityAdminRegisterBinding
    private lateinit var Dialog: ProgressDialog
    private lateinit var firebaseAuth: FirebaseAuth




    val formatter = SimpleDateFormat("yyyy_MM_dd_mm_ss", Locale.getDefault())
    val now = Date()
    val FILE_NAME = formatter.format(now)

    lateinit var ImageUri : Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.e("ERR21","test");

        binding = ActivityAdminRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)


        firebaseAuth = FirebaseAuth.getInstance()
        Dialog = ProgressDialog(this)
        Dialog.setTitle("Please wait")
        Dialog.setCanceledOnTouchOutside(false)



        binding.btnRegister.setOnClickListener {
            validateData()
        }

        binding.btnUploadPicture.setOnClickListener {
            selectImage()
        }


        binding.btnTakePicture.setOnClickListener {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            photoFile = getPhotoFile(FILE_NAME)
            try{

                startActivityForResult(intent, REQUEST_CODE)


            }catch (e: ActivityNotFoundException){
                Toast.makeText(this, "Unable to open camera", Toast.LENGTH_SHORT).show()
            }
        }


    }

    override fun onActivityResult(requestcode: Int, resultCode: Int, data: Intent?) {
        if(requestcode == REQUEST_CODE && resultCode == Activity.RESULT_OK){
            val takenImage = data?.extras?.get("data") as Bitmap

            binding.imageView.setImageBitmap(takenImage)

            ImageUri = getImageUri(applicationContext, takenImage)

        }else{
            super.onActivityResult(requestcode, resultCode, data)
        }

        if (requestcode == 100 && resultCode == RESULT_OK){
            ImageUri = data?.data!!
            binding.imageView.setImageURI(ImageUri)
        }
    }



    private var name = ""
    private var email = ""
    private var password = ""
    private var contactNo = ""

    private fun validateData() {
        // Step 1: Input data
        name = binding.nameEt.text.toString().trim()
        email = binding.emailEt.text.toString().trim()
        password = binding.passwordEt.text.toString().trim()
        contactNo = binding.contactNumberEt.text.toString()

        val cPassword = binding.cPasswordEt.text.toString().trim()

        //Validate data
        if(name.isEmpty()){

            Toast.makeText(this, "Please Enter the username", Toast.LENGTH_SHORT).show()
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){

            Toast.makeText(this, "Please enter the correct email pattern", Toast.LENGTH_SHORT).show()
        }
        else if(password.isEmpty()){

            Toast.makeText(this, "Please enter your password", Toast.LENGTH_SHORT).show()
        }
        else if(cPassword.isEmpty()){

            Toast.makeText(this, "Please enter confirm password", Toast.LENGTH_SHORT).show()
        }
        else if(password != cPassword){

            Toast.makeText(this, "Password not match", Toast.LENGTH_SHORT).show()
        }
        else if(binding.imageView.getDrawable() == null){

            Toast.makeText(this, "Capturing an image", Toast.LENGTH_SHORT).show()
        }
        else if(contactNo.isEmpty()){

            Toast.makeText(this, "Please enter contact number", Toast.LENGTH_SHORT).show()
        }
        else if(!contactNo.matches(".*[0-9].*".toRegex())){

            Toast.makeText(this, "Please enter number only ", Toast.LENGTH_SHORT).show()
        }
        else if(contactNo.length > 11) {
            Toast.makeText(this, "Contact no no more than 11 digit", Toast.LENGTH_SHORT).show()
        }
        else if(contactNo.length < 10){
            Toast.makeText(this, "Invalid contact no", Toast.LENGTH_SHORT).show()
        }
        else{
            createAccount()
        }
    }

    private fun createAccount() {

        Dialog.setMessage("Creating Account")
        Dialog.show()


        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {

                updateInfo()

            }
            .addOnFailureListener {
                Dialog.dismiss()
                Toast.makeText(this, "Fail to create because ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun updateInfo() {
        Dialog.setMessage("Saving info")


        val timestamp = System.currentTimeMillis()


        val uid = binding.editUsername.text.toString().trim()


        val hashMap: HashMap<String, Any?> = HashMap()
        hashMap["username"] = uid
        hashMap["email"] = email
        hashMap["name"] = name
        hashMap["phone"] = contactNo
        hashMap["password"] = password
        hashMap["profilePic"] = FILE_NAME
        hashMap["role"] = "admin"



        uploadImage()

        // Set data to db
        val ref = FirebaseDatabase.getInstance().getReference("users") //change the table name of here
        ref.child(uid)
            .setValue(hashMap)
            .addOnSuccessListener {
                Dialog.dismiss()
                Toast.makeText(this, "Account created successfully ", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this@RegisterAdmin, RegisterAdmin::class.java))
            }
            .addOnFailureListener {
                Dialog.dismiss()
                Toast.makeText(this, "Fail to save the info  ${it.message}", Toast.LENGTH_SHORT).show()
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

    private fun selectImage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT

        startActivityForResult(intent, 100)
    }

    private fun getPhotoFile(fileName: String): File {
        val storageDirectory = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(fileName, ".jpg", storageDirectory)
    }

    private fun uploadImage() {

        val storageReference = FirebaseStorage.getInstance().getReference("images/$FILE_NAME")

        storageReference.putFile(ImageUri).
        addOnSuccessListener {
        }.addOnFailureListener {
        }
    }


}