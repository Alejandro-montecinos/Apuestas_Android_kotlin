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
import androidx.compose.foundation.background
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.layout.ContentScale
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.apuestas.R





@Composable
fun RuletaScreen(
    navController: NavHostController,
    ruletaview: RuletaViewModel = viewModel()
) {
    var iduser by remember { mutableStateOf("") }
    val montoUsuario by ruletaview.montoUsuario.collectAsState()
    var saldoConsultado by remember { mutableStateOf<Double?>(null) }
    var mostrarApuesta by remember { mutableStateOf(false) }
    var montoApuestaTexto by remember { mutableStateOf("") }
    var esperando by remember { mutableStateOf(false) }
    var resultado by remember { mutableStateOf<String?>(null) }
    var saldoResultante by remember { mutableStateOf<Double?>(null) }
    var mensajeErrorApuesta by remember { mutableStateOf("") }
    var iniciarCuentaRegresiva by remember { mutableStateOf(false) }
    var segundosRestantes by remember { mutableStateOf(4) }

    // ----- Cuenta regresiva y lógica de apuesta -----
    if (iniciarCuentaRegresiva) {
        LaunchedEffect(Unit) {
            esperando = true
            for (i in 4 downTo 1) {
                resultado = "Jugando... $i"
                segundosRestantes = i
                kotlinx.coroutines.delay(1000)
            }
            val montoApostar = montoApuestaTexto.toDoubleOrNull() ?: 0.0
            val gano = (0..1).random() == 1
            if (gano) {
                resultado = "¡Ganaste!"
                saldoResultante = saldoConsultado!! + montoApostar
            } else {
                resultado = "Perdiste."
                saldoResultante = saldoConsultado!! - montoApostar
            }
            esperando = false
            saldoConsultado = saldoResultante
            iniciarCuentaRegresiva = false
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFADB5D9))
            .padding(16.dp),
    ) {
        Text(
            text = "Juego Ruleta",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(vertical = 16.dp)
        )

        OutlinedTextField(
            value = iduser,
            onValueChange = { iduser = it.filter { it.isDigit() } },
            label = { Text("Ingrese el ID del usuario") },
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            onClick = {
                val idInt = iduser.toIntOrNull()
                if (idInt != null) {
                    ruletaview.cargarMontoUsuario(idInt)
                }
            },
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Text("Consultar Saldo")
        }

        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = {
                saldoConsultado = montoUsuario
                resultado = null
            },
            enabled = montoUsuario != null
        ) {
            Text("Guardar Saldo Consultado")
        }

        Text(
            text = "Saldo actual: ${(saldoConsultado?.toString() ?: "Sin saldar")}",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(top = 12.dp)
        )

        Image(
            painter = painterResource(id = R.drawable.ruletabyn),
            contentDescription = "ruletabyn",
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(vertical = 16.dp),
            contentScale = ContentScale.Crop
        )

        Button(
            onClick = {
                if (saldoConsultado != null) {
                    mostrarApuesta = true
                    montoApuestaTexto = ""
                    resultado = null
                    mensajeErrorApuesta = ""
                }
            },
            enabled = saldoConsultado != null && !esperando,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00897B))
        ) {
            Text("Apostar")
        }

        if (mostrarApuesta) {
            AlertDialog(
                onDismissRequest = {
                    if (!esperando) {
                        mostrarApuesta = false
                        resultado = null
                        mensajeErrorApuesta = ""
                    }
                },
                title = { Text("Apuesta rápida") },
                text = {
                    Column {
                        Text("¿Cuánto deseas apostar?")
                        OutlinedTextField(
                            value = montoApuestaTexto,
                            onValueChange = {
                                if (it.all { c -> c.isDigit() }) montoApuestaTexto = it
                            },
                            label = { Text("Monto a apostar") }
                        )
                        if (mensajeErrorApuesta.isNotEmpty())
                            Text(mensajeErrorApuesta, color = Color.Red)
                        if (resultado != null) {
                            Spacer(modifier = Modifier.height(8.dp))
                            if (resultado!!.startsWith("Jugando")) {
                                Text("Jugando... $segundosRestantes", style = MaterialTheme.typography.bodyMedium)
                            } else if (resultado == "¡Ganaste!") {
                                Text(
                                    "¡Ganaste $montoApuestaTexto!\nSaldo nuevo: $saldoResultante",
                                    color = Color(0xFF388E3C), style = MaterialTheme.typography.bodyLarge
                                )
                            } else if (resultado == "Perdiste.") {
                                Text(
                                    "Perdiste $montoApuestaTexto\nSaldo nuevo: $saldoResultante",
                                    color = Color.Red, style = MaterialTheme.typography.bodyLarge
                                )
                            }
                        }
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            val montoApostar = montoApuestaTexto.toDoubleOrNull() ?: 0.0
                            if (montoApostar <= 0.0) {
                                mensajeErrorApuesta = "Debes ingresar un monto válido"
                            } else if (saldoConsultado != null && montoApostar > saldoConsultado!!) {
                                mensajeErrorApuesta = "No tienes saldo suficiente"
                            } else if (!esperando) {
                                mensajeErrorApuesta = ""
                                resultado = null
                                iniciarCuentaRegresiva = true
                            }
                        },
                        enabled = !esperando
                    ) { Text("Jugar") }
                },
                dismissButton = {
                    Button(
                        onClick = {
                            mostrarApuesta = false
                            resultado = null
                            mensajeErrorApuesta = ""
                        },
                        enabled = !esperando
                    ) {
                        Text("Cancelar")
                    }
                }
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                navController.popBackStack("inicio", inclusive = false)
                // Limpia estados si es necesario
                iduser = ""
                saldoConsultado = null
                resultado = null
                mostrarApuesta = false
                mensajeErrorApuesta = ""
            }
        ) { Text("Volver") }
    }
}
