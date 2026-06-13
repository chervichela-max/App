package com.carsharing.app.service

import android.content.Context
import android.content.SharedPreferences
import com.carsharing.app.models.InstallmentFine
import com.carsharing.app.models.InstallmentPayment
import com.carsharing.app.models.InstallmentStatus
import com.carsharing.app.models.PaymentStatus
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.SimpleDateFormat
import java.util.*

class DailyDebitService(
    private val context: Context,
    private val balanceService: BalanceService
) {
    
    private val _activeInstallments = MutableStateFlow<List<InstallmentFine>>(emptyList())
    val activeInstallments: StateFlow<List<InstallmentFine>> = _activeInstallments.asStateFlow()
    
    private val _payments = MutableStateFlow<List<InstallmentPayment>>(emptyList())
    val payments: StateFlow<List<InstallmentPayment>> = _payments.asStateFlow()
    
    private val prefs: SharedPreferences = context.getSharedPreferences("installment_prefs", Context.MODE_PRIVATE)
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    
    init {
        loadMockInstallments()
        startDailyCollection()
    }
    
    private fun loadMockInstallments() {
        val calendar = Calendar.getInstance()
        
        val fine1 = InstallmentFine(
            fineId = "fine_1",
            uin = "18810151210318057802",
            articleCode = "КоАП 12.16 ч.1, игнорирование знаков",
            originalAmount = 576,
            remainingAmount = 576,
            dailyPayment = 300,
            startDate = Date(),
            endDate = getDateOffset(calendar, 2),
            status = InstallmentStatus.ACTIVE,
            offensePlace = "МО, г. Егорьевск, ул. Антипова",
            offenseDate = getDateOffset(calendar, -5)
        )
        
        val fine2 = InstallmentFine(
            fineId = "fine_2",
            uin = "18810150211106026865",
            articleCode = "КоАП 12.12 ч.2, пересечение стоп-линии",
            originalAmount = 1250,
            remainingAmount = 1250,
            dailyPayment = 300,
            startDate = Date(),
            endDate = getDateOffset(calendar, 5),
            status = InstallmentStatus.ACTIVE,
            offensePlace = "МО, г. Егорьевск, пересечение Рязанской и Антипова",
            offenseDate = getDateOffset(calendar, -10)
        )
        
        _activeInstallments.value = listOf(fine1, fine2)
    }
    
    private fun getDateOffset(calendar: Calendar, days: Int): Date {
        calendar.add(Calendar.DAY_OF_YEAR, days)
        return calendar.time
    }
    
    fun startDailyCollection() {
        val lastCollectionDate = prefs.getString("last_collection_date", "")
        val today = dateFormat.format(Date())
        
        if (lastCollectionDate != today) {
            processDailyCollections()
        }
        
        CoroutineScope(Dispatchers.IO).launch {
            while (true) {
                delay(24 * 60 * 60 * 1000L)
                processDailyCollections()
            }
        }
    }
    
    private fun processDailyCollections() {
        CoroutineScope(Dispatchers.Main).launch {
            var remainingBalance = balanceService.getBalance()
            val today = Date()
            val dailyLimit = 300
            
            val updatedInstallments = _activeInstallments.value.toMutableList()
            val paymentsToday = mutableListOf<InstallmentPayment>()
            
            for (i in updatedInstallments.indices) {
                val installment = updatedInstallments[i]
                if (installment.status != InstallmentStatus.ACTIVE) continue
                
                val amountToCollect = minOf(dailyLimit, installment.remainingAmount)
                val actualAmount = minOf(amountToCollect, remainingBalance.toInt())
                
                if (actualAmount > 0) {
                    val newRemaining = installment.remainingAmount - actualAmount
                    remainingBalance = balanceService.withdraw(actualAmount.toDouble())
                    
                    val payment = InstallmentPayment(
                        id = UUID.randomUUID().toString(),
                        fineId = installment.fineId,
                        date = today,
                        amount = actualAmount,
                        previousRemaining = installment.remainingAmount,
                        newRemaining = newRemaining,
                        status = if (actualAmount == amountToCollect) PaymentStatus.SUCCESS else PaymentStatus.PARTIAL
                    )
                    paymentsToday.add(payment)
                    
                    val newStatus = if (newRemaining <= 0) InstallmentStatus.COMPLETED else InstallmentStatus.ACTIVE
                    updatedInstallments[i] = installment.copy(
                        remainingAmount = newRemaining,
                        status = newStatus
                    )
                }
            }
            
            _activeInstallments.value = updatedInstallments.filter { it.status != InstallmentStatus.COMPLETED }
            _payments.value = paymentsToday + _payments.value
            
            prefs.edit().putString("last_collection_date", dateFormat.format(today)).apply()
        }
    }
    
    fun getTotalRemainingAmount(): Int = _activeInstallments.value.sumOf { it.remainingAmount }
    
    fun getTodayCollectionAmount(): Int {
        var remaining = 300
        var total = 0
        for (fine in _activeInstallments.value) {
            if (fine.status != InstallmentStatus.ACTIVE) continue
            val toCollect = minOf(remaining, fine.remainingAmount)
            total += toCollect
            remaining -= toCollect
            if (remaining <= 0) break
        }
        return total
    }
    
    fun getEstimatedCompletionDate(): Date? {
        val total = getTotalRemainingAmount()
        if (total <= 0) return null
        val days = (total + 299) / 300
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, days)
        return calendar.time
    }
    
    fun getPaymentsForFine(fineId: String): List<InstallmentPayment> {
        return _payments.value.filter { it.fineId == fineId }
    }
}
