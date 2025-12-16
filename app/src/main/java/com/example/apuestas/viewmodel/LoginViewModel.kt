package com.example.apuestas.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.apuestas.local.UsuarioDao
import com.example.apuestas.local.UsuarioEntity
import com.example.apuestas.model.UsuarioAuth.LoginRequest
import com.example.apuestas.remote.ApiService

import kotlinx.coroutines.launch

class LoginViewModel(
    private val usuarioDao: UsuarioDao,
    private val api: ApiService
) : ViewModel() {

    fun validarUsuario(
        correo: String,
        contrasena: String,
        onResult: (Boolean) -> Unit
    ) {
        viewModelScope.launch {
            // 1) Intentar primero con Room
            val local = usuarioDao.obtenerUsuarioPorCorreo(correo)
            if (local != null && local.contrasena == contrasena) {
                usuarioDao.cerrarTodasLasSesiones()
                usuarioDao.insertarUsuario(local.copy(sesionActiva = true))
                onResult(true)
                return@launch
            }

            // 2) Si no, llamar API de login
            try {
                val response = api.login(LoginRequest(correo, contrasena))

                val entity = UsuarioEntity(
                    id = response.id,
                    nombre = response.nombre,
                    correo = response.correo,
                    contrasena = contrasena,      // misma que enviaste
                    edad = response.edad,
                    telefono = response.telefono,
                    pais = response.pais,
                    moneda = response.moneda,
                    monto = response.monto,
                    sesionActiva = true
                )

                usuarioDao.cerrarTodasLasSesiones()
                usuarioDao.insertarUsuario(entity)
                onResult(true)
            } catch (e: Exception) {
                e.printStackTrace()
                onResult(false)
            }
        }
    }

    fun cerrarSesion(onDone: () -> Unit) {
        viewModelScope.launch {
            usuarioDao.cerrarTodasLasSesiones()
            onDone()
        }
    }
}
