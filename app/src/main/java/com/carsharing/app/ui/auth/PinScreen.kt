package com.carsharing.app.ui.auth

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.*
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.carsharing.app.checkPin
import com.carsharing.app.theme.PremiumBlue
import com.carsharing.app.theme.PrimaryGradient
import kotlinx.coroutines.delay

@Composable
fun PinScreen(onSuccess: () -> Unit) {
    var pinCode by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var showError by remember { mutableStateOf(false) }
    var isAnimating by remember { mutableStateOf(false) }
    
    val alphaAnim by animateFloatAsState(
        targetValue = if (!isAnimating) 0f else 1f,
        animationSpec = tween(500),
        label = "alpha"
    )
    
    LaunchedEffect(Unit) {
        isAnimating = true
    }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color.White, PremiumBlue.copy(alpha = 0.05f))
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp)
                .graphicsLayer { alpha = alphaAnim },
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(PrimaryGradient),
                contentAlignment = Alignment.Center
            ) {
                Text("🚗", fontSize = 50.sp)
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            Text("Быстрый вход", fontSize = 32.sp, fontWeight = FontWeight.Bold, color = PremiumBlue)
            Spacer(modifier = Modifier.height(48.dp))
            
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                repeat(4) { index ->
                    Box(
                        modifier = Modifier
                            .size(20.dp)
                            .clip(CircleShape)
                            .background(if (index < pinCode.length) PremiumBlue else Color.LightGray.copy(alpha = 0.5f))
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(48.dp))
            
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                PinRow(listOf("1", "2", "3")) { pinCode += it; checkSubmit(pinCode, onSuccess) { showErrorMsg(it) { pinCode = ""; errorMessage = it; showError = true; delay(2000); showError = false } } }
                PinRow(listOf("4", "5", "6")) { pinCode += it; checkSubmit(pinCode, onSuccess) { showErrorMsg(it) { pinCode = ""; errorMessage = it; showError = true; delay(2000); showError = false } } }
                PinRow(listOf("7", "8", "9")) { pinCode += it; checkSubmit(pinCode, onSuccess) { showErrorMsg(it) { pinCode = ""; errorMessage = it; showError = true; delay(2000); showError = false } } }
                PinRow(listOf("0", "⌫"), isLastDelete = true) {
                    if (it == "⌫") { if (pinCode.isNotEmpty()) pinCode = pinCode.dropLast(1) }
                    else { pinCode += it; checkSubmit(pinCode, onSuccess) { showErrorMsg(it) { pinCode = ""; errorMessage = it; showError = true; delay(2000); showError = false } } }
                }
            }
            
            AnimatedVisibility(visible = showError, enter = fadeIn() + slideInVertically(), exit = fadeOut() + slideOutVertically()) {
                Card(modifier = Modifier.padding(top = 24.dp), colors = CardDefaults.cardColors(containerColor = Color(0xFFFF3B30).copy(alpha = 0.1f)), shape = RoundedCornerShape(12.dp)) {
                    Text(errorMessage ?: "Неверный PIN-код", Modifier.padding(12.dp), textAlign = TextAlign.Center, color = Color(0xFFFF3B30), fontSize = 14.sp)
                }
            }
        }
    }
}

@Composable
fun PinRow(buttons: List<String>, isLastDelete: Boolean = false, onDigitClick: (String) -> Unit) {
    Row(horizontalArrangement = Arrangement.spacedBy(20.dp)) {
        buttons.forEach { btn ->
            PinButton(btn, isDelete = isLastDelete && btn == "⌫") { onDigitClick(it) }
        }
    }
}

@Composable
fun PinButton(text: String, isDelete: Boolean, onClick: (String) -> Unit) {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(targetValue = if (isPressed) 0.9f else 1f, animationSpec = spring(Spring.DampingRatioMediumBouncy), label = "scale")
    
    Box(
        modifier = Modifier.size(75.dp).scale(scale).shadow(if (isPressed) 4.dp else 8.dp, CircleShape).clip(CircleShape)
            .background(brush = if (isDelete) Brush.horizontalGradient(listOf(Color(0xFFFF3B30), Color(0xFFFF5252))) else Brush.horizontalGradient(listOf(Color.White, Color(0xFFF5F5F5))))
            .clickable(indication = null, interactionSource = remember { MutableInteractionSource() }) { isPressed = true; onClick(text); isPressed = false },
        contentAlignment = Alignment.Center
    ) {
        Text(text, fontSize = 28.sp, fontWeight = FontWeight.Medium, color = if (isDelete) Color.White else Color.Black)
    }
}

private suspend fun checkSubmit(pinCode: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
    if (pinCode.length == 4) { if (checkPin(pinCode.toInt())) onSuccess() else onError("Неверный PIN-код") }
}

private suspend fun showErrorMsg(msg: String, block: suspend () -> Unit) { block() }app/src/main/java/com/carsharing/app/ui/auth/PinScreen.kt
