package com.example.apuestas.repository

import com.example.apuestas.model.RegistroUiState
import com.example.apuestas.model.Ruleta
import com.example.apuestas.remote.RetrofitInstance
import kotlin.Int
import kotlin.random.Random

class RuletaRepository {

    var historial = mutableListOf<Ruleta>()

    fun generarNumero(): Int = Random.nextInt(0,37)


    fun generarColor() : Boolean= Random.nextBoolean()



    suspend fun getMontos(): List<Double> {
        val usuarios = RetrofitInstance.api.getUsuarios()
        return usuarios.map { it.monto }  // Extrae solo el campo monto
    }


    suspend fun getUsuarioPorId(id: Int): RegistroUiState {
        return RetrofitInstance.api.getUsuarioPorId(id)
    }

    suspend fun actualizarUsuario(id: Int, usuario: RegistroUiState) {
        RetrofitInstance.api.actualizarUsuario(id, usuario)
    }


    suspend fun getMontoUsuario(id: Int): Double {
        return RetrofitInstance.api.getMontoUsuario(id)
    }


}