package com.carsharing.app.service

import android.content.Context
import com.carsharing.app.data.ServiceWorksData
import com.carsharing.app.models.AppointmentStatus
import com.carsharing.app.models.ServiceAppointment
import com.carsharing.app.models.TimeSlot
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.*

class ServiceBookingManager(private val context: Context) {
    
    private val _appointments = MutableStateFlow<List<ServiceAppointment>>(emptyList())
    val appointments: StateFlow<List<ServiceAppointment>> = _appointments.asStateFlow()
    
    fun isTimeSlotAvailable(startTime: Date, durationMinutes: Int): Boolean {
        val endTime = Date(startTime.time + durationMinutes * 60 * 1000)
        
        for (appointment in _appointments.value) {
            val existingStart = appointment.startTime.time
            val existingEnd = appointment.endTime.time
            val newStart = startTime.time
            val newEnd = endTime.time
            
            if (newStart < existingEnd && newEnd > existingStart) {
                return false
            }
        }
        return true
    }
    
    fun getAvailableSlots(date: Date, durationMinutes: Int): List<TimeSlot> {
        val slots = mutableListOf<TimeSlot>()
        val calendar = Calendar.getInstance().apply { time = date }
        
        calendar.set(Calendar.HOUR_OF_DAY, 9)
        calendar.set(Calendar.MINUTE, 0)
        
        val endTime = Calendar.getInstance().apply {
            time = date
            set(Calendar.HOUR_OF_DAY, 20)
            set(Calendar.MINUTE, 0)
        }.time
        
        var current = calendar.time
        
        while (current.before(endTime)) {
            val slotEnd = Date(current.time + durationMinutes * 60 * 1000)
            if (slotEnd.after(endTime)) break
            
            val isAvailable = isTimeSlotAvailable(current, durationMinutes)
            slots.add(TimeSlot(current, slotEnd, isAvailable))
            
            calendar.add(Calendar.MINUTE, 30)
            current = calendar.time
        }
        
        return slots
    }
    
    fun createAppointment(
        workIds: List<String>,
        startTime: Date,
        carId: String,
        driverId: String,
        onSuccess: (ServiceAppointment) -> Unit
    ) {
        val duration = ServiceWorksData.getTotalDuration(workIds)
        val endTime = Date(startTime.time + duration * 60 * 1000)
        
        val appointment = ServiceAppointment(
            id = UUID.randomUUID().toString(),
            workIds = workIds,
            startTime = startTime,
            endTime = endTime,
            carId = carId,
            driverId = driverId,
            status = AppointmentStatus.CONFIRMED,
            createdAt = Date(),
            totalDuration = duration
        )
        
        _appointments.value = _appointments.value + appointment
        onSuccess(appointment)
    }
}
