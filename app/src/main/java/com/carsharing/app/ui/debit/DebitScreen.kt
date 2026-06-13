package com.carsharing.app.ui.debit

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.carsharing.app.models.PaymentStatus
import com.carsharing.app.service.BalanceService
import com.carsharing.app.service.DailyDebitService
import com.carsharing.app.ui.components.*
import com.carsharing.app.theme.*
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun DebitScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val balanceService = remember { BalanceService(context) }
    val debitService = remember { DailyDebitService(context, balanceService) }
    
    val balance by balanceService.balance.collectAsState()
    val activeFines by debitService.activeInstallments.collectAsState()
    val payments by debitService.payments.collectAsState()
    val totalRemaining = debitService.getTotalRemainingAmount()
    val dateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale("ru"))
    
    Scaffold(topBar = {
        TopAppBar(title = { Text("Ежедневные списания", fontSize = 20.sp, fontWeight = FontWeight.SemiBold) },
            navigationIcon = { IconButton(onBack) { Text("←", fontSize = 24.sp) } },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White))
    }) { padding ->
        LazyColumn(Modifier.fillMaxSize().padding(padding).background(PremiumBackground).padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            item { PremiumCard(Modifier.fillMaxWidth()) { Text("Текущий баланс", Color.White.copy(alpha = 0.8f), 14.sp); Text(String.format("%.2f ₽", balance), Color.White, 32.sp, FontWeight.Bold) } }
            if (activeFines.isNotEmpty()) {
                item {
                    Card(shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                        Column(Modifier.padding(20.dp)) { Text("📊 Итого к оплате", 16.sp, FontWeight.SemiBold); Text("$totalRemaining ₽", 28.sp, FontWeight.Bold, PremiumOrange); Text("Ежедневно списывается до 300 ₽", 12.sp, Color.Gray) }
                    }
                }
                item { Text("📋 История списаний", 18.sp, FontWeight.SemiBold) }
                items(payments.take(20)) { payment ->
                    Card(Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                        Row(Modifier.fillMaxWidth().padding(12.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                Box(Modifier.size(40.dp).clip(RoundedCornerShape(10.dp)).background(if (payment.status == PaymentStatus.SUCCESS) PremiumGreen.copy(alpha = 0.1f) else PremiumOrange.copy(alpha = 0.1f)), Alignment.Center) { Text(if (payment.status == PaymentStatus.SUCCESS) "✓" else "⚠️", 20.sp) }
                                Column { Text(dateFormat.format(payment.date), 11.sp, Color.Gray); if (payment.amount > 0) Text("Списано ${payment.amount} ₽", 13.sp, FontWeight.Medium) else Text("Недостаточно средств", 11.sp, PremiumOrange) }
                            }
                            Text("${payment.previousRemaining} → ${payment.newRemaining} ₽", 11.sp, Color.Gray)
                        }
                    }
                }
            } else {
                item {
                    Card(shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                        Column(Modifier.fillMaxWidth().padding(48.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("✅", 64.sp); Text("Нет активных списаний", 20.sp, FontWeight.SemiBold); Text("У вас нет штрафов в рассрочке", 14.sp, Color.Gray)
                        }
                    }
                }
            }
        }
    }
}
