package com.example.apuestas.viewmodel

import com.example.apuestas.model.Ruleta
import com.example.apuestas.repository.RuletaRepository


class RuletaViewModel{

    val rulRepo = RuletaRepository()
    val rul = Ruleta()

    fun apostarNumero(numSdiv : Int): Boolean{
        if (numSdiv == rulRepo.generarNumero()){
            return true
        }else return false
    }

    fun aposarColor(colorAdiv: Boolean): Boolean{

        if(colorAdiv == rulRepo.generarColor()){
            return true
        }else return false
    }




}