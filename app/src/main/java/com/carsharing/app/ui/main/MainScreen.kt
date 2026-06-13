package com.carsharing.app.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.carsharing.app.ui.components.*
import com.carsharing.app.theme.*

@Composable
fun MainScreen(
    onCarClick: () -> Unit,
    onServiceClick: () -> Unit,
    onMoreClick: () -> Unit
) {
    Scaffold(
        bottomBar = {
            NavigationBar(containerColor = Color.White, tonalElevation = 8.dp) {
                listOf("Авто" to "🚗", "Сервис" to "🔧", "Еще" to "☰").forEachIndexed { index, (title, icon) ->
                    NavigationBarItem(
                        selected = false,
                        onClick = when (index) {
                            0 -> onCarClick
                            1 -> onServiceClick
                            else -> onMoreClick
                        },
                        icon = { Text(icon, fontSize = 24.sp) },
                        label = { Text(title, fontSize = 12.sp) }
                    )
                }
            }
        }
    ) { padding ->
        LazyColumn(
            Modifier.fillMaxSize().padding(padding).background(PremiumBackground).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Column {
                        Text("Добро пожаловать!", fontSize = 14.sp, color = PremiumTextSecondary)
                        Text("Иван Иванов", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                    }
                    Surface(shape = RoundedCornerShape(12.dp), color = PremiumLightBlue) {
                        Text("⭐ 4.8", Modifier.padding(12.dp), fontSize = 14.sp, fontWeight = FontWeight.Medium, color = PremiumBlue)
                    }
                }
            }
            
            item {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    StatCard("Оплачено смен", "204", "🔄", PremiumBlue, Modifier.weight(1f))
                    StatCard("Осталось смен", "1256", "⏳", PremiumOrange, Modifier.weight(1f))
                }
            }
            
            item {
                PremiumCard(Modifier.fillMaxWidth()) {
                    Text("Текущий баланс", Color.White.copy(alpha = 0.8f), 14.sp)
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        AnimatedCounter(352, prefix = "₽ ")
                        Text("Пополнить →", Color.White, 14.sp)
                    }
                    Text("На сегодня всё оплачено ✓", Color.White.copy(alpha = 0.7f), 12.sp, Modifier.padding(top = 8.dp))
                }
            }
            
            item {
                Text("Быстрые действия", 18.sp, FontWeight.SemiBold)
                Spacer(Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    QuickCard("📱", "Штрихкод", "Для КПП и ТО", PremiumGreen, Modifier.weight(1f))
                    QuickCard("⛽", "Заправки", "Бонусные карты", PremiumPurple, Modifier.weight(1f))
                }
            }
            
            item {
                Card(shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                    Row(Modifier.padding(20.dp), horizontalArrangement = Arrangement.SpaceBetween, Modifier.fillMaxWidth()) {
                        Column {
                            Text("🎁 Бонусная смена", 18.sp, FontWeight.Bold, PremiumPurple)
                            Text("9 из 25 дней", 14.sp, PremiumTextSecondary)
                        }
                        Surface(shape = RoundedCornerShape(12.dp), PremiumPurple.copy(alpha = 0.1f)) {
                            Text("Осталось 16 дней", Modifier.padding(12.dp), 12.sp, color = PremiumPurple)
                        }
                    }
                    Spacer(Modifier.height(12.dp))
                    AnimatedProgressBar(9f / 25f, Modifier.fillMaxWidth(), color = PremiumPurple)
                }
            }
        }
    }
}

@Composable
fun QuickCard(icon: String, title: String, subtitle: String, color: Color, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.height(100.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            Modifier.fillMaxSize().background(color.copy(alpha = 0.05f)).padding(12.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(icon, 28.sp)
            Column {
                Text(title, 14.sp, FontWeight.Medium, color)
                Text(subtitle, 11.sp, PremiumTextSecondary)
            }
        }
    }
}
