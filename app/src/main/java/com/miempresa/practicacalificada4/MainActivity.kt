package com.miempresa.practicacalificada4
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.miempresa.practicacalificada4.ui.theme.PracticaCalificada4Theme

class MainActivity : ComponentActivity() {

    private val auth: FirebaseAuth by lazy { Firebase.auth }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PracticaCalificada4Theme {
                // A surface container using the 'background' color from the theme
                val navController = rememberNavController()

                NavHost(navController = navController, startDestination = "login") {
                    composable("login") {
                        LoginScreen(navController = navController)
                    }
                    composable("second") {
                        SecondScreen(navController = navController, viewModel = viewModel())
                    }

                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Correo electrónico") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )

        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Contraseña") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )

        errorMessage?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(8.dp)
            )
        }

        Button(
            onClick = {
                try {
                    // Validar que ambos campos estén llenos
                    if (email.isNotEmpty() && password.isNotEmpty()) {
                        // Inicia sesión con Firebase Authentication
                        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    // Inicio de sesión exitoso
                                    navController.navigate("second")
                                } else {
                                    // Manejar error de inicio de sesión
                                    errorMessage = "Error al iniciar sesión: ${task.exception?.message}"
                                }
                            }
                    } else {
                        // Mostrar mensaje de error si no se llenan las casillas
                        errorMessage = "Por favor, completa todos los campos."
                    }
                } catch (e: Exception) {
                    // Manejar cualquier otra excepción que pueda ocurrir
                    errorMessage = "Error inesperado: ${e.message}"
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Text("Iniciar sesión")
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SecondScreen(
    navController: NavController,
    viewModel: UsuarioViewModel = viewModel<UsuarioViewModel>()
) {
    // Observar la información del usuario desde el ViewModel
    val usuario by viewModel.usuario.collectAsState()

    var correosElectronicos by remember { mutableStateOf(List(5) { generarCorreoElectronico() }) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top
    ) {
        // Barra de título
        Text(
            text = "Gmail",
            modifier = Modifier.padding(bottom = 16.dp),
            style = MaterialTheme.typography.bodyLarge
        )

        // Lista de correos electrónicos (puedes usar un LazyColumn para una lista más larga)
        LazyColumn {
            items(correosElectronicos) { correo ->
                CorreoElectronicoItem(correo = correo)
            }
        }

        // Botón de cerrar sesión
        Button(
            onClick = {
                // Cerrar sesión y navegar de vuelta a la pantalla de inicio de sesión
                FirebaseAuth.getInstance().signOut()
                navController.navigate("login")
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Text("Cerrar sesión")
        }

        // Botón de redaccióndd
        FloatingActionButton(
            onClick = {
                navController.navigate("nuevaRedaccion")
                // Aquí puedes agregar la lógica para abrir la pantalla de redacción de correo
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Icon(imageVector = Icons.Default.Add, contentDescription = "Redactar")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CorreoElectronicoItem(correo: CorreoElectronico) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        onClick = {
            // Acción al hacer clic en el correo electrónico
            // Puedes abrir una pantalla de detalles del correo o cualquier otra acción
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = correo.asunto,
                style = MaterialTheme.typography.displaySmall,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Text(
                text = correo.contenido,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

data class CorreoElectronico(val asunto: String, val contenido: String)

fun generarCorreoElectronico(): CorreoElectronico {
    return CorreoElectronico(
        asunto = "Asunto de ejemplo",
        contenido = "Contenido del correo electrónico de ejemplo."
    )
}




