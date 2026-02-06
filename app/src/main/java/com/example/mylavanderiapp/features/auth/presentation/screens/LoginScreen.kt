package com.example.mylavanderiapp.features.auth.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mylavanderiapp.core.ui.theme.*
import com.example.mylavanderiapp.features.auth.presentation.components.CustomButton
import com.example.mylavanderiapp.features.auth.presentation.components.CustomTextField
import com.example.mylavanderiapp.features.auth.presentation.states.LoginUIState
import com.example.mylavanderiapp.features.auth.presentation.viewmodels.LoginViewModel

@Composable
fun LoginScreen(
    viewModel: LoginViewModel,
    onNavigateToRegister: () -> Unit,
    onLoginSuccess: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val formState by viewModel.formState.collectAsState()
    val scrollState = rememberScrollState()

    LaunchedEffect(uiState) {
        when (uiState) {
            is LoginUIState.Success -> {
                onLoginSuccess()
                viewModel.resetState()
            }
            else -> {}
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        PrimaryTealDark,
                        BackgroundTeal
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(80.dp))

            // ... (Círculo e Icono se mantienen igual)
            Box(
                modifier = Modifier
                    .size(140.dp)
                    .clip(CircleShape)
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.LocalLaundryService,
                    contentDescription = "Laundry Icon",
                    modifier = Modifier.size(80.dp),
                    tint = PrimaryTealDark
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "LAUNDRY HUB",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                letterSpacing = 3.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Welcome Back!",
                fontSize = 18.sp,
                fontWeight = FontWeight.Normal,
                color = Color.White.copy(alpha = 0.9f)
            )

            Spacer(modifier = Modifier.height(50.dp))

            // Campo Email y su Error local
            CustomTextField(
                value = formState.email,
                onValueChange = { viewModel.onEmailChange(it) },
                placeholder = "Email Address",
                leadingIcon = Icons.Filled.Email,
                keyboardType = KeyboardType.Email,
                enabled = uiState !is LoginUIState.Loading
            )

            if (formState.emailError != null) {
                Text(
                    text = formState.emailError!!,
                    color = Color.Yellow, // Color resaltado para fondo oscuro
                    fontSize = 12.sp,
                    modifier = Modifier.fillMaxWidth().padding(start = 16.dp, top = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Campo Password y su Error local
            CustomTextField(
                value = formState.password,
                onValueChange = { viewModel.onPasswordChange(it) },
                placeholder = "Password",
                isPassword = true,
                keyboardType = KeyboardType.Password,
                enabled = uiState !is LoginUIState.Loading
            )

            if (formState.passwordError != null) {
                Text(
                    text = formState.passwordError!!,
                    color = Color.Yellow,
                    fontSize = 12.sp,
                    modifier = Modifier.fillMaxWidth().padding(start = 16.dp, top = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // ... (Row de Remember Me y Forgot Password se mantiene igual)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = formState.rememberMe,
                        onCheckedChange = { viewModel.onRememberMeChange(it) },
                        enabled = uiState !is LoginUIState.Loading,
                        colors = CheckboxDefaults.colors(
                            checkedColor = Color.White,
                            uncheckedColor = Color.White.copy(alpha = 0.6f),
                            checkmarkColor = PrimaryTealDark
                        )
                    )
                    Text(text = "Remember Me", color = Color.White, fontSize = 14.sp)
                }
                Text(
                    text = "Forgot Password?",
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { }
                )
            }

            Spacer(modifier = Modifier.height(30.dp))

            // --- NUEVOS LABELS DE ESTADO DEL SERVIDOR ---
            when (uiState) {
                is LoginUIState.Loading -> {
                    Text(
                        text = "Verificando credenciales...",
                        color = Color.White.copy(alpha = 0.8f),
                        fontSize = 14.sp,
                        modifier = Modifier.padding(bottom = 10.dp)
                    )
                }
                is LoginUIState.Error -> {
                    Text(
                        text = (uiState as LoginUIState.Error).message,
                        color = Color(0xFFFFCDD2),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 10.dp)
                    )
                }
                is LoginUIState.Success -> {
                    Text(
                        text = "¡Ingreso exitoso!",
                        color = Color(0xFFB2DFDB),
                        fontSize = 14.sp,
                        modifier = Modifier.padding(bottom = 10.dp)
                    )
                }
                else -> {}
            }

            CustomButton(
                text = "LOG IN",
                onClick = { viewModel.login() },
                isLoading = uiState is LoginUIState.Loading,
                icon = Icons.Filled.Login
            )

            Spacer(modifier = Modifier.height(32.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(text = "Don't have an account? ", color = Color.White, fontSize = 14.sp)
                Text(
                    text = "Sign Up",
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { onNavigateToRegister() }
                )
            }

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}