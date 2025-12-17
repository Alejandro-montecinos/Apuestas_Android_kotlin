package com.example.apuestas.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.apuestas.R
import com.example.apuestas.local.AppDatabase
import com.example.apuestas.local.UsuarioEntity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuPrincipalScreen(
    nombreUsuario: String,
    onIrARuleta: () -> Unit,
    onIrABuscagana: () -> Unit
) {
    val context = LocalContext.current
    val usuarioDao = remember { AppDatabase.getInstance(context).usuarioDao() }

    var usuarioActivo by remember { mutableStateOf<UsuarioEntity?>(null) }

    LaunchedEffect(Unit) {
        usuarioActivo = usuarioDao.obtenerUsuarioActivo()
    }


    Scaffold(
        topBar = {
            SmallTopAppBar(
                title = { Text("Bienvenido, ${usuarioActivo?.nombre?.substringBefore(" ")}") },
                navigationIcon = {
                    IconButton(onClick = {}) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Abrir menú de navegación",
                        )
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {

            Text("Saldo: ${usuarioActivo?.monto}")
            Spacer(Modifier.height(12.dp))

            Text("Juegos disponibles", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(8.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                // Botón Buscagana
                Button(
                    onClick = onIrABuscagana,
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(1f),
                    shape = RoundedCornerShape(16.dp),
                    contentPadding = PaddingValues(12.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.buscagana2),
                            contentDescription = "Buscagana",
                            modifier = Modifier
                                .size(128.dp)
                                .clip(RoundedCornerShape(12.dp))
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Buscagana", style = MaterialTheme.typography.titleMedium)
                    }
                }

                // Botón Ruleta
                Button(
                    onClick = onIrARuleta,
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(1f),
                    shape = RoundedCornerShape(16.dp),
                    contentPadding = PaddingValues(12.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ruletabyn),
                            contentDescription = "Ruleta",
                            modifier = Modifier
                                .size(128.dp)
                                .clip(RoundedCornerShape(12.dp))
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Ruleta", style = MaterialTheme.typography.titleMedium)
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewMenuPrincipal() {
    MenuPrincipalScreen(
        nombreUsuario = "Boris",
        onIrARuleta = {},
        onIrABuscagana = {}
    )
}
