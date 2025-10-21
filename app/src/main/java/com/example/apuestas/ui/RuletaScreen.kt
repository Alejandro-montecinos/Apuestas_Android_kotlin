package com.example.apuestas.ui


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

import androidx.compose.ui.unit.dp
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import com.example.apuestas.viewmodel.RuletaViewModel

import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.layout.ContentScale
import com.example.apuestas.R





@Composable
fun RuletaScreen(){

    var ruletaview = RuletaViewModel()










    var numeroTexto by remember { mutableStateOf("") }
    var mensajePerder2 by remember { mutableStateOf("") }
    var mensajePerder by remember { mutableStateOf("") }
    var mostrarAlerta by remember { mutableStateOf(false) }
    var contadorPerdidas by remember { mutableStateOf(0) }
    var contadorPerdidas2 by remember { mutableStateOf(0) }
    var dineroPerdido by remember { mutableStateOf(0) }
    var dineroPerdido2 by remember { mutableStateOf(0) }
    var colorSeleccionado by remember { mutableStateOf<Boolean?>(null) }


    // Convierte a Int si es posible, o 0
    val numeroIngresado: Int = numeroTexto.toIntOrNull() ?: 0

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {


        Text(
            text = "Juego Ruleta",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(bottom = 16.dp)

        )

        Image(
            painter = painterResource(id = R.drawable.ruletabyn), // tu imagen sin la extensión
            contentDescription = "ruletabyn",
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .padding(bottom = 16.dp),
            contentScale = ContentScale.Crop
        )



        OutlinedTextField(
            value = numeroTexto,
            onValueChange = { nuevoTexto ->
                // Permitir solo dígitos
                if (nuevoTexto.all { it.isDigit() }) {
                    // Limitar a números entre 0 y 37
                    val numero = nuevoTexto.toIntOrNull()
                    if (numero != null && numero <= 37) {
                        numeroTexto = nuevoTexto
                    } else if (nuevoTexto.isEmpty()) {
                        numeroTexto = ""
                    }
                }
            },
            label = { Text("Ingresa un número (0-37)") },
            modifier = Modifier.fillMaxWidth()
            )



        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (ruletaview.apostarNumero(numeroIngresado)){
                    mostrarAlerta = true
                    mensajePerder = ""
                }else{
                    dineroPerdido = dineroPerdido + 500
                    contadorPerdidas++
                    mensajePerder = "¡Perdiste!. Intentos $contadorPerdidas"

                }
            }


        ){
            Text("Jugar")
        }

        Spacer(modifier = Modifier.height(16.dp))


        if (mensajePerder.isNotEmpty()){
            Text(mensajePerder)
            Text("Menos $dineroPerdido")
        }

        if (mostrarAlerta) {
            contadorPerdidas = 0
            dineroPerdido = 0
            AlertDialog(
                onDismissRequest = { mostrarAlerta = false },
                title = { Text("¡Ganaste!") },
                text = { Text("Felicidades, acertaste el número. y ganaste 10.000 USD") },
                confirmButton = {
                    Button(onClick = { mostrarAlerta = false }) {
                        Text("OK")
                    }
                }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))





            Text(
                text = "Apuesta por un color",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            //  Fila con botones de color
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Boton rojo
                Button(
                    onClick = { colorSeleccionado = true },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = androidx.compose.ui.graphics.Color.Red),
                    shape = androidx.compose.foundation.shape.RoundedCornerShape(0.dp)


                ) {
                    Text("Rojo")
                }
                //boton azul
                Button(
                    onClick = { colorSeleccionado = false }, // Negro
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = androidx.compose.ui.graphics.Color.Black),
                    shape = androidx.compose.foundation.shape.RoundedCornerShape(0.dp)
                ) {
                    Text("Negro")
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            //  Botón Jugar
            Button(
                onClick = {
                    if (colorSeleccionado != null) {
                        if (ruletaview.aposarColor(colorSeleccionado!!)) {
                            mostrarAlerta = true
                            mensajePerder2 = ""
                            contadorPerdidas2 = 0
                            dineroPerdido2=0


                        } else {
                            contadorPerdidas2++
                            dineroPerdido2 = dineroPerdido2 + 100
                            mensajePerder2 = "¡Perdiste!. $contadorPerdidas2"

                        }
                    }
                },

            ) {
                Text("Jugar")
            }

            Spacer(modifier = Modifier.height(16.dp))

        if (mensajePerder2.isNotEmpty()){
            Text(mensajePerder2)
            Text("Menos. $dineroPerdido2 USD")
        }



            // 🔔 Alerta de Ganar
        if (mostrarAlerta) {
            AlertDialog(
                onDismissRequest = { mostrarAlerta = false },
                title = { Text("🎉 ¡Ganaste!") },
                text = { Text("Adivinaste el color correcto. Ganaste 300 USD,") },
                confirmButton = {
                    Button(onClick = { mostrarAlerta = false }) {
                        Text("OK")
                    }
                }
            )

        }










    }






}