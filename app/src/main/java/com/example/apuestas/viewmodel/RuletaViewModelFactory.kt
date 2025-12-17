package com.example.apuestas.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.apuestas.local.UsuarioDao
import com.example.apuestas.remote.ApiService

class RuletaViewModelFactory(
    private val usuarioDao: UsuarioDao,
    private val api: ApiService
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RuletaViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RuletaViewModel(usuarioDao, api) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
