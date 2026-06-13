package com.carsharing.app.ui.fines

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
import com.carsharing.app.models.InstallmentStatus
import com.carsharing.app.service.BalanceService
import com.carsharing.app.service.DailyDebitService
import com.carsharing.app.ui.components.*
import com.carsharing.app.theme.*
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun FinesScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val balanceService = remember { BalanceService(context) }
    val debitService = remember { DailyDebitService(context, balanceService) }
    
    val activeFines by debitService.activeInstallments.collectAsState()
    val balance by balanceService.balance.collectAsState()
    val totalRemaining = debitService.getTotalRemainingAmount()
    val todayCollection = debitService.getTodayCollectionAmount()
    val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale("ru"))
    
    Scaffold(topBar = {
        TopAppBar(title = { Text("Штрафы в рассрочку", fontSize = 20.sp, fontWeight = FontWeight.SemiBold) },
            navigationIcon = { IconButton(onBack) { Text("←", fontSize = 24.sp) } },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White))
    }) { padding ->
        LazyColumn(Modifier.fillMaxSize().padding(padding).background(PremiumBackground).padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            item { PremiumCard(Modifier.fillMaxWidth()) { Text("Текущий баланс", Color.White.copy(alpha = 0.8f), 14.sp); Text(String.format("%.2f ₽", balance), Color.White, 32.sp, FontWeight.Bold) } }
            if (activeFines.isNotEmpty()) {
                item {
                    Card(shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                        Column(Modifier.padding(20.dp)) {
                            Text("📊 Сводка", 16.sp, FontWeight.SemiBold)
                            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Column { Text("Осталось", 12.sp, Color.Gray); Text("$totalRemaining ₽", 22.sp, FontWeight.Bold, PremiumOrange) }
                                Column(horizontalAlignment = Alignment.End) { Text("Сегодня спишется", 12.sp, Color.Gray); Text("$todayCollection ₽", 22.sp, FontWeight.Bold, PremiumBlue) }
                            }
                        }
                    }
                }
                items(activeFines) { fine ->
                    Card(Modifier.fillMaxWidth().clickable { }, shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                        Column(Modifier.padding(16.dp)) {
                            Row(horizontalArrangement = Arrangement.SpaceBetween, Modifier.fillMaxWidth()) {
                                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                    Box(Modifier.size(48.dp).clip(RoundedCornerShape(12.dp)).background(PremiumOrange.copy(alpha = 0.1f)), Alignment.Center) { Text("📜", 24.sp) }
                                    Column { Text(fine.articleCode.take(35), 14.sp, FontWeight.Medium); Text(fine.offensePlace, 11.sp, Color.Gray) }
                                }
                                Surface(shape = RoundedCornerShape(8.dp), color = if (fine.status == InstallmentStatus.ACTIVE) PremiumOrange.copy(alpha = 0.1f) else Color(0xFFFF3B30).copy(alpha = 0.1f)) {
                                    Text(if (fine.status == InstallmentStatus.ACTIVE) "В процессе" else "Просрочен", 11.sp, color = if (fine.status == InstallmentStatus.ACTIVE) PremiumOrange else Color(0xFFFF3B30), Modifier.padding(8.dp))
                                }
                            }
                            Spacer(Modifier.height(12.dp))
                            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("Осталось: ${fine.remainingAmount} ₽", 14.sp, FontWeight.Medium, PremiumBlue)
                                Text("из ${fine.originalAmount} ₽", 12.sp, Color.Gray)
                            }
                            AnimatedProgressBar((fine.originalAmount - fine.remainingAmount).toFloat() / fine.originalAmount, Modifier.fillMaxWidth(), height = 6, color = PremiumBlue)
                            Text("Ежедневно: до ${fine.dailyPayment} ₽", 11.sp, Color.Gray, Modifier.padding(top = 8.dp))
                        }
                    }
                }
            } else {
                item {
                    Card(shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                        Column(Modifier.fillMaxWidth().padding(48.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("✅", 64.sp); Text("Нет активных штрафов", 20.sp, FontWeight.SemiBold); Text("У вас нет штрафов в рассрочке", 14.sp, Color.Gray)
                        }
                    }
                }
            }
        }
    }
}
