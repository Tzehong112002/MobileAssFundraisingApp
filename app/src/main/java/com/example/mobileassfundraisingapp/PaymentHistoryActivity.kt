package com.example.mobileassfundraisingapp

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mobileassfundraisingapp.admin.PaymentAdapter
import com.example.mobileassfundraisingapp.admin.PaymentDataClass
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class PaymentHistoryFragment : Fragment() {

    private lateinit var mDatabase: DatabaseReference
    private lateinit var mAdapter: PaymentAdapter
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mPaymentList: List<PaymentDataClass>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.payment_history, container, false)

        // Get the username from the arguments
        val username = arguments?.getString("username")
        Log.d(TAG, "Username: $username")

        // Set up the recycler view and adapter
        mDatabase = FirebaseDatabase.getInstance().reference.child("payments")
        mPaymentList = emptyList()
        mAdapter = PaymentAdapter(requireContext(), mPaymentList)
        mRecyclerView = view.findViewById(R.id.recycler_view_payment_history)
        mRecyclerView.setHasFixedSize(true)
        mRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        mRecyclerView.adapter = mAdapter

   
        // Retrieve payment history for the current user
        val query = mDatabase.orderByChild("username").equalTo(username)
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                Log.d(TAG, "Data changed")
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

        return view
    }

    companion object {
        private const val TAG = "PaymentHistoryFragment"

        /**
         * Factory method to create a new instance of the PaymentHistoryFragment.
         * @param username The username of the current user.
         * @return A new instance of PaymentHistoryFragment.
         */
        @JvmStatic
        fun newInstance(username: String?): PaymentHistoryFragment {
            val fragment = PaymentHistoryFragment()
            val args = Bundle()
            args.putString("username", username)
            fragment.arguments = args
            return fragment
        }
    }
}

