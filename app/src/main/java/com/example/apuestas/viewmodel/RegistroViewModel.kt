package com.example.apuestas.viewmodel

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

data class RegistroUiState(
    val nombre: String = "",
    val correo: String = "",
    val contrasena: String = "",
    val edad: String = "",
    val telefono: String = "",
    val pais: String = "",
    val moneda: String = ""

)

class RegistroViewModel : ViewModel() {
    var uiState by mutableStateOf(RegistroUiState())
        private set

    fun onNombreChange(value: String) { uiState = uiState.copy(nombre = value) }
    fun onCorreoChange(value: String) { uiState = uiState.copy(correo = value) }
    fun onContrasenaChange(value: String) { uiState = uiState.copy(contrasena = value) }
    fun onEdadChange(value: String) { uiState = uiState.copy(edad = value) }
    fun onTelefonoChange(value: String) { uiState = uiState.copy(telefono = value) }
    fun onPaisChange(value: String) { uiState = uiState.copy(pais = value) }
    fun onMonedaChange(value: String) { uiState = uiState.copy(moneda = value) }

    fun guardarUsuario(context: Context) {
        val prefs = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        prefs.edit().apply {
            putString("correo", uiState.correo)
            putString("contrasena", uiState.contrasena)
            apply()
        }
    }
}
