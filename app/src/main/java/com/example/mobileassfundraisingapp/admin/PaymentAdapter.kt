package com.example.mobileassfundraisingapp.admin

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mobileassfundraisingapp.R

class PaymentAdapter(
    private val mContext: Context,
    private var mPaymentList: List<PaymentDataClass>,
    private var mListener: OnItemClickListener? = null
) : RecyclerView.Adapter<PaymentAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(mContext).inflate(R.layout.item_payment_history, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val paymentData = mPaymentList[position]
        holder.mTextViewDate.text = paymentData.timestamp
        holder.mTextViewAmount.text = "RM" + paymentData.donationAmount.toString()

        holder.itemView.setOnClickListener {
            mListener?.onItemClick(position)
        }
    }

    override fun getItemCount(): Int {
        return mPaymentList.size
    }

    fun updatePaymentList(newList: List<PaymentDataClass>) {
        mPaymentList = newList
        notifyDataSetChanged()
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        mListener = listener
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mTextViewDate: TextView = itemView.findViewById(R.id.text_view_payment_date)
        val mTextViewAmount: TextView = itemView.findViewById(R.id.text_view_payment_amount)
        val mImageViewStatus: ImageView = itemView.findViewById(R.id.image_view_payment_status)
    }
}

