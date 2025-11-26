package com.example.apuestas.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.apuestas.model.BuscaganaUiState
import com.example.apuestas.model.RegistroUiState
import com.example.apuestas.remote.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.random.Random

class BuscaganaViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(BuscaganaUiState())
    val uiState: StateFlow<BuscaganaUiState> = _uiState

    fun actualizarIdUsuario(id: Int) {
        _uiState.value = _uiState.value.copy(idUsuario = id)
        obtenerSaldoDesdeApi(id)
    }

    private fun obtenerSaldoDesdeApi(id: Int) {
        viewModelScope.launch {
            try {
                val usuario = RetrofitInstance.api.getUsuarioPorId(id)
                _uiState.value = _uiState.value.copy(saldo = usuario.monto)
            } catch (e: Exception) {
                println("Error obteniendo usuario: ${e.message}")
            }
        }
    }

    fun reiniciarJuego() {
        _uiState.value = _uiState.value.copy(
            posicionesMinas = generarMinas(),
            posicionesDescubiertas = emptySet(),
            dineroEncontrado = 0,
            perdio = false,
            gano = false,
            finalizo = false
        )
    }

    private fun generarMinas(): Set<Int> {
        val indices = (0..8).toMutableList()
        indices.shuffle()
        return indices.take(3).toSet()
    }

    fun descubrirCasilla(indice: Int) {
        val estado = _uiState.value
        if (estado.perdio || estado.gano || estado.finalizo || estado.posicionesDescubiertas.contains(indice)) return

        val nuevasDescubiertas = estado.posicionesDescubiertas + indice
        val esMina = estado.posicionesMinas.contains(indice)
        val nuevoDinero = if (!esMina) estado.dineroEncontrado + 1 else estado.dineroEncontrado

        val perdio = esMina
        val gano = nuevoDinero == estado.totalDinero

        _uiState.value = estado.copy(
            posicionesDescubiertas = nuevasDescubiertas,
            dineroEncontrado = nuevoDinero,
            perdio = perdio,
            gano = gano
        )

        if (perdio || gano) {
            actualizarSaldoApi(perdio, nuevoDinero)
        }
    }

    fun terminar() {
        val estado = _uiState.value
        if (estado.perdio || estado.gano || estado.finalizo) return
        _uiState.value = estado.copy(finalizo = true)
        actualizarSaldoApi(perdio = false, descubierto = estado.dineroEncontrado)
    }

    private fun actualizarSaldoApi(perdio: Boolean, descubierto: Int) {
        val estado = _uiState.value
        val id = estado.idUsuario ?: return

        viewModelScope.launch {
            try {
                val usuario = RetrofitInstance.api.getUsuarioPorId(id)
                val nuevoSaldo = if (perdio) usuario.monto - 1000 else usuario.monto + descubierto * 1000
                val actualizado = usuario.copy(monto = nuevoSaldo)
                RetrofitInstance.api.actualizarUsuario(id, actualizado)
                _uiState.value = _uiState.value.copy(saldo = nuevoSaldo)
            } catch (e: Exception) {
                println("Error actualizando saldo: ${e.message}")
            }
        }
    }
}
