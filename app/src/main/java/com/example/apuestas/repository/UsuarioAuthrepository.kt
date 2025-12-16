package com.example.apuestas.repository

import com.example.apuestas.local.UsuarioDao
import com.example.apuestas.local.UsuarioEntity
import com.example.apuestas.remote.ApiService

class UsuarioAuthRepository(
    private val api: ApiService,
    private val usuarioDao: UsuarioDao
) {

    // Intenta login: primero Room, luego API
    suspend fun login(correo: String, contrasena: String): Result<UsuarioEntity> {
        return try {
            // 1) Local
            val local = usuarioDao.obtenerUsuarioPorCorreo(correo)
            if (local != null && local.contrasena == contrasena) {
                usuarioDao.cerrarTodasLasSesiones()
                usuarioDao.insertarUsuario(local.copy(sesionActiva = true))
                return Result.success(local)
            }

            // 2) Remoto (EJEMPLO: ajustar a tu ApiService real)
            // Si tu API solo tiene getUsuarioPorId(id: Int), aquí deberías tener ese id
            val remoto = api.getUsuarioPorId(correo.toInt())  // ajusta según tu caso

            val entity = UsuarioEntity(
                nombre = remoto.nombre,
                correo = remoto.correo,
                contrasena = contrasena,
                edad = remoto.edad,
                telefono = remoto.telefono,
                pais = remoto.pais,
                moneda = remoto.moneda,
                monto = remoto.monto,
                sesionActiva = true
            )

            usuarioDao.cerrarTodasLasSesiones()
            usuarioDao.insertarUsuario(entity)
            Result.success(entity)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getLoggedUser(): UsuarioEntity? =
        usuarioDao.obtenerUsuarioActivo()

    suspend fun logout() {
        usuarioDao.cerrarTodasLasSesiones()
    }
}
