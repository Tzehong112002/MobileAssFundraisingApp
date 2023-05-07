package com.example.mobileassfundraisingapp.admin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import com.example.mobileassfundraisingapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import androidx.appcompat.app.AlertDialog


class PaymentDetails : AppCompatActivity() {

    private lateinit var mDatabase: DatabaseReference
    private lateinit var mPaymentKey: String
    private lateinit var mNameOnCard: EditText
    private lateinit var mCardNumber: EditText
    private lateinit var mDonationAmount: EditText
    private lateinit var mTimestamp: TextView
    private lateinit var mUsername: EditText
    private lateinit var mEventId: TextView
    private lateinit var mSaveButton: Button
    private lateinit var mDeleteButton: Button
    private lateinit var mCvc: String
    private lateinit var mExpiry: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.payment_details)

        mDatabase = FirebaseDatabase.getInstance().reference
        mPaymentKey = intent.getStringExtra("payment_key")!!

        mNameOnCard = findViewById<EditText>(R.id.name_on_card) ?: throw IllegalStateException("Name on card view not found")
        mCardNumber = findViewById<EditText>(R.id.card_number) ?: throw IllegalStateException("Card number view not found")
        mDonationAmount = findViewById<EditText>(R.id.donation_amount) ?: throw IllegalStateException("Donation amount view not found")
        mTimestamp = findViewById<TextView>(R.id.timestamp) ?: throw IllegalStateException("Donated Time view not found")
        mUsername = findViewById<EditText>(R.id.username) ?: throw IllegalStateException("Username view not found")
        mEventId = findViewById<TextView>(R.id.event_id) ?: throw IllegalStateException("Event ID view not found")
        mSaveButton = findViewById<Button>(R.id.btn_edit_payment) ?: throw IllegalStateException("Save button not found")
        mDeleteButton = findViewById<Button>(R.id.btn_delete_payment) ?: throw IllegalStateException("Delete button not found")


        mDatabase.child("payments").child(mPaymentKey)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val payment = dataSnapshot.getValue(PaymentDataClass::class.java)
                    payment?.key = dataSnapshot.key

                    // Update the UI with the payment details
                    mNameOnCard.setText(payment?.nameOnCard)
                    mCardNumber.setText(payment?.cardNumber)
                    mDonationAmount.setText(payment?.donationAmount.toString())
                    mUsername.setText(payment?.username)
                    mEventId.text = payment?.eventId ?: ""
                    mTimestamp.text = payment?.timestamp.toString()
                    mCvc = payment?.cvc ?: ""
                    mExpiry = payment?.expiry ?: ""


                    mDeleteButton.setOnClickListener {
                        val payment = PaymentDataClass()
                        payment.key = mPaymentKey
                        deletePayment(payment)
                    }

                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Handle error
                }
            })

        mSaveButton.setOnClickListener {
            if (validateInputs()) {
                savePayment()
            }
        }

        // Check for null values
        if (mNameOnCard == null || mCardNumber == null || mDonationAmount == null || mUsername == null || mEventId == null || mSaveButton == null) {
            throw IllegalStateException("One or more views are null")
        }


    }

    private fun validateInputs(): Boolean {
        val nameOnCard = mNameOnCard.text.toString().trim()
        val cardNumber = mCardNumber.text.toString().trim()
        val donationAmount = mDonationAmount.text.toString().trim()
        val username = mUsername.text.toString().trim()

        if (nameOnCard.isEmpty()) {
            mNameOnCard.error = "Name on card is required"
            return false
        }

        if (cardNumber.isEmpty()) {
            mCardNumber.error = "Card number is required"
            return false
        }

        if (donationAmount.isEmpty()) {
            mDonationAmount.error = "Donation amount is required"
            return false
        }

        if (username.isEmpty()) {
            mUsername.error = "Username is required"
            return false
        }

        return true
    }


    private fun savePayment() {
        val nameOnCard = mNameOnCard.text.toString()
        val cardNumber = mCardNumber.text.toString()
        val expiry = mExpiry
        val cvc = mCvc
        val donationAmount = mDonationAmount.text.toString().toDouble()
        val timestamp = mTimestamp.text.toString()
        val username = mUsername.text.toString()
        val eventId = mEventId.text.toString()

        val payment = PaymentDataClass(
            nameOnCard = nameOnCard,
            cardNumber = cardNumber,
            expiry = expiry,
            cvc = cvc,
            donationAmount = donationAmount,
            timestamp = timestamp,
            username = username,
            key = "",
            eventId = eventId
        )

        mDatabase.child("payments").child(mPaymentKey)
            .setValue(payment)
            .addOnSuccessListener {
                // Payment successfully saved
                finish()
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "Error saving payment details", e)
                // Handle error
            }
        val intent = Intent(this@PaymentDetails, DonorList::class.java)
        startActivity(intent)
        finish()
    }

    private fun deletePayment(payment: PaymentDataClass) {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("Are you sure you want to delete this payment?")
            .setPositiveButton("Yes") { dialog, _ ->
                mDatabase.child("payments").child(payment.key!!)
                    .removeValue()
                    .addOnSuccessListener {
                        // Payment successfully deleted
                        val intent = Intent(this@PaymentDetails, DonorList::class.java)
                        startActivity(intent)
                        finish()
                    }
                    .addOnFailureListener {
                        // Handle error
                    }
            }
            .setNegativeButton("No") { dialog, _ ->
                // User clicked No button, do nothing
                dialog.dismiss()
            }
        builder.show()
    }



    companion object {
        private const val TAG = "PaymentDetails"
    }
}
