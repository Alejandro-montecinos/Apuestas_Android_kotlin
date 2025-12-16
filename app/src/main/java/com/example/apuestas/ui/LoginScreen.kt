package com.example.apuestas.ui

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.apuestas.R
import com.example.apuestas.ui.theme.colorAzul
import com.example.apuestas.viewmodel.LoginViewModel

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    loginViewModel: LoginViewModel,
    navController: NavHostController
) {
    val context = LocalContext.current

    val iconTintColor = Color(0xFF1D1F2A)
    val inputTextColor = Color(0xFF1D1F2A)

    var correo by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }

    val correoValid = correo.isNotBlank() && android.util.Patterns.EMAIL_ADDRESS.matcher(correo).matches()
    val contrasenaValid = contrasena.length >= 6

    val formValid = correoValid && contrasenaValid

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFADB5D9))
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.icono), // Cambia por tu imagen en drawable
            contentDescription = "Icono Login",
            modifier = Modifier.size(100.dp)
        )

        Spacer(Modifier.height(24.dp))

        OutlinedTextField(
            value = correo,
            onValueChange = { correo = it },
            label = { Text("Correo electrónico", color = colorAzul) },
            leadingIcon = { Icon(Icons.Filled.Email, contentDescription = null, tint = iconTintColor) },
            textStyle = TextStyle(color = inputTextColor),
            isError = !correoValid && correo.isNotEmpty(),
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFFFFA726),
                unfocusedBorderColor = Color.Black,
                errorBorderColor = Color.Red,
                cursorColor = Color.White,
                focusedLabelColor = Color.LightGray,
                unfocusedLabelColor = Color.LightGray
            )
        )
        if (!correoValid && correo.isNotEmpty()) Text("Correo inválido", color = Color.Red)

        Spacer(Modifier.height(12.dp))

        OutlinedTextField(
            value = contrasena,
            onValueChange = { contrasena = it },
            label = { Text("Contraseña", color = colorAzul) },
            leadingIcon = { Icon(Icons.Filled.Lock, contentDescription = null, tint = iconTintColor) },
            textStyle = TextStyle(color = inputTextColor),
            isError = !contrasenaValid && contrasena.isNotEmpty(),
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
        if (!contrasenaValid && contrasena.isNotEmpty()) Text("La contraseña debe tener al menos 6 caracteres", color = Color.Red)

        Spacer(Modifier.height(24.dp))

        Button(
            enabled = formValid,
            onClick = {
                loginViewModel.validarUsuario(correo, contrasena) { ok ->
                    if (ok) {
                        Toast.makeText(context, "Login exitoso", Toast.LENGTH_SHORT).show()
                        onLoginSuccess()
                    } else {
                        Toast.makeText(context, "Correo o contraseña incorrectos", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            ,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (formValid) Color(0xFFFFA726) else colorAzul,
                contentColor = if (formValid) Color.Black else colorAzul
            ),
            shape = RoundedCornerShape(8.dp),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp)
        ) {
            Text("Ingresar", style = MaterialTheme.typography.bodyLarge)
        }



        Spacer(Modifier.height(12.dp))

        TextButton(
            onClick = { navController.navigate("registro") },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("¿No tiene un usuario creado?", color = Color.White)
        }



    }
}
