package com.example.apuestas.ui

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.apuestas.viewmodel.LoginViewModel
import com.example.apuestas.viewmodel.RegistroViewModel

@Composable
fun NavGraph(
    navController: NavHostController = rememberNavController(),
    registroViewModel: RegistroViewModel = viewModel(),
    loginViewModel: LoginViewModel = viewModel(),
    startDestination: String = "login"
) {
    NavHost(navController = navController, startDestination = startDestination) {

        composable("login") {
            LoginScreen(
                navController = navController,
                onLoginSuccess = {
                    navController.navigate("inicio") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                loginViewModel = loginViewModel
            )
        }

        composable("registro") {
            RegistroScreen(
                onRegistroExitoso = {
                    navController.navigate("inicio") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                registroViewModel = registroViewModel
            )
        }

        composable("inicio") {
            MenuPrincipalScreen(
                nombreUsuario = loginViewModel.nombreUsuario,
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
