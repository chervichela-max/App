package com.carsharing.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.lifecycleScope
import com.carsharing.app.navigation.AppNavigation
import com.carsharing.app.theme.CarSharingTheme
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

val Context.dataStore by preferencesDataStore(name = "settings")

class MainActivity : ComponentActivity() {
    
    companion object {
        val PIN_KEY = intPreferencesKey("user_pin")
        val DEFAULT_PIN = 1234
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        lifecycleScope.launch {
            val prefs = dataStore.data.first()
            if (!prefs.contains(PIN_KEY)) {
                dataStore.edit { settings ->
                    settings[PIN_KEY] = DEFAULT_PIN
                }
            }
        }
        
        setContent {
            CarSharingTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation()
                }
            }
        }
    }
}

suspend fun checkPin(enteredPin: Int): Boolean {
    val prefs = dataStore.data
    var savedPin = -1
    prefs.collect { preferences ->
        savedPin = preferences[MainActivity.PIN_KEY] ?: -1
    }
    return enteredPin == savedPin
}
