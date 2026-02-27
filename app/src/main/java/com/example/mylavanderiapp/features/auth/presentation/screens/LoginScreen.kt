package com.example.mylavanderiapp.features.auth.presentation.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mylavanderiapp.core.ui.theme.*
import com.example.mylavanderiapp.features.auth.domain.entities.User
import com.example.mylavanderiapp.features.auth.presentation.components.*
import com.example.mylavanderiapp.features.auth.presentation.states.LoginUIState
import com.example.mylavanderiapp.features.auth.presentation.viewmodels.LoginViewModel

@Composable
fun LoginScreen(
    viewModel: LoginViewModel,
    onNavigateToRegister: () -> Unit,
    onLoginSuccess: (user: User) -> Unit,
    onGoogleSignIn: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    val formState by viewModel.formState.collectAsState()
    val scrollState = rememberScrollState()

    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { visible = true }

    LaunchedEffect(uiState) {
        if (uiState is LoginUIState.Success) {
            onLoginSuccess((uiState as LoginUIState.Success).user)
            viewModel.resetState()
        }
    }

    Box(modifier = Modifier.fillMaxSize().background(SurfaceWhite)) {
        LoginHeader()

        Column(
            modifier = Modifier.fillMaxSize().verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(252.dp))

            AnimatedVisibility(
                visible = visible,
                enter = fadeIn(tween(550)) + slideInVertically(
                    initialOffsetY = { 140 },
                    animationSpec = tween(550, easing = EaseOutCubic)
                )
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp, bottomStart = 0.dp, bottomEnd = 0.dp),
                    colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 28.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("Bienvenido 👋", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = TextDark)
                        Text("Ingresa tus credenciales para continuar", fontSize = 12.sp, color = TextMid, modifier = Modifier.padding(top = 4.dp, bottom = 22.dp))

                        AuthTextField(value = formState.email, onValueChange = { viewModel.onEmailChange(it) }, label = "Correo electrónico", icon = Icons.Outlined.Email, keyboardType = KeyboardType.Email, enabled = uiState !is LoginUIState.Loading)
                        formState.emailError?.let { AuthError(it) }

                        Spacer(Modifier.height(12.dp))

                        AuthTextField(value = formState.password, onValueChange = { viewModel.onPasswordChange(it) }, label = "Contraseña", icon = Icons.Outlined.Lock, isPassword = true, enabled = uiState !is LoginUIState.Loading)
                        formState.passwordError?.let { AuthError(it) }

                        Spacer(Modifier.height(18.dp))

                        AnimatedVisibility(visible = uiState is LoginUIState.Loading || uiState is LoginUIState.Error || uiState is LoginUIState.Success) {
                            when (uiState) {
                                is LoginUIState.Loading -> AuthStateBar("Verificando credenciales…", Brand)
                                is LoginUIState.Error -> AuthStateBar((uiState as LoginUIState.Error).message, ErrorRed)
                                is LoginUIState.Success -> AuthStateBar("¡Bienvenido! ✓", SuccessGreen)
                                else -> {}
                            }
                        }

                        AuthGradientButton(text = "INICIAR SESIÓN", isLoading = uiState is LoginUIState.Loading, onClick = { viewModel.login() })

                        Spacer(Modifier.height(20.dp))
                        AuthOrDivider()
                        Spacer(Modifier.height(16.dp))
                        AuthGoogleButton(onClick = onGoogleSignIn)
                        Spacer(Modifier.height(24.dp))

                        Row {
                            Text("¿No tienes cuenta? ", fontSize = 13.sp, color = TextMid)
                            Text("Regístrate", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Brand, modifier = Modifier.clickable { onNavigateToRegister() })
                        }
                    }
                }
            }
            Spacer(Modifier.height(48.dp))
        }
    }
}