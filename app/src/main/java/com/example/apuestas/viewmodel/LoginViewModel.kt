package com.example.apuestas.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel

class LoginViewModel : ViewModel() {
    fun validarUsuario(context: Context, correo: String, contrasena: String): Boolean {
        val prefs = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val correoGuardado = prefs.getString("correo", "")
        val contrasenaGuardada = prefs.getString("contrasena", "")
        return correo == correoGuardado && contrasena == contrasenaGuardada
    }
}
