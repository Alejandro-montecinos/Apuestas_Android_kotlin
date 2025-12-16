package com.example.apuestas.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.apuestas.viewmodel.BuscaganaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BuscaganaScreen(
    Buscagana: () -> Unit = {},
    vm: BuscaganaViewModel = viewModel()
) {
    val estado = vm.uiState

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Buscagana") },
                navigationIcon = { TextButton(onClick = Buscagana) { Text("Volver") } },
                actions = { TextButton(onClick = vm::reiniciarJuego) { Text("Reiniciar") } }
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
            // Estado
            when {
                estado.perdio   -> Text("Perdiste. Reinicia para jugar de nuevo.", color = MaterialTheme.colorScheme.error)
                estado.gano     -> Text("Ganaste. Encontraste las 6 casillas con dinero.", color = MaterialTheme.colorScheme.primary)
                estado.finalizo -> Text("Terminaste con ${estado.dineroEncontrado * 1000} créditos")
                else            -> Text("Toca una casilla: 6 son dinero y 3 son minas")
            }

            Spacer(Modifier.height(16.dp))

            // Grid 3×3
            CuadriculaBuscagana(
                habilitado = !estado.perdio && !estado.gano && !estado.finalizo,
                casillasDescubiertas = estado.posicionesDescubiertas,
                posicionesMinas = estado.posicionesMinas,
                alTocarCasilla = vm::descubrirCasilla
            )

            Spacer(Modifier.height(12.dp))

            // Terminar
            Button(
                onClick = vm::terminar,
                enabled = !estado.perdio && !estado.gano && !estado.finalizo && estado.dineroEncontrado > 0
            ) { Text("Terminar") }

            Spacer(Modifier.height(8.dp))
            Text("Dinero descubierto: ${estado.dineroEncontrado} / ${estado.totalDinero}")
        }
    }
}

@Composable
private fun CuadriculaBuscagana(
    habilitado: Boolean,
    casillasDescubiertas: Set<Int>,
    posicionesMinas: Set<Int>,
    alTocarCasilla: (Int) -> Unit
) {
    val filas = (0 until 9).chunked(3)
    Column(verticalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
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
                    ) { Text(texto, style = MaterialTheme.typography.titleLarge) }
                }
            }
        }
    }
}

@Preview
@Composable
private fun PreviewBuscagana() {
    BuscaganaScreen(Buscagana = {})
}
