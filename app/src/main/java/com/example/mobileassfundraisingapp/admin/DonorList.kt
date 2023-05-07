package com.example.mobileassfundraisingapp.admin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mobileassfundraisingapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class DonorList : AppCompatActivity() {

    private lateinit var mDatabase: DatabaseReference
    private lateinit var mAdapter: PaymentAdapter
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mPaymentList: List<PaymentDataClass>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.payment_history)

        // Set up the recycler view and adapter
        mDatabase = FirebaseDatabase.getInstance().reference.child("payments")
        mPaymentList = emptyList()
        mAdapter = PaymentAdapter(this, mPaymentList)
        mRecyclerView = findViewById(R.id.recycler_view_payment_history)
        mRecyclerView.setHasFixedSize(true)
        mRecyclerView.layoutManager = LinearLayoutManager(this)
        mRecyclerView.adapter = mAdapter

        // Retrieve all payment data
        val query = mDatabase.orderByChild("timestamp")
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val paymentList = mutableListOf<PaymentDataClass>()
                for (postSnapshot in dataSnapshot.children) {
                    val payment = postSnapshot.getValue(PaymentDataClass::class.java)
                    payment?.key = postSnapshot.key
                    if (payment != null) {
                        paymentList.add(payment)
                    }
                }
                mPaymentList = paymentList
                mAdapter.updatePaymentList(mPaymentList)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle error
            }
        })

        // Set up item click listener
        mAdapter.setOnItemClickListener(object : PaymentAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                val payment = mPaymentList[position]
                val intent = Intent(this@DonorList, PaymentDetails::class.java)
                intent.putExtra("payment_key", payment.key)
                startActivity(intent)
            }
        })
    }

    companion object {
        private const val TAG = "DonorList"
    }
}

