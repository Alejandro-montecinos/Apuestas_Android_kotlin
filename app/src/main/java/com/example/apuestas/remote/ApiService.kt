package com.example.apuestas.remote

import com.example.apuestas.model.RegistroUiState
import retrofit2.http.GET

interface ApiService {
    @GET("/api/apuestas/usuarios")
    suspend fun getUsuarios(): List<RegistroUiState>
}