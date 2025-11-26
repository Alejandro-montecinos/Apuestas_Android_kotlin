package com.example.apuestas.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.apuestas.viewmodel.BuscaganaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BuscaganaScreen(
    Buscagana: () -> Unit = {},
    vm: BuscaganaViewModel = viewModel()
) {
    val estado by vm.uiState.collectAsState()
    var idInput by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Buscagana") },
                navigationIcon = {
                    TextButton(onClick = Buscagana) {
                        Text("Volver")
                    }
                },
                actions = {
                    TextButton(onClick = vm::reiniciarJuego) {
                        Text("Reiniciar")
                    }
                }
            )
        }
    ) { pad ->
        Column(
            modifier = Modifier
                .padding(pad)
                .padding(16.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = idInput,
                onValueChange = { idInput = it },
                label = { Text("ID Usuario") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true
            )

            Spacer(Modifier.height(8.dp))

            Button(onClick = {
                idInput.toIntOrNull()?.let { vm.actualizarIdUsuario(it) }
            }) {
                Text("Cargar saldo")
            }

            Spacer(Modifier.height(16.dp))

            Text("Saldo: ${estado.saldo}")

            when {
                estado.perdio -> Text("Perdiste. Reinicia para jugar de nuevo.", color = MaterialTheme.colorScheme.error)
                estado.gano -> Text("¡Ganaste! Encontraste todas las casillas con dinero.", color = MaterialTheme.colorScheme.primary)
                estado.finalizo -> Text("Terminaste con ${estado.dineroEncontrado * 1000} créditos")
                else -> Text("Toca una casilla: 6 con 💸 y 3 con 💣")
            }

            Spacer(Modifier.height(16.dp))

            CuadriculaBuscagana(
                habilitado = !estado.perdio && !estado.gano && !estado.finalizo,
                casillasDescubiertas = estado.posicionesDescubiertas,
                posicionesMinas = estado.posicionesMinas,
                alTocarCasilla = vm::descubrirCasilla
            )

            Spacer(Modifier.height(12.dp))

            Button(
                onClick = vm::terminar,
                enabled = !estado.perdio && !estado.gano && !estado.finalizo && estado.dineroEncontrado > 0
            ) {
                Text("Terminar")
            }

            Spacer(Modifier.height(8.dp))
            Text("Dinero descubierto: ${estado.dineroEncontrado} / ${estado.totalDinero}")
        }
    }
}

@Composable
fun CuadriculaBuscagana(
    habilitado: Boolean,
    casillasDescubiertas: Set<Int>,
    posicionesMinas: Set<Int>,
    alTocarCasilla: (Int) -> Unit
) {
    val filas = (0 until 9).chunked(3)
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        filas.forEach { fila ->
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
                fila.forEach { indice ->
                    val descubierta = indice in casillasDescubiertas
                    val esMina = indice in posicionesMinas
                    val texto = when {
                        !descubierta -> "?"
                        esMina -> "💣"
                        else -> "💸"
                    }
                    Button(
                        onClick = { alTocarCasilla(indice) },
                        enabled = !descubierta && habilitado,
                        modifier = Modifier.weight(1f).aspectRatio(1f)
                    ) {
                        Text(texto, style = MaterialTheme.typography.titleLarge)
                    }
                }
            }
        }
    }
}
