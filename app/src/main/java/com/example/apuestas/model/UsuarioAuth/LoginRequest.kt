package com.example.apuestas.model.UsuarioAuth

data class LoginRequest(
    val correo: String,
    val contrasena: String
)