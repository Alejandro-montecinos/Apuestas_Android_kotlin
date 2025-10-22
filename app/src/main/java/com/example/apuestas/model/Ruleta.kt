package com.example.apuestas.model

class Ruleta (
    var numRuleta: Int? = 0,
    var colorRul: Boolean? = false,
    var numAdiv : Int? = 0,
    nombre : String = "Ruleta",
    estado : Boolean = false,
    apuestaMinima : Double = 100.0,
    apuestaMaxima : Double = 10000.0,
    cantidadJugadores : Int = 1

) : Juego(nombre,
        estado,
        apuestaMinima,
        apuestaMaxima,
        cantidadJugadores)

