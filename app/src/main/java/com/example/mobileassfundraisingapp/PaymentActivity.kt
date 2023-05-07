package com.example.mobileassfundraisingapp;

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.isDigitsOnly
import com.example.mobileassfundraisingapp.admin.DonorList
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.*



class PaymentActivity : AppCompatActivity() {

    private lateinit var cardNumberEditText: TextInputEditText
    private lateinit var expiryEditText: TextInputEditText
    private lateinit var cvcEditText: TextInputEditText
    private lateinit var nameOnCardEditText: EditText
    private lateinit var donationAmountEditText: EditText
    private lateinit var donateButton: Button
    private lateinit var cancelButton: Button

    private lateinit var username: String
    private lateinit var eventId: String
    private lateinit var database: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.payment)

        // Retrieve userId and eventId from the previous activity
        username = intent?.getStringExtra("username") ?: ""

        eventId = intent.getStringExtra("eventId") ?: ""

        // Initialize Firebase Realtime Database
        database = FirebaseDatabase.getInstance()

        // Find views by IDs
        cardNumberEditText = findViewById(R.id.editText_cardNumber)
        expiryEditText = findViewById(R.id.editText_expiry)
        cvcEditText = findViewById(R.id.editText_cvc)
        nameOnCardEditText = findViewById(R.id.editText_nameOnCard)
        donationAmountEditText = findViewById(R.id.editText_donationAmount)
        donateButton = findViewById(R.id.button_donate)
        cancelButton = findViewById(R.id.button_cancel)

        // Set click listener for the donate button
        donateButton.setOnClickListener {
            // Get input values from the UI
            val cardNumber = cardNumberEditText.text.toString()
            val expiry = expiryEditText.text.toString()
            val cvc = cvcEditText.text.toString()
            val nameOnCard = nameOnCardEditText.text.toString()
            val donationAmount = donationAmountEditText.text.toString()

            // Validate input values
            if (cardNumber.isEmpty() || cardNumber.length != 16 || !cardNumber.isDigitsOnly()) {
                cardNumberEditText.error = "Please enter a valid 16-digit card number"
                return@setOnClickListener
            }

            if (expiry.isEmpty() || !expiry.matches(Regex("^\\d{2}/\\d{2}$"))) {
                expiryEditText.error = "Please enter a valid expiry date in MM/YY format"
                return@setOnClickListener
            }

            if (cvc.isEmpty() || cvc.length != 3 || !cvc.isDigitsOnly()) {
                cvcEditText.error = "Please enter a valid 3-digit CVC number"
                return@setOnClickListener
            }

            if (nameOnCard.isEmpty()) {
                nameOnCardEditText.error = "Please enter a name on the card"
                return@setOnClickListener
            }

            if (donationAmount.isEmpty() || donationAmount.toDoubleOrNull() == null) {
                donationAmountEditText.error = "Please enter a valid donation amount"
                return@setOnClickListener
            }

            // Get current date and time
            val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            val currentTime = dateFormat.format(Date())

            // Create a payment object
            val payment = hashMapOf(
                "username" to username,
                "eventId" to eventId,
                "cardNumber" to cardNumber,
                "expiry" to expiry,
                "cvc" to cvc,
                "nameOnCard" to nameOnCard,
                "donationAmount" to donationAmount.toDouble(),
                "timestamp" to currentTime
            )

            // Insert payment data into Firebase Realtime Database
            val paymentReference = database.reference.child("payments").push()
            paymentReference.setValue(payment)
                .addOnSuccessListener {
                    Toast.makeText(this, "Payment successful", Toast.LENGTH_SHORT).show()
                    finish()
                }
                .addOnFailureListener {
                    Log.e(TAG, "Error adding payment", it)
                    Toast.makeText(this, "Payment failed", Toast.LENGTH_SHORT).show()
                }
        }


        cancelButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("username", username)
            intent.putExtra("eventId", eventId)
            intent.putExtra("fragment", "event_details_user")
            startActivity(intent)
        }




    }

    companion object {
        private const val TAG = "PaymentActivity"
    }
}

