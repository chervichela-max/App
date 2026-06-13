package com.carsharing.app.ui.service

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.carsharing.app.ui.components.*
import com.carsharing.app.theme.*

@Composable
fun ServiceScreen(onBookingClick: () -> Unit) {
    val nextTO = 125335
    val currentTo = 13568
    val remaining = 1432
    
    Scaffold(topBar = {
        TopAppBar(
            title = { Text("ТО и сервис", fontSize = 20.sp, fontWeight = FontWeight.SemiBold) },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
        )
    }) { padding ->
        LazyColumn(
            Modifier.fillMaxSize().padding(padding).background(PremiumBackground).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                PremiumCard(Modifier.fillMaxWidth()) {
                    Text("Следующее ТО", color = Color.White.copy(alpha = 0.9f), fontSize = 14.sp)
                    Text("$nextTO км", color = Color.White, fontSize = 28.sp, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(12.dp))
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Прогресс", color = Color.White.copy(alpha = 0.8f), fontSize = 12.sp)
                        Text("$currentTo / 15000 км", color = Color.White.copy(alpha = 0.8f), fontSize = 12.sp)
                    }
                    AnimatedProgressBar(currentTo / 15000f, Modifier.fillMaxWidth(), color = Color.White, backgroundColor = Color.White.copy(alpha = 0.3f))
                    Text("Осталось $remaining км", color = Color.White.copy(alpha = 0.7f), fontSize = 12.sp, Modifier.padding(top = 8.dp))
                }
            }
            
            item {
                Button(
                    onClick = onBookingClick,
                    Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = PremiumGreen),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text("📅 Записаться на ТО", fontSize = 16.sp)
                }
            }
            
            item {
                Text("📋 История посещений", fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
            }
            
            item { HistoryCard("20 июня", "10:00", "Ул. Шефская 3И", true) }
            item { HistoryCard("27 мая", "11:00", status = false) }
            item { HistoryCard("25 мая", "11:00", status = false) }
            item { HistoryCard("18 мая", "11:15", status = false) }
            item { HistoryCard("18 мая", "11:00", status = false) }
        }
    }
}

@Composable
fun HistoryCard(date: String, time: String, address: String? = null, status: Boolean) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    Modifier.size(48.dp).clip(RoundedCornerShape(12.dp))
                        .background(if (status) PremiumBlue.copy(alpha = 0.1f) else PremiumGreen.copy(alpha = 0.1f)),
                    Alignment.Center
                ) {
                    Text(if (status) "📅" else "✅", fontSize = 24.sp)
                }
                Column {
                    Text("$date в $time", fontSize = 16.sp, fontWeight = FontWeight.Medium)
                    if (address != null) Text(address, fontSize = 12.sp, color = Color.Gray)
                }
            }
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = if (status) PremiumBlue.copy(alpha = 0.1f) else PremiumGreen.copy(alpha = 0.1f)
            ) {
                Text(
                    if (status) "Подтверждена" else "Завершено",
                    fontSize = 12.sp,
                    color = if (status) PremiumBlue else PremiumGreen,
                    Modifier.padding(8.dp)
                )
            }
        }
    }
}
