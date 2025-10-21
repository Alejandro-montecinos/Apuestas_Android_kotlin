import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.TextStyle
import com.example.apuestas.ui.theme.miColor


@Composable
fun RegistroScreen() {
    val context = LocalContext.current
    val iconTintColor = Color(0xFF300B0B) // blanco suave/gris claro
    val inputTextColor = Color(0xFF300B0B) // mismo color texto

    var nombre by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var edad by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var pais by remember { mutableStateOf("") }
    var moneda by remember { mutableStateOf("") }

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

    val nombreValid = nombre.isNotBlank()
    val correoValid = correo.isNotBlank() && android.util.Patterns.EMAIL_ADDRESS.matcher(correo).matches()
    val edadInt = edad.toIntOrNull() ?: 0
    val edadValid = edadInt >= 18
    val telefonoValid = telefono.isNotBlank()
    val paisValid = pais.isNotBlank()
    val monedaValid = moneda.isNotBlank()

    val formValid = nombreValid && correoValid && edadValid && telefonoValid && paisValid && monedaValid

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF811E1E))
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Registro", color = miColor, fontSize = 24.sp)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Nombre completo", color = miColor) },
            textStyle = TextStyle(color = inputTextColor),
            leadingIcon = { Icon(Icons.Filled.Person, contentDescription = null, tint = iconTintColor) },
            isError = !nombreValid && nombre.isNotEmpty(),
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
        if (!nombreValid && nombre.isNotEmpty()) Text("El nombre es obligatorio", color = Color.Red)

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = correo,
            onValueChange = { correo = it },
            label = { Text("Correo electrónico", color = miColor) },
            leadingIcon = { Icon(Icons.Filled.Email, contentDescription = null , tint = iconTintColor) },
            textStyle = TextStyle(color = inputTextColor),
            isError = !correoValid && correo.isNotEmpty(),
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
        if (!correoValid && correo.isNotEmpty()) Text("Correo inválido", color = Color.Red)

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = edad,
            onValueChange = { edad = it.filter { c -> c.isDigit() } }, // solo dígitos
            label = { Text("Edad", color = miColor) },
            leadingIcon = { Icon(Icons.Filled.Person, contentDescription = null , tint = iconTintColor) },
            textStyle = TextStyle(color = inputTextColor),
            isError = !edadValid && edad.isNotEmpty(),
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
        if (!edadValid && edad.isNotEmpty()) Text("Debes tener al menos 18 años", color = Color.Red)

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = telefono,
            onValueChange = { telefono = it },
            label = { Text("Teléfono", color = miColor) },
            leadingIcon = { Icon(Icons.Filled.Phone, contentDescription = null , tint = iconTintColor) },
            textStyle = TextStyle(color = inputTextColor),
            isError = !telefonoValid && telefono.isNotEmpty(),
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
        if (!telefonoValid && telefono.isNotEmpty()) Text("El teléfono es obligatorio", color = Color.Red)

        Spacer(modifier = Modifier.height(12.dp))

        Box {
            OutlinedButton(
                onClick = { expandedPais = true },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = Color.Transparent // fondo transparente
                ),
                border = BorderStroke(2.dp, Color(0xFF300B0B)), // borde sólido
                shape = RoundedCornerShape(4.dp)
            ) {
                Text(if (pais.isEmpty()) "Selecciona país" else pais, color = Color.White)
            }
            DropdownMenu(expanded = expandedPais, onDismissRequest = { expandedPais = false }) {
                paises.forEach { p ->
                    DropdownMenuItem(
                        text = { Text(p) },
                        onClick = {
                            pais = p
                            moneda = monedas[p] ?: ""
                            expandedPais = false
                        }
                    )
                }
            }
        }
        if (!paisValid && pais.isNotEmpty()) Text("Selecciona un país", color = Color.Red)

        Spacer(modifier = Modifier.height(12.dp))

        Box {
            OutlinedButton(
                onClick = { expandedMoneda = true },
                modifier = Modifier.fillMaxWidth(),
                enabled = pais.isNotEmpty(),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = Color.Transparent // fondo transparente
                ),
                border = BorderStroke(2.dp, Color(0xFF300B0B)),
                shape = RoundedCornerShape(4.dp)
            ) {
                Text(if (moneda.isEmpty()) "Selecciona moneda" else moneda, color = Color.White)
            }
            DropdownMenu(expanded = expandedMoneda, onDismissRequest = { expandedMoneda = false }) {
                monedas[pais]?.let { MonedaSeleccionada ->
                    DropdownMenuItem(
                        text = { Text(MonedaSeleccionada) },
                        onClick = {
                            moneda = MonedaSeleccionada
                            expandedMoneda = false
                        }
                    )
                } ?: run {
                    DropdownMenuItem(
                        text = { Text("No hay moneda para este país") },
                        onClick = { expandedMoneda = false }
                    )
                }
            }
        }

        if (!monedaValid && moneda.isNotEmpty()) Text("Selecciona una moneda", color = Color.Red)

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            enabled = formValid,
            onClick = {
                if (formValid) {
                    Toast.makeText(context, "Registro exitoso", Toast.LENGTH_SHORT).show()
                    // Aquí puedes agregar navegación a login o siguiente pantalla
                } else {
                    Toast.makeText(context, "Por favor, completa todos los campos correctamente", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (formValid) Color(0xFFFFA726) else miColor,
                contentColor = if (formValid) Color.Black else miColor
            ),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Check,
                contentDescription = "Registrar",
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Registrarse", style = MaterialTheme.typography.bodyLarge)
        }
    }
}