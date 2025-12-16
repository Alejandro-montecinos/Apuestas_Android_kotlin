package com.example.apuestas.model

data class BuscaganaUiState(
    val posicionesMinas: Set<Int> = emptySet(),      // índices 0..8
    val posicionesDescubiertas: Set<Int> = emptySet(),
    val perdio: Boolean = false,
    val gano: Boolean = false,
    val finalizo: Boolean = false,                   // usuario tocó "Terminar"
    val totalMinas: Int = 3,                         // 3 minas
    val totalDinero: Int = 6                         // 6 casillas con dinero
) {
    val dineroEncontrado: Int get() = posicionesDescubiertas.count { it !in posicionesMinas }
}
