package com.example.apuestas.model

data class Usuario(
    val id: Int,
    val nombre: String,
    val correo: String,
    val contrasena: String,
    val edad: Int,
    val telefono: String,
    val pais: String,
    val moneda: String,
    val monto: Double
)
