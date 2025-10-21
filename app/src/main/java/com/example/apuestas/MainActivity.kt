package com.example.apuestas


import RegistroScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.apuestas.ui.RuletaScreen
import com.example.apuestas.ui.theme.ApuestasTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ApuestasTheme {
                RuletaScreen()
            }
        }
    }
}
