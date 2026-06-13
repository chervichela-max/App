package com.carsharing.app.service

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class BalanceService(private val context: Context) {
    
    private val prefs: SharedPreferences = context.getSharedPreferences("balance_prefs", Context.MODE_PRIVATE)
    
    private val _balance = MutableStateFlow(getBalance())
    val balance: StateFlow<Double> = _balance.asStateFlow()
    
    fun getBalance(): Double {
        return prefs.getFloat("balance", 352f).toDouble()
    }
    
    fun addMoney(amount: Double) {
        val newBalance = getBalance() + amount
        saveBalance(newBalance)
    }
    
    fun withdraw(amount: Double): Double {
        val currentBalance = getBalance()
        val newBalance = (currentBalance - amount).coerceAtLeast(0.0)
        saveBalance(newBalance)
        return newBalance
    }
    
    private fun saveBalance(amount: Double) {
        prefs.edit().putFloat("balance", amount.toFloat()).apply()
        _balance.value = amount
    }
}
