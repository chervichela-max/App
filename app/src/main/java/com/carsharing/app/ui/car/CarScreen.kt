package com.carsharing.app.ui.car

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
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
fun CarScreen() {
    var currentMileage by remember { mutableStateOf(123903) }
    var showDialog by remember { mutableStateOf(false) }
    var newMileage by remember { mutableStateOf("") }
    
    val nextTO = 125335
    val currentTo = 13568
    val remaining = 1432
    
    Scaffold(topBar = {
        TopAppBar(title = { Text("Мой автомобиль", fontSize = 20.sp, fontWeight = FontWeight.SemiBold) },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White))
    }) { padding ->
        LazyColumn(Modifier.fillMaxSize().padding(padding).background(PremiumBackground).padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            
            item {
                Box(Modifier.fillMaxWidth().height(180.dp).clip(RoundedCornerShape(24.dp)).background(PremiumBlue.copy(alpha = 0.1f)), Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("🚗", fontSize = 64.sp)
                        Text("Volkswagen BORA", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                        Text("Golf IV • 1.6 MPI", fontSize = 14.sp, color = PremiumTextSecondary)
                    }
                }
            }
            
            item {
                Card(shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                    Row(Modifier.fillMaxWidth().padding(20.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Column {
                            Text("Общий пробег", fontSize = 14.sp, color = PremiumTextSecondary)
                            Row(verticalAlignment = Alignment.Bottom) {
                                AnimatedCounter(targetValue = currentMileage, modifier = Modifier)
                                Text("км", fontSize = 14.sp, color = PremiumTextSecondary, Modifier.padding(bottom = 4.dp))
                            }
                        }
                        OutlinedButton(onClick = { showDialog = true }, shape = RoundedCornerShape(12.dp)) {
                            Icon(Icons.Default.Edit, null, Modifier.size(16.dp))
                            Spacer(Modifier.width(4.dp))
                            Text("Указать", fontSize = 12.sp)
                        }
                    }
                }
            }
            
            item {
                PremiumCard(Modifier.fillMaxWidth()) {
                    Text("🔧 Следующее ТО", color = Color.White.copy(alpha = 0.9f), fontSize = 14.sp)
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
        }
    }
    
    if (showDialog) {
        AlertDialog(onDismissRequest = { showDialog = false }, title = { Text("Указать пробег") },
            text = { Column { Text("Введите текущий пробег"); Spacer(Modifier.height(12.dp)); OutlinedTextField(newMileage, { newMileage = it.filter { c -> c.isDigit() } }, label = { Text("Пробег (км)") }) } },
            confirmButton = { TextButton({ val m = newMileage.toIntOrNull(); if (m != null && m > currentMileage) { currentMileage = m; showDialog = false } }) { Text("Сохранить", color = PremiumBlue) } },
            dismissButton = { TextButton({ showDialog = false }) { Text("Отмена") } }, shape = RoundedCornerShape(20.dp))
    }
}
