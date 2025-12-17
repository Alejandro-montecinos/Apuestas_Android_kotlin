package com.example.apuestas.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.example.apuestas.local.UsuarioDao
import com.example.apuestas.local.UsuarioEntity
import kotlinx.coroutines.launch

data class RegistroUiState(
    val nombre: String = "",
    val correo: String = "",
    val contrasena: String = "",
    val edad: String = "",
    val telefono: String = "",
    val pais: String = "",
    val moneda: String = ""
)

class RegistroViewModel(
    private val usuarioDao: UsuarioDao
) : ViewModel() {

    var uiState by mutableStateOf(RegistroUiState())
        private set

    fun onNombreChange(value: String) { uiState = uiState.copy(nombre = value) }
    fun onCorreoChange(value: String) { uiState = uiState.copy(correo = value) }
    fun onContrasenaChange(value: String) { uiState = uiState.copy(contrasena = value) }
    fun onEdadChange(value: String) { uiState = uiState.copy(edad = value) }
    fun onTelefonoChange(value: String) { uiState = uiState.copy(telefono = value) }
    fun onPaisChange(value: String) { uiState = uiState.copy(pais = value) }
    fun onMonedaChange(value: String) { uiState = uiState.copy(moneda = value) }

    fun registrarUsuario(
        onSuccess: () -> Unit,
        onError: () -> Unit
    ) {
        viewModelScope.launch {
            try {
                val usuario = UsuarioEntity(
                    nombre = uiState.nombre,
                    correo = uiState.correo,
                    contrasena = uiState.contrasena,
                    edad = uiState.edad,
                    telefono = uiState.telefono,
                    pais = uiState.pais,
                    moneda = uiState.moneda,
                    monto = 5000.0,
                    sesionActiva = true
                )
                usuarioDao.cerrarTodasLasSesiones()
                usuarioDao.insertarUsuario(usuario)
                onSuccess()
            } catch (e: Exception) {
                onError()
            }
        }
    }
}
