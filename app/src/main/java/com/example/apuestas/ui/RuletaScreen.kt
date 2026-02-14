package com.example.apuestas.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.apuestas.R
import com.example.apuestas.local.AppDatabase
import com.example.apuestas.local.UsuarioEntity
import com.example.apuestas.viewmodel.RuletaViewModel
import kotlinx.coroutines.delay

@Composable
fun RuletaScreen(
    navController: NavHostController,
    ruletaview: RuletaViewModel = viewModel()
) {
    val context = LocalContext.current
    val usuarioDao = remember { AppDatabase.getInstance(context).usuarioDao() }

    var usuarioActivo by remember { mutableStateOf<UsuarioEntity?>(null) }
    var saldoConsultado by remember { mutableStateOf<Double?>(null) }

    var mostrarApuesta by remember { mutableStateOf(false) }
    var mostrarConfirmacion by remember { mutableStateOf(false) }

    var montoApuestaTexto by remember { mutableStateOf("") }
    var numeroTexto by remember { mutableStateOf("") }
    var apostarRojo by remember { mutableStateOf(false) }
    var apostarNegro by remember { mutableStateOf(false) }

    var esperando by remember { mutableStateOf(false) }
    var resultado by remember { mutableStateOf<String?>(null) }
    var saldoResultante by remember { mutableStateOf<Double?>(null) }
    var mensajeErrorApuesta by remember { mutableStateOf("") }

    var iniciarCuentaRegresiva by remember { mutableStateOf(false) }
    var segundosRestantes by remember { mutableStateOf(4) }

    // Cargar usuario activo
    LaunchedEffect(Unit) {
        usuarioActivo = usuarioDao.obtenerUsuarioActivo()
        saldoConsultado = usuarioActivo?.monto
    }

    // Lógica del juego + cuenta regresiva
    if (iniciarCuentaRegresiva) {
        LaunchedEffect(Unit) {
            esperando = true
            for (i in 4 downTo 1) {
                resultado = "Jugando... $i"
                segundosRestantes = i
                delay(1000)
            }

            val montoApostar = montoApuestaTexto.toDoubleOrNull() ?: 0.0
            val numeroElegido = numeroTexto.toIntOrNull()
            val numeroRuleta = (0..37).random()

            val esRojo = numeroRuleta % 2 == 1
            val esNegro = numeroRuleta % 2 == 0

            val colorTexto = when {
                numeroRuleta == 0 -> "verde"
                esRojo -> "rojo"
                else -> "negro"
            }

            val apostoNumero = numeroElegido != null
            val apostoColor = apostarRojo || apostarNegro

            val acertoNumero = apostoNumero && numeroElegido == numeroRuleta
            val acertoColor = apostoColor &&
                    ((apostarRojo && esRojo) || (apostarNegro && esNegro))

            var gano = false
            var descripcion = "Salió $numeroRuleta ($colorTexto) "

            // Reglas:
            // 1) Si apuesta número y color -> ambos deben cumplirse
            // 2) Si apuesta solo número -> solo número
            // 3) Si apuesta solo color -> solo color
            if (apostoNumero && apostoColor) {
                if (acertoNumero && acertoColor) {
                    gano = true
                    descripcion += "(acertaste número y color) "
                }
            } else if (apostoNumero) {
                if (acertoNumero) {
                    gano = true
                    descripcion += "(acertaste el número) "
                }
            } else if (apostoColor) {
                if (acertoColor) {
                    gano = true
                    descripcion += "(acertaste el color) "
                }
            }

            if (gano) {
                resultado = "¡Ganaste! $descripcion"
                saldoResultante = saldoConsultado!! + montoApostar
            } else {
                resultado = "Perdiste. $descripcion"
                saldoResultante = saldoConsultado!! - montoApostar
            }

            esperando = false
            saldoConsultado = saldoResultante
            iniciarCuentaRegresiva = false

            usuarioActivo?.let { usuario ->
                saldoResultante?.let { nuevoSaldo ->
                    ruletaview.actualizarSaldoDespuesDeJuego(usuario.id, nuevoSaldo)
                }
            }
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFADB5D9))
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start
        ) {
            Button(
                onClick = { navController.popBackStack("inicio", inclusive = false) },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00897B))
            ) {
                Text("Volver")
            }
        }

        Spacer(Modifier.height(8.dp))

        Text(
            text = "Juego Ruleta",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(vertical = 16.dp)
        )

        Text(
            text = "Saldo actual: ${saldoConsultado ?: 0.0}",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        Image(
            painter = painterResource(id = R.drawable.ruletabyn),
            contentDescription = "ruletabyn",
            modifier = Modifier
                .size(220.dp)
                .padding(vertical = 16.dp),
            contentScale = ContentScale.Fit
        )

        Button(
            onClick = {
                if (saldoConsultado != null) {
                    mostrarApuesta = true
                    montoApuestaTexto = ""
                    numeroTexto = ""
                    apostarRojo = false
                    apostarNegro = false
                    resultado = null
                    mensajeErrorApuesta = ""
                }
            },
            enabled = saldoConsultado != null && !esperando,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00897B))
        ) {
            Text("Apostar")
        }

        // ----- Diálogo principal de apuesta -----
        if (mostrarApuesta) {
            AlertDialog(
                onDismissRequest = {
                    if (!esperando) {
                        mostrarApuesta = false
                        resultado = null
                        mensajeErrorApuesta = ""
                    }
                },
                title = { Text("Elegir apuesta") },
                text = {
                    Column {
                        Text("Saldo disponible: ${saldoConsultado ?: 0.0}")
                        Spacer(Modifier.height(8.dp))

                        OutlinedTextField(
                            value = montoApuestaTexto,
                            onValueChange = {
                                if (it.all { c -> c.isDigit() }) montoApuestaTexto = it
                            },
                            label = { Text("Monto a apostar") }
                        )

                        Spacer(Modifier.height(8.dp))

                        OutlinedTextField(
                            value = numeroTexto,
                            onValueChange = {
                                if (it.all { c -> c.isDigit() }) {
                                    val n = it.toIntOrNull()
                                    if (n == null || n in 0..37) {
                                        numeroTexto = it
                                    }
                                }
                            },
                            label = { Text("Número (0 - 37, opcional)") }
                        )

                        Spacer(Modifier.height(8.dp))

                        Text("Apostar por color (opcional)")

                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Button(
                                onClick = {
                                    apostarRojo = !apostarRojo
                                    if (apostarRojo) apostarNegro = false
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (apostarRojo) Color.Red else Color.LightGray,
                                    contentColor = Color.White
                                )
                            ) { Text("Rojo") }

                            Button(
                                onClick = {
                                    apostarNegro = !apostarNegro
                                    if (apostarNegro) apostarRojo = false
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (apostarNegro) Color.Black else Color.LightGray,
                                    contentColor = Color.White
                                )
                            ) { Text("Negro") }
                        }

                        if (mensajeErrorApuesta.isNotEmpty()) {
                            Text(mensajeErrorApuesta, color = Color.Red)
                        }

                        if (resultado != null) {
                            Spacer(modifier = Modifier.height(8.dp))
                            if (resultado!!.startsWith("Jugando")) {
                                Text(
                                    "Jugando... $segundosRestantes",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            } else {
                                Text(
                                    resultado!! + "\nSaldo nuevo: $saldoResultante",
                                    color = if (resultado!!.startsWith("¡Ganaste!"))
                                        Color(0xFF388E3C) else Color.Red,
                                    style = MaterialTheme.typography.bodyLarge
                                )
                            }
                        }
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            val montoApostar = montoApuestaTexto.toDoubleOrNull() ?: 0.0
                            val numeroElegido = numeroTexto.toIntOrNull()

                            if (montoApostar <= 0.0) {
                                mensajeErrorApuesta = "Debes ingresar un monto válido"
                            } else if (saldoConsultado != null && montoApostar > saldoConsultado!!) {
                                mensajeErrorApuesta = "No tienes saldo suficiente"
                            } else if (numeroElegido == null && !apostarRojo && !apostarNegro) {
                                mensajeErrorApuesta = "Debes elegir un número, un color o ambos"
                            } else if (!esperando) {
                                mensajeErrorApuesta = ""
                                mostrarApuesta = false
                                mostrarConfirmacion = true
                            }
                        },
                        enabled = !esperando
                    ) {
                        Text("Jugar")
                    }
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

        // ----- Diálogo de confirmación -----
        if (mostrarConfirmacion) {
            val montoApostar = montoApuestaTexto.toDoubleOrNull() ?: 0.0
            val numeroElegido = numeroTexto.toIntOrNull()
            val textoNumero = numeroElegido?.toString() ?: "Sin número"
            val textoColor = when {
                apostarRojo -> "Rojo"
                apostarNegro -> "Negro"
                else -> "Sin color"
            }

            AlertDialog(
                onDismissRequest = { mostrarConfirmacion = false },
                title = { Text("¿Está seguro que desea apostar?") },
                text = {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Monto a apostar:", style = MaterialTheme.typography.bodyMedium)
                            Text("$montoApostar", style = MaterialTheme.typography.bodyMedium)
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Número a apostar:", style = MaterialTheme.typography.bodyMedium)
                            Text(textoNumero, style = MaterialTheme.typography.bodyMedium)
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Color a apostar:", style = MaterialTheme.typography.bodyMedium)
                            Text(textoColor, style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            resultado = null
                            iniciarCuentaRegresiva = true
                            mostrarConfirmacion = false
                            mostrarApuesta = true
                        }
                    ) { Text("Aceptar") }
                },
                dismissButton = {
                    TextButton(onClick = { mostrarConfirmacion = false }) {
                        Text("Cancelar")
                    }
                }
            )
        }
    }
}
