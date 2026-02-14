package com.example.apuestas.ui

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.apuestas.viewmodel.LoginViewModel
import com.example.apuestas.viewmodel.RegistroViewModel
import com.example.apuestas.viewmodel.RuletaViewModel

@Composable
fun NavGraph(
    navController: NavHostController = rememberNavController(),
    registroViewModel: RegistroViewModel = viewModel(),
    loginViewModel: LoginViewModel = viewModel(),
    ruletaViewModel: RuletaViewModel,
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
                onIrABuscagana = { navController.navigate("buscagana") },
                onCerrarSesion = {
                    loginViewModel.cerrarSesion {
                        navController.navigate("login") {
                            popUpTo("inicio") { inclusive = true }
                        }
                    }
                }
            )
        }


        composable("ruleta") {
            RuletaScreen(
                navController = navController,
                ruletaview = ruletaViewModel
            )
        }


        composable("buscagana") {
            BuscaganaScreen(Buscagana = { navController.popBackStack() })
        }
    }
}
