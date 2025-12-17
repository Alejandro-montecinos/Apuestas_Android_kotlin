package com.example.apuestas.viewmodel

import com.example.apuestas.local.UsuarioDao
import com.example.apuestas.local.UsuarioEntity
import com.example.apuestas.model.UsuarioAuth.LoginRequest
import com.example.apuestas.model.UsuarioAuth.LoginResponse
import com.example.apuestas.remote.ApiService
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest : StringSpec({

    val dispatcher = StandardTestDispatcher()

    beforeTest {
        Dispatchers.setMain(dispatcher)
    }

    afterTest {
        Dispatchers.resetMain()
        unmockkAll()
    }

    val usuarioDao = mockk<UsuarioDao>(relaxed = true)
    val apiService = mockk<ApiService>()
    val viewModel = LoginViewModel(usuarioDao, apiService)

    "login local exitoso actualiza nombre y sesión" {
        val user = UsuarioEntity(
            id = 1,
            nombre = "Juan",
            correo = "juan@test.com",
            contrasena = "1234",
            edad = "22",
            telefono = "999",
            pais = "CL",
            moneda = "CLP",
            monto = 5000.0,
            sesionActiva = false
        )

        coEvery { usuarioDao.obtenerUsuarioPorCorreo("juan@test.com") } returns user

        var result = false
        viewModel.validarUsuario("juan@test.com", "1234") { result = it }

        dispatcher.scheduler.advanceUntilIdle()

        result shouldBe true
        viewModel.nombreUsuario shouldBe "Juan"

        coVerify {
            usuarioDao.insertarUsuario(user.copy(sesionActiva = true))
        }
    }

    "login con API exitoso guarda usuario y devuelve true" {
        coEvery { usuarioDao.obtenerUsuarioPorCorreo("api@test.com") } returns null

        val response = LoginResponse(
            id = 2,
            nombre = "API",
            correo = "api@test.com",
            edad = "30",
            telefono = "9999",
            pais = "CL",
            moneda = "CLP",
            monto = 6000.0
        )

        coEvery {
            apiService.login(LoginRequest("api@test.com", "abcd"))
        } returns response

        var result = false
        viewModel.validarUsuario("api@test.com", "abcd") { result = it }

        dispatcher.scheduler.advanceUntilIdle()

        result shouldBe true
        viewModel.nombreUsuario shouldBe "API"

        coVerify {
            usuarioDao.insertarUsuario(
                match {
                    it.id == 2 &&
                            it.correo == "api@test.com" &&
                            it.sesionActiva
                }
            )
        }
    }

    "login fallido devuelve false" {
        coEvery { usuarioDao.obtenerUsuarioPorCorreo(any()) } returns null
        coEvery { apiService.login(any()) } throws Exception("Error API")

        var result = true
        viewModel.validarUsuario("fail@test.com", "fail") { result = it }

        dispatcher.scheduler.advanceUntilIdle()

        result shouldBe false
        viewModel.nombreUsuario shouldBe "API"
    }
})
