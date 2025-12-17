package com.example.apuestas.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.apuestas.local.UsuarioDao
import kotlinx.coroutines.launch

class StartupViewModel(
    private val usuarioDao: UsuarioDao
) : ViewModel() {

    // "login" o "inicio"
    var startDestination by mutableStateOf("login")
        private set

    fun checkLoggedUser(onUserLoaded: (String?) -> Unit) {
        viewModelScope.launch {
            val usuario = usuarioDao.obtenerUsuarioActivo()
            if (usuario != null) {
                startDestination = "inicio"
                onUserLoaded(usuario.nombre)   // para setear nombre en LoginViewModel
            } else {
                startDestination = "login"
                onUserLoaded(null)
            }
        }
    }
}
