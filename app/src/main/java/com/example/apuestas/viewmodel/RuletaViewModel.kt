package com.example.apuestas.viewmodel

import android.util.Log
import com.example.apuestas.model.Ruleta
import com.example.apuestas.repository.RuletaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.apuestas.local.UsuarioDao
import com.example.apuestas.remote.ApiService
import kotlinx.coroutines.launch


class RuletaViewModel(

    private val usuarioDao: UsuarioDao,
    private val api: ApiService

): ViewModel() {

    val rulRepo = RuletaRepository()
    val rul = Ruleta()


    private val _montoUsuario = MutableStateFlow<Double?>(null)
    val montoUsuario: StateFlow<Double?> get() = _montoUsuario


    fun cargarMontoUsuario(id: Int) {
        viewModelScope.launch {
            try {
                val usuario = rulRepo.getUsuarioPorId(id)
                _montoUsuario.value = usuario.monto
            } catch (e: Exception) {
                _montoUsuario.value = null
                Log.e("RuletaViewModel", "Error consultando monto", e)
            }
        }
    }



    fun procesarApuesta(id: Int, ganaste: Boolean, valorApuesta: Double) {
        viewModelScope.launch {
            val usuario = rulRepo.getUsuarioPorId(id)
            val nuevoMonto = if (ganaste) usuario.monto + valorApuesta else usuario.monto - valorApuesta
            val usuarioActualizado = usuario.copy(monto = nuevoMonto)
            rulRepo.actualizarUsuario(id, usuarioActualizado)
            // Actualiza el estado observable en la UI si lo necesitas
        }
    }




    private val _montos = MutableStateFlow<List<Double>>(emptyList())
    val montos: StateFlow<List<Double>> get() = _montos


    fun cargarMontos() {
        viewModelScope.launch {
            val listaMontos = rulRepo.getMontos()
            _montos.value = listaMontos
        }
    }



    fun apostarNumero(numSdiv : Int): Boolean{
        if (numSdiv == rulRepo.generarNumero()){
            return true
        }else return false
    }

    fun apostarColor(colorAdiv: Boolean): Boolean{

        if(colorAdiv == rulRepo.generarColor()){
            return true
        }else return false
    }


    fun actualizarSaldoDespuesDeJuego(idUsuario: Int, nuevoSaldo: Double) {
        viewModelScope.launch {
            try {
                usuarioDao.actualizarMontoUsuario(idUsuario, nuevoSaldo)
                api.actualizarMontoUsuario(idUsuario, nuevoSaldo)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }







}