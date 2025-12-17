package com.example.apuestas.model

data class Usuario(
    val id: Int? = null,
    val nombre: String,
    val correo: String,
    val contrasena: String,
    val edad: String,
    val telefono: String,
    val pais: String,
    val moneda: String,
    val monto: Double = 0.0
)
