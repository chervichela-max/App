package com.carsharing.app.models

import java.util.*

data class InstallmentFine(
    val fineId: String,
    val uin: String,
    val articleCode: String,
    val originalAmount: Int,
    val remainingAmount: Int,
    val dailyPayment: Int = 300,
    val startDate: Date,
    val endDate: Date,
    val status: InstallmentStatus,
    val offensePlace: String,
    val offenseDate: Date
)

enum class InstallmentStatus {
    ACTIVE, COMPLETED, OVERDUE, CANCELLED
}

data class InstallmentPayment(
    val id: String,
    val fineId: String,
    val date: Date,
    val amount: Int,
    val previousRemaining: Int,
    val newRemaining: Int,
    val status: PaymentStatus
)

enum class PaymentStatus {
    SUCCESS, PARTIAL, FAILED
}
