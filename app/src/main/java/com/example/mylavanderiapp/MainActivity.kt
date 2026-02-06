package com.example.mylavanderiapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mylavanderiapp.core.di.AppContainer
import com.example.mylavanderiapp.core.ui.theme.MylavanderiappTheme
import com.example.mylavanderiapp.features.auth.domain.entities.User
import com.example.mylavanderiapp.features.auth.domain.entities.UserRole
import com.example.mylavanderiapp.features.auth.presentation.screens.LoginScreen
import com.example.mylavanderiapp.features.auth.presentation.screens.RegisterScreen
import com.example.mylavanderiapp.features.auth.presentation.viewmodels.LoginViewModel
import com.example.mylavanderiapp.features.auth.presentation.viewmodels.LoginViewModelFactory
import com.example.mylavanderiapp.features.auth.presentation.viewmodels.RegisterViewModel
import com.example.mylavanderiapp.features.auth.presentation.viewmodels.RegisterViewModelFactory
import com.example.mylavanderiapp.features.machines.presentation.screens.HomeScreen

class MainActivity : ComponentActivity() {

    private lateinit var appContainer: AppContainer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializar el contenedor de dependencias
        appContainer = AppContainer()

        enableEdgeToEdge()
        setContent {
            MylavanderiappTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    LaundryApp(appContainer = appContainer)
                }
            }
        }
    }
}

@Composable
fun LaundryApp(appContainer: AppContainer) {
    // Estado para controlar qué pantalla mostrar
    var currentScreen by remember { mutableStateOf("login") }
    var currentUser by remember { mutableStateOf<User?>(null) }

    // Crear los ViewModels con sus Factories
    val registerViewModel: RegisterViewModel = viewModel(
        factory = RegisterViewModelFactory(appContainer.registerUseCase)
    )

    val loginViewModel: LoginViewModel = viewModel(
        factory = LoginViewModelFactory(appContainer.registerUseCase)
    )

    // Navegación simple entre pantallas
    when (currentScreen) {
        "login" -> {
            LoginScreen(
                viewModel = loginViewModel,
                onNavigateToRegister = {
                    currentScreen = "register"
                },
                onLoginSuccess = {
                    // Simular usuario que hace login
                    // Cambiar role a UserRole.USER para probar como usuario normal
                    currentUser = User(
                        id = "user123",
                        name = "Usuario Demo",
                        email = "demo@test.com",
                        role = UserRole.ADMIN  // ← Cambiar a UserRole.USER para probar como usuario normal
                    )
                    currentScreen = "home"
                }
            )
        }
        "register" -> {
            RegisterScreen(
                viewModel = registerViewModel,
                onNavigateToLogin = {
                    currentScreen = "login"
                },
                onRegisterSuccess = {
                    // Simular usuario registrado (por defecto USER)
                    currentUser = User(
                        id = "user456",
                        name = "Nuevo Usuario",
                        email = "nuevo@test.com",
                        role = UserRole.USER
                    )
                    currentScreen = "home"
                }
            )
        }
        "home" -> {
            HomeScreen(
                user = currentUser,
                onLogout = {
                    currentUser = null
                    currentScreen = "login"
                },
                onReserveTurn = { machine ->
                    // TODO: Navegar a pantalla de reservar turno
                    println("Reservar turno para: ${machine.name}")
                },
                onMyTurns = {
                    // TODO: Navegar a pantalla de Mis Turnos
                    println("Ir a Mis Turnos")
                }
            )
        }
    }
}