package com.example.apuestas.remote

import com.example.apuestas.model.RegistroUiState
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {
    @GET("/api/apuestas/usuarios")
    suspend fun getUsuarios(): List<RegistroUiState>


    @GET("/api/apuestas/usuarios/{id}")
    suspend fun getUsuarioPorId(@Path("id") id: Int): RegistroUiState

    @PUT("/api/apuestas/usuarios/{id}")
    suspend fun actualizarUsuario(@Path("id") id: Int, @Body usuario: RegistroUiState): RegistroUiState

    @GET("/api/apuestas/usuarios/{id}/monto")
    suspend fun getMontoUsuario(@Path("id") id: Int): Double


}