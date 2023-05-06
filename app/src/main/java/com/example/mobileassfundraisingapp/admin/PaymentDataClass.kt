package com.example.mobileassfundraisingapp.admin

import android.os.Parcel
import android.os.Parcelable

class PaymentDataClass() : Parcelable {
    var nameOnCard: String? = null
    var cardNumber: String? = null
    var expiry: String? = null
    var cvc: String? = null
    var donationAmount: Double? = null
    var timestamp: String? = null
    var username: String? = null
    var key: String? = null
    var eventId: String? = null

    constructor(
        nameOnCard: String?,
        cardNumber: String?,
        expiry: String?,
        cvc: String?,
        donationAmount: Double?,
        timestamp: String?,
        username: String?,
        key: String?,
        eventId: String?
    ) : this() {
        this.nameOnCard = nameOnCard
        this.cardNumber = cardNumber
        this.expiry = expiry
        this.cvc = cvc
        this.donationAmount = donationAmount
        this.timestamp = timestamp
        this.username = username
        this.key = key
        this.eventId = eventId
    }

    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(nameOnCard)
        parcel.writeString(cardNumber)
        parcel.writeString(expiry)
        parcel.writeString(cvc)
        parcel.writeValue(donationAmount)
        parcel.writeString(timestamp)
        parcel.writeString(username)
        parcel.writeString(key)
        parcel.writeString(eventId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PaymentDataClass> {
        override fun createFromParcel(parcel: Parcel): PaymentDataClass {
            return PaymentDataClass(parcel)
        }

        override fun newArray(size: Int): Array<PaymentDataClass?> {
            return arrayOfNulls(size)
        }
    }
}




