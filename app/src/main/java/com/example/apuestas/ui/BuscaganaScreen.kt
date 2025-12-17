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

    // Cargar usuario activo y saldo al entrar
    LaunchedEffect(Unit) {
        usuarioActivo = usuarioDao.obtenerUsuarioActivo()
        saldo = usuarioActivo?.monto ?: 0.0
    }

    // Función local para guardar saldo en Room
    suspend fun guardarSaldo(nuevoSaldo: Double) {
        val u = usuarioDao.obtenerUsuarioActivo() ?: return
        usuarioDao.insertarUsuario(u.copy(monto = nuevoSaldo))
        usuarioActivo = u.copy(monto = nuevoSaldo)
        saldo = nuevoSaldo
    }

    // ✅ Cuando PIERDE: descuento (ejemplo) y guardar
    // Cambia el descuento a lo que tu profe pida.
    LaunchedEffect(estado.perdio) {
        if (estado.perdio) {
            val apuesta = 1000.0 // 👈 ejemplo: pierde 1000
            val nuevoSaldo = (saldo - apuesta).coerceAtLeast(0.0)
            guardarSaldo(nuevoSaldo)
        }
    }

    // ✅ Cuando TERMINA (finalizo=true): suma ganancia y guardar
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

            // ✅ Mostrar nombre + saldo real
            Text("Usuario: ${usuarioActivo?.nombre?.substringBefore(" ") ?: "Usuario"}")
            Text("Saldo: $saldo")
            Spacer(Modifier.height(12.dp))

            // Estado del juego
            when {
                estado.perdio ->
                    Text(
                        "Perdiste. Reinicia para jugar de nuevo.",
                        color = MaterialTheme.colorScheme.error
                    )
                estado.gano ->
                    Text(
                        "Ganaste. Encontraste las 6 casillas con dinero.",
                        color = MaterialTheme.colorScheme.primary
                    )
                estado.finalizo ->
                    Text("Terminaste con ${estado.dineroEncontrado * 1000} créditos")
                else ->
                    Text("Toca una casilla: 6 son dinero y 3 son minas")
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
