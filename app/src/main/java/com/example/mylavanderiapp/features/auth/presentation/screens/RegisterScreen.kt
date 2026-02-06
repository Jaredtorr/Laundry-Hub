package com.example.mylavanderiapp.features.auth.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mylavanderiapp.features.auth.presentation.viewmodels.RegisterViewModel
import com.example.mylavanderiapp.features.auth.presentation.components.CustomTextField
import com.example.mylavanderiapp.features.auth.presentation.components.CustomButton
import com.example.mylavanderiapp.features.auth.presentation.states.RegisterUIState

@Composable
fun RegisterScreen(
    viewModel: RegisterViewModel,
    onNavigateToLogin: () -> Unit, // Sincronizado con NavGraph
    onRegisterSuccess: () -> Unit  // Sincronizado con NavGraph
) {
    val formState by viewModel.formState.collectAsState()
    val uiState by viewModel.uiState.collectAsState()

    // Escuchar el estado de éxito para navegar
    LaunchedEffect(uiState) {
        if (uiState is RegisterUIState.Success) {
            onRegisterSuccess()
            viewModel.resetState()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(32.dp))

        // --- LABELS DE CABECERA ---
        Text(
            text = "Crear Cuenta",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1A7A7A)
        )

        Text(
            text = "Completa tus datos para registrarte en el sistema",
            fontSize = 14.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 8.dp, bottom = 32.dp)
        )

        // --- CAMPO NOMBRE ---
        CustomTextField(
            value = formState.fullName,
            onValueChange = { viewModel.onFullNameChange(it) },
            placeholder = "Nombre completo",
            leadingIcon = Icons.Default.Person,
            enabled = uiState !is RegisterUIState.Loading
        )
        // Label de error específico
        formState.fullNameError?.let { ErrorText(it) }

        Spacer(modifier = Modifier.height(16.dp))

        // --- CAMPO EMAIL ---
        CustomTextField(
            value = formState.email,
            onValueChange = { viewModel.onEmailChange(it) },
            placeholder = "Correo electrónico",
            leadingIcon = Icons.Default.Email,
            keyboardType = KeyboardType.Email,
            enabled = uiState !is RegisterUIState.Loading
        )
        formState.emailError?.let { ErrorText(it) }

        Spacer(modifier = Modifier.height(16.dp))

        // --- CAMPO PASSWORD ---
        CustomTextField(
            value = formState.password,
            onValueChange = { viewModel.onPasswordChange(it) },
            placeholder = "Contraseña",
            leadingIcon = Icons.Default.Lock,
            isPassword = true,
            enabled = uiState !is RegisterUIState.Loading
        )
        formState.passwordError?.let { ErrorText(it) }

        Spacer(modifier = Modifier.height(32.dp))

        // --- LABEL DE ESTADO (Carga o Error de Red) ---
        when (uiState) {
            is RegisterUIState.Loading -> {
                Text(
                    text = "Estamos creando tu cuenta...",
                    color = Color(0xFF1A7A7A),
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }
            is RegisterUIState.Error -> {
                Text(
                    text = (uiState as RegisterUIState.Error).message,
                    color = MaterialTheme.colorScheme.error,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }
            else -> {}
        }

        // --- BOTÓN DE ACCIÓN ---
        CustomButton(
            text = "REGISTRARME AHORA",
            isLoading = uiState is RegisterUIState.Loading,
            onClick = { viewModel.register() }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // --- NAVEGACIÓN DE RETORNO ---
        TextButton(onClick = onNavigateToLogin) {
            Text(
                text = "¿Ya tienes una cuenta? Inicia sesión",
                color = Color(0xFF1A7A7A),
                fontWeight = FontWeight.Medium
            )
        }
    }
}

/**
 * Componente auxiliar para mostrar los errores bajo los inputs
 */
@Composable
fun ErrorText(message: String) {
    Text(
        text = message,
        color = MaterialTheme.colorScheme.error,
        style = MaterialTheme.typography.labelSmall,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, top = 4.dp)
    )
}