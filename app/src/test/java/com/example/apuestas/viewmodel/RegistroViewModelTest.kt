package com.example.apuestas.viewmodel

import com.example.apuestas.local.UsuarioDao
import com.example.apuestas.model.Usuario
import com.example.apuestas.remote.ApiService
import com.example.apuestas.remote.RetrofitInstance
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*

@OptIn(ExperimentalCoroutinesApi::class)
class RegistroViewModelTest : StringSpec({

    val dispatcher = StandardTestDispatcher()

    beforeTest {
        Dispatchers.setMain(dispatcher)
    }

    afterTest {
        Dispatchers.resetMain()
        unmockkAll()
    }

    val usuarioDao = mockk<UsuarioDao>(relaxed = true)
    val viewModel = RegistroViewModel(usuarioDao)

    "onXChange actualiza correctamente el uiState" {
        viewModel.onNombreChange("Ana")
        viewModel.onCorreoChange("ana@test.com")
        viewModel.onContrasenaChange("pass")
        viewModel.onEdadChange("20")
        viewModel.onTelefonoChange("1111")
        viewModel.onPaisChange("Chile")
        viewModel.onMonedaChange("CLP")

        val state = viewModel.uiState
        state.nombre shouldBe "Ana"
        state.correo shouldBe "ana@test.com"
        state.contrasena shouldBe "pass"
        state.edad shouldBe "20"
        state.telefono shouldBe "1111"
        state.pais shouldBe "Chile"
        state.moneda shouldBe "CLP"
    }

    "registro exitoso llama onSuccess y guarda usuario" {
        val apiMock = mockk<ApiService>()
        mockkObject(RetrofitInstance)
        every { RetrofitInstance.api } returns apiMock

        val usuario = Usuario(
            nombre = "Pedro",
            correo = "pedro@test.com",
            contrasena = "abcd",
            edad = "29",
            telefono = "1234",
            pais = "CL",
            moneda = "CLP",
            monto = 5000.0
        )

        coEvery { apiMock.crearUsuario(any()) } returns usuario

        viewModel.onNombreChange(usuario.nombre)
        viewModel.onCorreoChange(usuario.correo)
        viewModel.onContrasenaChange(usuario.contrasena)
        viewModel.onEdadChange(usuario.edad)
        viewModel.onTelefonoChange(usuario.telefono)
        viewModel.onPaisChange(usuario.pais)
        viewModel.onMonedaChange(usuario.moneda)

        var success = false
        viewModel.registrarUsuario(
            onSuccess = { success = true },
            onError = { error("No debería fallar") }
        )

        dispatcher.scheduler.advanceUntilIdle()

        success shouldBe true

        coVerify {
            usuarioDao.insertarUsuario(
                match {
                    it.correo == "pedro@test.com" &&
                            it.sesionActiva
                }
            )
        }
    }
})
