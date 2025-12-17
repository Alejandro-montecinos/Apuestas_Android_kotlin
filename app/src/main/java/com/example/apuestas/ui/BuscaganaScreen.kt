package com.example.apuestas.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.apuestas.local.AppDatabase
import com.example.apuestas.local.UsuarioEntity
import com.example.apuestas.viewmodel.BuscaganaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BuscaganaScreen(
    Buscagana: () -> Unit = {},
    vm: BuscaganaViewModel = viewModel()
) {
    val estado = vm.uiState

    val context = LocalContext.current
    val usuarioDao = remember { AppDatabase.getInstance(context).usuarioDao() }

    var usuarioActivo by remember { mutableStateOf<UsuarioEntity?>(null) }
    var saldo by remember { mutableStateOf(0.0) }


    LaunchedEffect(Unit) {
        usuarioActivo = usuarioDao.obtenerUsuarioActivo()
        saldo = usuarioActivo?.monto ?: 0.0
    }

    val puedeJugar = saldo >= 1000.0

    suspend fun guardarSaldo(nuevoSaldo: Double) {
        val u = usuarioDao.obtenerUsuarioActivo() ?: return
        usuarioDao.insertarUsuario(u.copy(monto = nuevoSaldo))
        usuarioActivo = u.copy(monto = nuevoSaldo)
        saldo = nuevoSaldo
    }

    LaunchedEffect(estado.perdio) {
        if (estado.perdio && puedeJugar) {
            val nuevoSaldo = (saldo - 1000.0).coerceAtLeast(0.0)
            guardarSaldo(nuevoSaldo)
        }
    }

    LaunchedEffect(estado.finalizo) {
        if (estado.finalizo) {
            val ganancia = estado.dineroEncontrado * 1000.0
            val nuevoSaldo = saldo + ganancia
            guardarSaldo(nuevoSaldo)
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Buscagana") },
                navigationIcon = {
                    TextButton(onClick = Buscagana) { Text("Volver") }
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

            Text("Usuario: ${usuarioActivo?.nombre?.substringBefore(" ") ?: "Usuario"}")
            Text("Saldo: $saldo")
            Text("Apuesta mínima: 1000")

            if (!puedeJugar) {
                Spacer(Modifier.height(8.dp))
                Text(
                    "Saldo insuficiente para jugar",
                    color = MaterialTheme.colorScheme.error
                )
            }

            Spacer(Modifier.height(16.dp))

            when {
                estado.perdio ->
                    Text(
                        "Perdiste. Reinicia para volver a jugar.",
                        color = MaterialTheme.colorScheme.error
                    )
                estado.gano ->
                    Text(
                        "Ganaste. Encontraste todo el dinero.",
                        color = MaterialTheme.colorScheme.primary
                    )
                estado.finalizo ->
                    Text("Terminaste con ${estado.dineroEncontrado * 1000} créditos")
                else ->
                    Text("Toca una casilla")
            }

            Spacer(Modifier.height(16.dp))

            CuadriculaBuscagana(
                habilitado = puedeJugar &&
                        !estado.perdio &&
                        !estado.gano &&
                        !estado.finalizo,
                casillasDescubiertas = estado.posicionesDescubiertas,
                posicionesMinas = estado.posicionesMinas,
                alTocarCasilla = vm::descubrirCasilla
            )

            Spacer(Modifier.height(12.dp))

            Button(
                onClick = vm::terminar,
                enabled = puedeJugar &&
                        !estado.perdio &&
                        !estado.gano &&
                        !estado.finalizo &&
                        estado.dineroEncontrado > 0
            ) {
                Text("Terminar")
            }

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
                fila.forEach { index ->
                    val descubierta = index in casillasDescubiertas
                    val esMina = index in posicionesMinas
                    val texto = when {
                        !descubierta -> "?"
                        esMina -> "💣"
                        else -> "💸"
                    }
                    Button(
                        onClick = { alTocarCasilla(index) },
                        enabled = habilitado && !descubierta,
                        modifier = Modifier.weight(1f).aspectRatio(1f)
                    ) {
                        Text(texto, style = MaterialTheme.typography.titleLarge)
                    }
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
