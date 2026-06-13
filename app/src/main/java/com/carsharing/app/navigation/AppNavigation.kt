package com.carsharing.app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.carsharing.app.ui.auth.PinScreen
import com.carsharing.app.ui.car.CarScreen
import com.carsharing.app.ui.debit.DebitScreen
import com.carsharing.app.ui.fines.FinesScreen
import com.carsharing.app.ui.main.MainScreen
import com.carsharing.app.ui.more.MoreScreen
import com.carsharing.app.ui.payment.PaymentScreen
import com.carsharing.app.ui.service.ServiceScreen

sealed class Screen(val route: String) {
    object Pin : Screen("pin")
    object Main : Screen("main")
    object Car : Screen("car")
    object Service : Screen("service")
    object Payment : Screen("payment")
    object Fines : Screen("fines")
    object Debit : Screen("debit")
    object More : Screen("more")
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    var isAuthenticated by remember { mutableStateOf(false) }
    
    NavHost(
        navController = navController,
        startDestination = if (isAuthenticated) Screen.Main.route else Screen.Pin.route
    ) {
        composable(Screen.Pin.route) {
            PinScreen(
                onSuccess = {
                    isAuthenticated = true
                    navController.navigate(Screen.Main.route) {
                        popUpTo(Screen.Pin.route) { inclusive = true }
                    }
                }
            )
        }
        
        composable(Screen.Main.route) {
            MainScreen(
                onCarClick = { navController.navigate(Screen.Car.route) },
                onServiceClick = { navController.navigate(Screen.Service.route) },
                onMoreClick = { navController.navigate(Screen.More.route) }
            )
        }
        
        composable(Screen.Car.route) {
            CarScreen()
        }
        
        composable(Screen.Service.route) {
            ServiceScreen(onBookingClick = { })
        }
        
        composable(Screen.Payment.route) {
            PaymentScreen(onBack = { navController.popBackStack() })
        }
        
        composable(Screen.Fines.route) {
            FinesScreen(onBack = { navController.popBackStack() })
        }
        
        composable(Screen.Debit.route) {
            DebitScreen(onBack = { navController.popBackStack() })
        }
        
        composable(Screen.More.route) {
            MoreScreen(
                onLogout = {
                    isAuthenticated = false
                    navController.navigate(Screen.Pin.route) {
                        popUpTo(0) { inclusive = true }
                    }
                },
                onPaymentClick = { navController.navigate(Screen.Payment.route) },
                onFinesClick = { navController.navigate(Screen.Fines.route) },
                onDebitClick = { navController.navigate(Screen.Debit.route) }
            )
        }
    }
}
