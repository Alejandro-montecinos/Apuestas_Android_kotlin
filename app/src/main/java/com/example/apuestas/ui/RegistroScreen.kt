package com.example.apuestas.ui

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.apuestas.ui.theme.colorAzul
import com.example.apuestas.viewmodel.RegistroViewModel

@Composable
fun RegistroScreen(
    onRegistroExitoso: () -> Unit,
    registroViewModel: RegistroViewModel
) {
    val context = LocalContext.current
    val uiState = registroViewModel.uiState

    val iconTintColor = Color(0xFF1D1F2A)
    val inputTextColor = Color(0xFF1D1F2A)

    val paises = listOf("Chile", "Argentina", "Perú", "México", "España")
    val monedas = mapOf(
        "Chile" to "Peso chileno (CLP)",
        "Argentina" to "Peso argentino (ARS)",
        "Perú" to "Sol peruano (PEN)",
        "México" to "Peso mexicano (MXN)",
        "España" to "Euro (EUR)"
    )

    var expandedPais by remember { mutableStateOf(false) }
    var expandedMoneda by remember { mutableStateOf(false) }

    val nombreValid = uiState.nombre.isNotBlank()
    val correoValid = uiState.correo.isNotBlank() && android.util.Patterns.EMAIL_ADDRESS.matcher(uiState.correo).matches()
    val contrasenaValid = uiState.contrasena.length >= 6
    val edadInt = uiState.edad.toIntOrNull() ?: 0
    val edadValid = edadInt >= 18
    val telefonoValid = uiState.telefono.isNotBlank()
    val paisValid = uiState.pais.isNotBlank()
    val monedaValid = uiState.moneda.isNotBlank()

    val formValid = nombreValid && correoValid && contrasenaValid && edadValid && telefonoValid && paisValid && monedaValid

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFADB5D9))
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Registro", color = colorAzul, fontSize = 24.sp)
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = uiState.nombre,
            onValueChange = { registroViewModel.onNombreChange(it) },
            label = { Text("Nombre completo", color = colorAzul) },
            textStyle = TextStyle(color = inputTextColor),
            leadingIcon = { Icon(Icons.Filled.Person, contentDescription = null, tint = iconTintColor) },
            isError = !nombreValid && uiState.nombre.isNotEmpty(),
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFFFFA726),
                unfocusedBorderColor = Color.Black,
                errorBorderColor = Color.Red,
                cursorColor = Color.White,
                focusedLabelColor = Color.LightGray,
                unfocusedLabelColor = Color.LightGray
            )
        )
        if (!nombreValid && uiState.nombre.isNotEmpty()) Text("El nombre es obligatorio", color = Color.Red)
        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = uiState.correo,
            onValueChange = { registroViewModel.onCorreoChange(it) },
            label = { Text("Correo electrónico", color = colorAzul) },
            leadingIcon = { Icon(Icons.Filled.Email, contentDescription = null, tint = iconTintColor) },
            textStyle = TextStyle(color = inputTextColor),
            isError = !correoValid && uiState.correo.isNotEmpty(),
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFFFFA726),
                unfocusedBorderColor = Color.Black,
                errorBorderColor = Color.Red,
                cursorColor = Color.White,
                focusedLabelColor = Color.LightGray,
                unfocusedLabelColor = Color.LightGray
            )
        )
        if (!correoValid && uiState.correo.isNotEmpty()) Text("Correo inválido", color = Color.Red)
        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = uiState.contrasena,
            onValueChange = { registroViewModel.onContrasenaChange(it) },
            label = { Text("Contraseña", color = colorAzul) },
            leadingIcon = { Icon(Icons.Filled.Lock, contentDescription = null, tint = iconTintColor) },
            textStyle = TextStyle(color = inputTextColor),
            isError = uiState.contrasena.length < 6 && uiState.contrasena.isNotEmpty(),
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFFFFA726),
                unfocusedBorderColor = Color.Black,
                errorBorderColor = Color.Red,
                cursorColor = Color.White,
                focusedLabelColor = Color.LightGray,
                unfocusedLabelColor = Color.LightGray
            )
        )
        if (uiState.contrasena.length < 6 && uiState.contrasena.isNotEmpty()) {
            Text("La contraseña debe tener al menos 6 caracteres", color = Color.Red)
        }
        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = uiState.edad,
            onValueChange = { registroViewModel.onEdadChange(it.filter { c -> c.isDigit() }) },
            label = { Text("Edad", color = colorAzul) },
            leadingIcon = { Icon(Icons.Filled.Person, contentDescription = null, tint = iconTintColor) },
            textStyle = TextStyle(color = inputTextColor),
            isError = !edadValid && uiState.edad.isNotEmpty(),
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFFFFA726),
                unfocusedBorderColor = Color.Black,
                errorBorderColor = Color.Red,
                cursorColor = Color.White,
                focusedLabelColor = Color.LightGray,
                unfocusedLabelColor = Color.LightGray
            )
        )
        if (!edadValid && uiState.edad.isNotEmpty()) Text("Debes tener al menos 18 años", color = Color.Red)
        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = uiState.telefono,
            onValueChange = { registroViewModel.onTelefonoChange(it) },
            label = { Text("Teléfono", color = colorAzul) },
            leadingIcon = { Icon(Icons.Filled.Phone, contentDescription = null, tint = iconTintColor) },
            textStyle = TextStyle(color = inputTextColor),
            isError = !telefonoValid && uiState.telefono.isNotEmpty(),
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFFFFA726),
                unfocusedBorderColor = Color.Black,
                errorBorderColor = Color.Red,
                cursorColor = Color.White,
                focusedLabelColor = Color.LightGray,
                unfocusedLabelColor = Color.LightGray
            )
        )
        if (!telefonoValid && uiState.telefono.isNotEmpty()) Text("El teléfono es obligatorio", color = Color.Red)
        Spacer(modifier = Modifier.height(12.dp))

        Box {
            OutlinedButton(
                onClick = { expandedPais = true },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.Transparent),
                border = BorderStroke(1.dp, iconTintColor),
                shape = RoundedCornerShape(4.dp)
            ) {
                Text(if (uiState.pais.isEmpty()) "Selecciona país" else uiState.pais)
            }
            DropdownMenu(expanded = expandedPais, onDismissRequest = { expandedPais = false }) {
                paises.forEach { p ->
                    DropdownMenuItem(
                        text = { Text(p) },
                        onClick = {
                            registroViewModel.onPaisChange(p)
                            registroViewModel.onMonedaChange(monedas[p] ?: "")
                            expandedPais = false
                        }
                    )
                }
            }
        }
        if (!paisValid && uiState.pais.isNotEmpty()) Text("Selecciona un país", color = Color.Red)
        Spacer(modifier = Modifier.height(12.dp))

        Box {
            OutlinedButton(
                onClick = { expandedMoneda = true },
                modifier = Modifier.fillMaxWidth(),
                enabled = uiState.pais.isNotEmpty(),
                colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.Transparent),
                border = BorderStroke(1.dp, iconTintColor),
                shape = RoundedCornerShape(4.dp)
            ) {
                Text(if (uiState.moneda.isEmpty()) "Selecciona moneda" else uiState.moneda)
            }
            DropdownMenu(expanded = expandedMoneda, onDismissRequest = { expandedMoneda = false }) {
                monedas[uiState.pais]?.let { monedaSeleccionada ->
                    DropdownMenuItem(
                        text = { Text(monedaSeleccionada) },
                        onClick = {
                            registroViewModel.onMonedaChange(monedaSeleccionada)
                            expandedMoneda = false
                        }
                    )
                } ?: DropdownMenuItem(
                    text = { Text("No hay moneda para este país") },
                    onClick = { expandedMoneda = false }
                )
            }
        }
        if (!monedaValid && uiState.moneda.isNotEmpty()) Text("Selecciona una moneda", color = Color.Red)
        Spacer(modifier = Modifier.height(24.dp))

        Button(
            enabled = formValid,
            onClick = {
                if (formValid) {
                    registroViewModel.registrarUsuario(
                        onSuccess = {
                            Toast.makeText(context, "Registro exitoso", Toast.LENGTH_SHORT).show()
                            onRegistroExitoso()
                        },
                        onError = {
                            Toast.makeText(context, "Error al registrar", Toast.LENGTH_SHORT).show()
                        }
                    )
                } else {
                    Toast.makeText(
                        context,
                        "Completa todos los campos correctamente",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (formValid) Color(0xFFFFA726) else colorAzul,
                contentColor = if (formValid) Color.Black else colorAzul
            ),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp)
        ) {
            Icon(Icons.Filled.Check, contentDescription = "Registrar", modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text("Registrarse", style = MaterialTheme.typography.bodyLarge)
        }
    }



}
