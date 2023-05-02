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
    private lateinit var progressDialog: ProgressDialog
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
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please wait")
        progressDialog.setCanceledOnTouchOutside(false)


        binding.backBtn.setOnClickListener {
            finish()
        }



        binding.btnRegister.setOnClickListener {
            validateData()
        }

        binding.btnUploadPicture.setOnClickListener {
            selectImage()
        }


        binding.btnTakePicture.setOnClickListener {
            //erm i need to refigure because kotlin seem change much
            //you no rebuild so it error
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

        // Step 2: Validate data
        if(name.isEmpty()){
            // Empty name
            Toast.makeText(this, "Enter your username...", Toast.LENGTH_SHORT).show()
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            // Invalid email address pattern
            Toast.makeText(this, "Invalid email pattern...", Toast.LENGTH_SHORT).show()
        }
        else if(password.isEmpty()){
            // Empty password
            Toast.makeText(this, "Enter your password...", Toast.LENGTH_SHORT).show()
        }
        else if(cPassword.isEmpty()){
            // Empty confirm password
            Toast.makeText(this, "Enter confirm password...", Toast.LENGTH_SHORT).show()
        }
        else if(password != cPassword){
            // Password mismatch
            Toast.makeText(this, "Password not match...", Toast.LENGTH_SHORT).show()
        }
//        else if(binding.imageView.getDrawable() == null){
//            // No image taken
//            Toast.makeText(this, "Capture an image...", Toast.LENGTH_SHORT).show()
//        }
        else if(contactNo.isEmpty()){
            // Empty contact no
            Toast.makeText(this, "Enter contact number...", Toast.LENGTH_SHORT).show()
        }
        else if(!contactNo.matches(".*[0-9].*".toRegex())){
            // Contact no must be number
            Toast.makeText(this, "Number only for contact number..", Toast.LENGTH_SHORT).show()
        }
        else if(contactNo.length > 11) {
            Toast.makeText(this, "Contact no must not be exceed...", Toast.LENGTH_SHORT).show()
        }
        else if(contactNo.length < 10){
            Toast.makeText(this, "Invalid contact no...", Toast.LENGTH_SHORT).show()
        }
        else{
            createUserAccount()
        }
    }

    private fun createUserAccount() {
        // Step 3: Create account - Firebase auth

        // Show progress
        progressDialog.setMessage("Creating Account...")
        progressDialog.show()

        // Create user in firebase
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                // Account created
                // Add user data to db
                updateUserInfo()

            }
            .addOnFailureListener {
                // Account fail to create
                progressDialog.dismiss()
                //can take picture for me the error message the screen too blur haha
                Toast.makeText(this, "Fail to create due to ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun updateUserInfo() {
        // Step 4: Save user info - Firebase Realtime Database
        progressDialog.setMessage("Saving user info...")

        // Timestamp
        val timestamp = System.currentTimeMillis()

        // Get current user uid
        val uid = firebaseAuth.uid

        // Setup data to add in db
        val hashMap: HashMap<String, Any?> = HashMap()
        hashMap["username"] = uid
        hashMap["email"] = email
        hashMap["name"] = name
        hashMap["phone"] = contactNo
        hashMap["password"] = password
        hashMap["profilePic"] = FILE_NAME
        hashMap["role"] = "admin" //data -> user/admin



        uploadImage()

        // Set data to db
        val ref = FirebaseDatabase.getInstance().getReference("users") //change the table name of here
        ref.child(uid!!)
            .setValue(hashMap)
            .addOnSuccessListener {
                // user info saved
                progressDialog.dismiss()
                Toast.makeText(this, "Account created successfully ", Toast.LENGTH_SHORT).show()
//                val intent = Intent(this@RegisterActivity, SuccessScanActivity::class.java)
//                intent.putExtra("imguri", ImageUri)
//                startActivity(intent)
                //ok worked
                //ok i try on my phone see
                //ok
                startActivity(Intent(this@RegisterAdmin, RegisterAdmin::class.java))
            }
            .addOnFailureListener {
                // Fail to add data to db
                progressDialog.dismiss()
                Toast.makeText(this, "Fail to save the info  ${it.message}", Toast.LENGTH_SHORT).show()
            }

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