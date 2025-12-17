package com.example.apuestas.remote

import com.example.apuestas.model.Usuario
import com.example.apuestas.model.UsuarioAuth.LoginRequest
import com.example.apuestas.model.UsuarioAuth.LoginResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path








interface ApiService {

    @GET("/api/apuestas/usuarios")
    suspend fun getUsuarios(): List<Usuario>

    @GET("/api/apuestas/usuarios/{id}")
    suspend fun getUsuarioPorId(@Path("id") id: Int): Usuario

    @POST("/api/apuestas/usuarios")
    suspend fun crearUsuario(@Body usuario: Usuario): Usuario

    @PUT("/api/apuestas/usuarios/{id}")
    suspend fun actualizarUsuario(
        @Path("id") id: Int,
        @Body usuario: Usuario
    ): Usuario


    @PUT("/api/apuestas/usuarios/{id}/monto")
    suspend fun actualizarMontoUsuario(
        @Path("id") id: Int,
        @Body nuevoMonto: Double
    ): Usuario


    @GET("/api/apuestas/usuarios/{id}/monto")
    suspend fun getMontoUsuario(@Path("id") id: Int): Double

    // NUEVO: endpoint de login
    @POST("/api/apuestas/usuarios/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse
}
