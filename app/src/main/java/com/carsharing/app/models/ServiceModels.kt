package com.carsharing.app.models

import java.util.*

data class ServiceWork(
    val id: String,
    val name: String,
    val durationMinutes: Int,
    val category: WorkCategory,
    val price: Int? = null
)

enum class WorkCategory {
    ENGINE, BRAKES, SUSPENSION, FILTERS, ELECTRICAL, TIRES, BODY
}

data class ServiceAppointment(
    val id: String,
    val workIds: List<String>,
    val startTime: Date,
    val endTime: Date,
    val carId: String,
    val driverId: String,
    val status: AppointmentStatus,
    val createdAt: Date,
    val totalDuration: Int
)

enum class AppointmentStatus {
    PENDING, CONFIRMED, COMPLETED, CANCELLED
}

data class TimeSlot(
    val startTime: Date,
    val endTime: Date,
    val isAvailable: Boolean,
    val bookedBy: String? = null
)
