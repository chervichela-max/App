package com.carsharing.app.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

val PremiumBlue = Color(0xFF0066FF)
val PremiumLightBlue = Color(0xFFE8F0FE)
val PremiumGreen = Color(0xFF00C853)
val PremiumOrange = Color(0xFFFF6B35)
val PremiumRed = Color(0xFFFF3B30)
val PremiumPurple = Color(0xFF5856D6)

val PremiumBackground = Color(0xFFF8F9FC)
val PremiumTextPrimary = Color(0xFF1A1A2E)
val PremiumTextSecondary = Color(0xFF666680)

val PrimaryGradient = Brush.linearGradient(
    colors = listOf(PremiumBlue, PremiumPurple)
)

val SuccessGradient = Brush.linearGradient(
    colors = listOf(PremiumGreen, Color(0xFF00E676))
)

val DangerGradient = Brush.linearGradient(
    colors = listOf(PremiumRed, Color(0xFFFF5252))
)

@Composable
fun CarSharingTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = lightColorScheme(
        primary = PremiumBlue,
        secondary = PremiumPurple,
        tertiary = PremiumOrange,
        background = PremiumBackground,
        surface = Color.White,
        onPrimary = Color.White,
        onBackground = PremiumTextPrimary
    )
    
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography(
            headlineLarge = MaterialTheme.typography.headlineLarge.copy(
                fontWeight = FontWeight.Bold
            ),
            bodyLarge = MaterialTheme.typography.bodyLarge.copy(
                lineHeight = 22.sp
            )
        ),
        shapes = Shapes(
            medium = RoundedCornerShape(12.dp),
            large = RoundedCornerShape(16.dp)
        ),
        content = content
    )
}
