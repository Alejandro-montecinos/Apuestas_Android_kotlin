package com.example.apuestas.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallTopAppBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.apuestas.R
import androidx.compose.ui.tooling.preview.Preview
import kotlin.Unit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuPrincipalScreen(
    onIrARuleta: () -> Unit,
    onIrABuscagana: () -> Unit
) {
    Scaffold(
        topBar = {
            SmallTopAppBar(title = { Text("Bienvenido") },
                navigationIcon = {
                    IconButton( onClick = {}) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Abrir menú de navegación",
                        )
                    }
                })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            Text("Saldo: 5000")
            Spacer(Modifier.height(12.dp))
            Text("Juegos disponibles", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(8.dp))

            // ===== GRID con dos columnas =====
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                // Boton juego 1 Buscagana
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

                // Boton juego 2 Ruleta
                Button(
                    onClick = onIrARuleta ,
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
        onIrARuleta = {},
        onIrABuscagana = {}
    )
}