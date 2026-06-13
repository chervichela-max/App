package com.carsharing.app.models

import java.util.*

data class PaymentMethod(
    val id: String,
    val cardNumber: String,
    val cardHolder: String,
    val expiryDate: String,
    val isDefault: Boolean = false
)

data class PaymentTransaction(
    val id: String,
    val amount: Double,
    val date: Date,
    val type: TransactionType,
    val status: TransactionStatus,
    val description: String
)

enum class TransactionType {
    RIDE_PAYMENT, TOP_UP, BONUS, PENALTY
}

enum class TransactionStatus {
    SUCCESS, PENDING, FAILED
}
