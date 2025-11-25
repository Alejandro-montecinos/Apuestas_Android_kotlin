package com.example.apuestas.repository

import com.example.apuestas.model.RegistroUiState
import com.example.apuestas.remote.RetrofitInstance

class RegistroUiStateRepository {
    suspend fun getUsuarios(): List<RegistroUiState> {
        return RetrofitInstance.api.getUsuarios()
    }
}
