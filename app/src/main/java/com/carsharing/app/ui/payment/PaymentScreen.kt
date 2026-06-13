package com.carsharing.app.ui.payment

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.carsharing.app.models.PaymentMethod
import com.carsharing.app.models.PaymentTransaction
import com.carsharing.app.models.TransactionStatus
import com.carsharing.app.models.TransactionType
import com.carsharing.app.service.BalanceService
import com.carsharing.app.ui.components.*
import com.carsharing.app.theme.*
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun PaymentScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val balanceService = remember { BalanceService(context) }
    val balance by balanceService.balance.collectAsState()
    var selectedTab by remember { mutableStateOf(0) }
    
    val cards = remember { mutableStateListOf(
        PaymentMethod("1", "**** 4532", "IVAN IVANOV", "12/26", true)
    )}
    
    val transactions = listOf(
        PaymentTransaction("1", 250.0, Date(), TransactionType.RIDE_PAYMENT, TransactionStatus.SUCCESS, "Поездка"),
        PaymentTransaction("2", 500.0, Date(System.currentTimeMillis() - 86400000), TransactionType.TOP_UP, TransactionStatus.SUCCESS, "Пополнение")
    )
    
    val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale("ru"))
    
    Scaffold(topBar = {
        TopAppBar(
            title = { Text("Оплата", fontSize = 20.sp, fontWeight = FontWeight.SemiBold) },
            navigationIcon = { IconButton(onBack) { Text("←", fontSize = 24.sp) } },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
        )
    }) { padding ->
        Column(
            Modifier.fillMaxSize().padding(padding).background(PremiumBackground)
        ) {
            PremiumCard(Modifier.fillMaxWidth().padding(16.dp)) {
                Text("Баланс", Color.White.copy(alpha = 0.8f), 14.sp)
                Text(String.format("%.2f ₽", balance), Color.White, 32.sp, FontWeight.Bold)
            }
            
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = Color.White
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = { Text("Карты") }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = { Text("История") }
                )
            }
            
            when (selectedTab) {
                0 -> {
                    LazyColumn {
                        items(cards) { card ->
                            Card(
                                Modifier.fillMaxWidth().padding(12.dp),
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(containerColor = Color.White)
                            ) {
                                Row(
                                    Modifier.padding(16.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column {
                                        Text("💳 ${card.cardNumber}", 16.sp)
                                        Text("${card.cardHolder} • ${card.expiryDate}", 12.sp, Color.Gray)
                                    }
                                    if (card.isDefault) {
                                        Text("Основная", 12.sp, PremiumGreen)
                                    }
                                }
                            }
                        }
                    }
                }
                1 -> {
                    LazyColumn {
                        items(transactions) { tx ->
                            Card(
                                Modifier.fillMaxWidth().padding(12.dp),
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(containerColor = Color.White)
                            ) {
                                Row(
                                    Modifier.padding(16.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column {
                                        Text(tx.description, 14.sp, FontWeight.Medium)
                                        Text(dateFormat.format(tx.date), 11.sp, Color.Gray)
                                    }
                                    Text(
                                        "${if (tx.type == TransactionType.TOP_UP) "+" else "-"}${tx.amount} ₽",
                                        16.sp,
                                        FontWeight.Bold,
                                        color = if (tx.type == TransactionType.TOP_UP) PremiumGreen else Color(0xFFFF3B30)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
