package com.carsharing.app.ui.more

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import com.carsharing.app.theme.*

@Composable
fun MoreScreen(
    onLogout: () -> Unit,
    onPaymentClick: () -> Unit,
    onFinesClick: () -> Unit,
    onDebitClick: () -> Unit
) {
    var showLogoutDialog by remember { mutableStateOf(false) }
    
    Scaffold(topBar = {
        TopAppBar(title = { Text("Настройки", fontSize = 20.sp, fontWeight = FontWeight.SemiBold) },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White))
    }) { padding ->
        LazyColumn(Modifier.fillMaxSize().padding(padding).background(Color(0xFFF8F9FC)).padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            item {
                Card(shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                    Row(Modifier.padding(20.dp), horizontalArrangement = Arrangement.spacedBy(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        Box(Modifier.size(60.dp).clip(RoundedCornerShape(20.dp)).background(Color(0xFF0066FF)), Alignment.Center) { Text("👤", fontSize = 32.sp) }
                        Column { Text("Иван Иванов", fontSize = 18.sp, fontWeight = FontWeight.SemiBold); Text("ID: 123456 • Стаж: 3 года", fontSize = 12.sp, color = Color.Gray) }
                    }
                }
            }
            item { Text("💰 Финансы", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Color.Gray, Modifier.padding(top = 8.dp)) }
            item { SettingsItem("💳", "Оплата", "Баланс: 3.52 ₽", onPaymentClick) }
            item { SettingsItem("📋", "Штрафы ГИБДД", "Рассрочка до 300 ₽/день", onFinesClick) }
            item { SettingsItem("💰", "Ежедневные списания", "Управление автоплатежами", onDebitClick) }
            item { Text("⚙️ Управление", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Color.Gray, Modifier.padding(top = 8.dp)) }
            item { SwitchItem("🏦", "Только безналичные заказы", false) }
            item { SettingsItem("🔔", "Уведомления", "История оповещений", {}) }
            item { SettingsItem("⭐", "Оценить работу", "Оценка менеджера", {}) }
            item { SettingsItem("🛡️", "Фикс Защита", "Неактивна", {}, true) }
            item { SettingsItem("📞", "Контакты", "Техподдержка, помощь при ДТП", {}) }
            item { Spacer(Modifier.height(8.dp)); Button({ showLogoutDialog = true }, Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF3B30)), shape = RoundedCornerShape(16.dp)) { Text("🚪 Выйти из аккаунта", fontSize = 16.sp) }; Spacer(Modifier.height(24.dp)) }
        }
    }
    
    if (showLogoutDialog) {
        AlertDialog(onDismissRequest = { showLogoutDialog = false }, title = { Text("Выход", fontWeight = FontWeight.Bold) }, text = { Text("Вы уверены?") },
            confirmButton = { TextButton({ showLogoutDialog = false; onLogout() }) { Text("Выйти", color = Color(0xFFFF3B30)) } },
            dismissButton = { TextButton({ showLogoutDialog = false }) { Text("Отмена") } }, shape = RoundedCornerShape(20.dp))
    }
}

@Composable
fun SettingsItem(icon: String, title: String, subtitle: String, onClick: () -> Unit, isWarning: Boolean = false) {
    Card(Modifier.fillMaxWidth().clickable { onClick() }, shape = RoundedCornerShape(12.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
        Row(Modifier.padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(icon, fontSize = 24.sp)
                Column { Text(title, fontSize = 16.sp, fontWeight = FontWeight.Medium, color = if (isWarning) Color(0xFFFF3B30) else Color(0xFF1A1A2E)); Text(subtitle, fontSize = 12.sp, color = Color.Gray) }
            }
            Text("›", fontSize = 24.sp, color = Color.Gray)
        }
    }
}

@Composable
fun SwitchItem(icon: String, title: String, checked: Boolean) {
    var isChecked by remember { mutableStateOf(checked) }
    Card(Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
        Row(Modifier.padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) { Text(icon, 24.sp); Text(title, 16.sp, FontWeight.Medium) }
            Switch(isChecked, { isChecked = it }, colors = SwitchDefaults.colors(checkedTrackColor = Color(0xFF0066FF)))
        }
    }
}
