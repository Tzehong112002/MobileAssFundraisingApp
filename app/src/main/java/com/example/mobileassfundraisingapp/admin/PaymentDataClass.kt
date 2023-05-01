package com.example.mobileassfundraisingapp.admin

class PaymentDataClass {
    var nameOnCard: String? = null
    var cardNumber: String? = null
    var expiry: String? = null
    var cvc: String? = null
    var donationAmount: Double? = null
    var timestamp: String? = null
    var username: String? = null
    var key: String? = null
    var eventId: String? = null // declare property here

    constructor(
        nameOnCard: String?,
        cardNumber: String?,
        expiry: String?,
        cvc: String?,
        donationAmount: Double?,
        timestamp: String?,
        username: String?,
        key: String?
    ) {
        this.nameOnCard = nameOnCard
        this.cardNumber = cardNumber
        this.expiry = expiry
        this.cvc = cvc
        this.donationAmount = donationAmount
        this.timestamp = timestamp
        this.username = username
        this.key = key
    }

    constructor() {}
}



