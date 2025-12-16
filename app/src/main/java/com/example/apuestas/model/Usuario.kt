package com.example.apuestas.model

data class Usuario(
    val nombre: String = "",
    val correo: String = "",
    val edad: String = "",
    val telefono: String = "",
    val pais: String = "",
    val moneda: String = "",
    val monto: Double
)
