package com.example.apuestas.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.apuestas.viewmodel.LoginViewModel
import com.example.apuestas.viewmodel.RegistroViewModel
import com.example.apuestas.local.UsuarioEntity

@Composable
fun NavGraph(
    navController: NavHostController = rememberNavController(),
    registroViewModel: RegistroViewModel,
    loginViewModel: LoginViewModel,
    usuarioActivo: UsuarioEntity?
) {
    val startDestination = if (usuarioActivo != null) "inicio" else "login"

    NavHost(navController = navController, startDestination = startDestination) {

        composable("login") {
            LoginScreen(
                navController = navController,
                onLoginSuccess = { navController.navigate("inicio") },
                loginViewModel = loginViewModel
            )
        }

        composable("registro") {
            RegistroScreen(
                onRegistroExitoso = { navController.navigate("login") },
                registroViewModel = registroViewModel
            )
        }

        composable("inicio") {
            MenuPrincipalScreen(
                onIrARuleta = { navController.navigate("ruleta") },
                onIrABuscagana = { navController.navigate("buscagana") }
            )
        }

        composable("ruleta") {
            RuletaScreen(navController = navController)
        }

        composable("buscagana") {
            BuscaganaScreen(Buscagana = { navController.popBackStack() })
        }
    }
}

