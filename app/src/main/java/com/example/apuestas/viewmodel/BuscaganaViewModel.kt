package com.example.apuestas.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.apuestas.model.BuscaganaUiState
import kotlin.random.Random

class BuscaganaViewModel : ViewModel() {

    var uiState by mutableStateOf(
        BuscaganaUiState(posicionesMinas = generarMinas())
    )
        private set

    fun reiniciarJuego() {
        uiState = BuscaganaUiState(posicionesMinas = generarMinas())
    }

    fun descubrirCasilla(indice: Int) {
        val s = uiState
        if (s.perdio || s.gano || s.finalizo || indice in s.posicionesDescubiertas) return

        val nuevasDescubiertas = s.posicionesDescubiertas + indice

        uiState = if (indice in s.posicionesMinas) {
            s.copy(posicionesDescubiertas = nuevasDescubiertas, perdio = true)
        } else {
            val ganoAhora = nuevasDescubiertas.count { it !in s.posicionesMinas } >= s.totalDinero
            s.copy(posicionesDescubiertas = nuevasDescubiertas, gano = ganoAhora)
        }
    }

    fun terminar() {
        val s = uiState
        if (!s.perdio && !s.gano && !s.finalizo && s.dineroEncontrado > 0) {
            uiState = s.copy(finalizo = true)
        }
    }

    private fun generarMinas(): Set<Int> {
        val minas = mutableSetOf<Int>()
        while (minas.size < 3) minas += Random.nextInt(0, 9)
        return minas
    }
}
