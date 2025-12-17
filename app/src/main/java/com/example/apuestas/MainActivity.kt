package com.example.apuestas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.rememberNavController
import com.example.apuestas.local.AppDatabase
import com.example.apuestas.remote.RetrofitInstance
import com.example.apuestas.ui.NavGraph
import com.example.apuestas.ui.theme.ApuestasTheme
import com.example.apuestas.viewmodel.LoginViewModel
import com.example.apuestas.viewmodel.RegistroViewModel
import androidx.compose.runtime.collectAsState
import com.example.apuestas.local.UsuarioEntity

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ApuestasTheme {
                val context = LocalContext.current
                val navController = rememberNavController()

                val db = remember { AppDatabase.getInstance(context) }
                val usuarioDao = remember { db.usuarioDao() }

                // 🔹 observar usuario activo de Room
                val usuarioActivoState =
                    usuarioDao.observarUsuarioActivo().collectAsState(initial = null)
                val usuarioActivo: UsuarioEntity? = usuarioActivoState.value

                val registroViewModel = remember { RegistroViewModel(usuarioDao) }
                val loginViewModel = remember { LoginViewModel(usuarioDao, RetrofitInstance.api) }

                NavGraph(
                    navController = navController,
                    registroViewModel = registroViewModel,
                    loginViewModel = loginViewModel,
                    usuarioActivo = usuarioActivo
                )
            }
        }
    }
}


