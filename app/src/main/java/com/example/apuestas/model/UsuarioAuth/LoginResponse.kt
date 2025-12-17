package com.example.apuestas.model.UsuarioAuth

data class LoginResponse(
    val id: Int,
    val nombre: String,
    val correo: String,
    val edad: String,
    val telefono: String,
    val pais: String,
    val moneda: String,
    val monto: Double
)