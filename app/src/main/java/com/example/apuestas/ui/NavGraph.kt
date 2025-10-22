package com.example.apuestas.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.apuestas.viewmodel.LoginViewModel
import com.example.apuestas.viewmodel.RegistroViewModel
import androidx.lifecycle.viewmodel.compose.viewModel


@Composable
fun NavGraph(
    navController: NavHostController = rememberNavController(),
    registroViewModel: RegistroViewModel = viewModel(),
    loginViewModel: LoginViewModel = viewModel()



) {
    NavHost(navController = navController, startDestination = "registro") {
        composable("registro") {
            RegistroScreen(
                onRegistroExitoso = { navController.navigate("login") },
                registroViewModel = registroViewModel
            )
        }
        composable("login") {
            LoginScreen(
                onLoginSuccess = { navController.navigate("inicio") },
                loginViewModel = loginViewModel
            )
        }
        composable("inicio") {
            MenuPrincipalScreen(
                onIrARuleta = { navController.navigate("ruleta") }
            )
        }



        composable("ruleta") {
            RuletaScreen(navController = navController)
        }




    }
}
