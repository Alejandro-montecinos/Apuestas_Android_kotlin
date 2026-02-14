package com.example.apuestas.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "usuarios")
data class UsuarioEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nombre: String,
    val correo: String,
    val contrasena: String,
    val edad: String,
    val telefono: String,
    val pais: String,
    val moneda: String,
    val monto: Double = 5000.0,
    val sesionActiva: Boolean = false
)