package com.example.apuestas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.apuestas.ui.NavGraph
import com.example.apuestas.ui.theme.ApuestasTheme
import com.example.apuestas.viewmodel.LoginViewModel
import com.example.apuestas.viewmodel.RegistroViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ApuestasTheme {
                val registroViewModel: RegistroViewModel = viewModel()
                val loginViewModel: LoginViewModel = viewModel()
                NavGraph(
                    registroViewModel = registroViewModel,
                    loginViewModel = loginViewModel
                )
            }
        }
    }
}
