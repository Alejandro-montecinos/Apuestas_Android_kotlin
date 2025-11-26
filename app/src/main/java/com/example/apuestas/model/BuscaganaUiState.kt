package com.example.apuestas.model

data class BuscaganaUiState(
    val idUsuario: Int? = null,
    val saldo: Double = 0.0,
    val posicionesMinas: Set<Int> = emptySet(),
    val posicionesDescubiertas: Set<Int> = emptySet(),
    val dineroEncontrado: Int = 0,
    val totalDinero: Int = 6,
    val perdio: Boolean = false,
    val gano: Boolean = false,
    val finalizo: Boolean = false
)
