package com.example.apuestas.repository

import com.example.apuestas.model.Ruleta
import kotlin.Int
import kotlin.random.Random

class RuletaRepository {

    var historial = mutableListOf<Ruleta>()

    fun generarNumero(): Int = Random.nextInt(0,37)


    fun generarColor() : Boolean= Random.nextBoolean()





}