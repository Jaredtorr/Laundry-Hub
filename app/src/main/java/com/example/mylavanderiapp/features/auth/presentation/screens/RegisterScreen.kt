package com.example.mylavanderiapp.features.auth.presentation.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mylavanderiapp.core.ui.theme.*
import com.example.mylavanderiapp.features.auth.presentation.components.*
import com.example.mylavanderiapp.features.auth.presentation.states.RegisterUIState
import com.example.mylavanderiapp.features.auth.presentation.viewmodels.RegisterViewModel

@Composable
fun RegisterScreen(
    viewModel: RegisterViewModel,
    onNavigateToLogin: () -> Unit,
    onRegisterSuccess: () -> Unit,
    onGoogleSignIn: () -> Unit = {}
) {
    val formState by viewModel.formState.collectAsState()
    val uiState by viewModel.uiState.collectAsState()
    val scrollState = rememberScrollState()

    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { visible = true }

    LaunchedEffect(uiState) {
        if (uiState is RegisterUIState.Success) {
            onRegisterSuccess()
            viewModel.resetState()
        }
    }

    Box(modifier = Modifier.fillMaxSize().background(SurfaceWhite)) {
        RegisterHeader(onNavigateToLogin = onNavigateToLogin)

        Box(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(top = 48.dp, start = 20.dp)
                .size(40.dp)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.18f))
                .clickable {
                    android.util.Log.d("RegisterScreen", "Back button clicked")
                    onNavigateToLogin()
                },
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Filled.ArrowBack, contentDescription = "Regresar", tint = Color.White, modifier = Modifier.size(20.dp))
        }

        Column(
            modifier = Modifier.fillMaxSize().verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(172.dp))

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
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 28.dp)
                    ) {
                        Text("Crear cuenta 🎉", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = TextDark)
                        Text("Completa los datos para registrarte", fontSize = 12.sp, color = TextMid, modifier = Modifier.padding(top = 4.dp, bottom = 22.dp))

                        AuthTextField(value = formState.name, onValueChange = { viewModel.onNameChange(it) }, label = "Nombre(s)", icon = Icons.Outlined.Person, enabled = uiState !is RegisterUIState.Loading)
                        formState.nameError?.let { AuthError(it) }

                        Spacer(Modifier.height(12.dp))

                        AuthTextField(value = formState.paternalSurname, onValueChange = { viewModel.onPaternalSurnameChange(it) }, label = "Apellido paterno", icon = Icons.Outlined.Badge, enabled = uiState !is RegisterUIState.Loading)
                        formState.paternalSurnameError?.let { AuthError(it) }

                        Spacer(Modifier.height(12.dp))

                        AuthTextField(value = formState.email, onValueChange = { viewModel.onEmailChange(it) }, label = "Correo electrónico", icon = Icons.Outlined.Email, keyboardType = KeyboardType.Email, enabled = uiState !is RegisterUIState.Loading)
                        formState.emailError?.let { AuthError(it) }

                        Spacer(Modifier.height(12.dp))

                        AuthTextField(value = formState.password, onValueChange = { viewModel.onPasswordChange(it) }, label = "Contraseña", icon = Icons.Outlined.Lock, isPassword = true, enabled = uiState !is RegisterUIState.Loading)
                        formState.passwordError?.let { AuthError(it) }

                        Spacer(Modifier.height(18.dp))

                        AnimatedVisibility(visible = uiState is RegisterUIState.Loading || uiState is RegisterUIState.Error) {
                            when (uiState) {
                                is RegisterUIState.Loading -> AuthStateBar("Creando tu cuenta…", Brand)
                                is RegisterUIState.Error -> AuthStateBar((uiState as RegisterUIState.Error).message, ErrorRed)
                                else -> {}
                            }
                        }

                        AuthGradientButton(text = "CREAR CUENTA", isLoading = uiState is RegisterUIState.Loading, onClick = { viewModel.register() })

                        Spacer(Modifier.height(18.dp))

                        Text("Al crear una cuenta aceptas nuestros Términos de Servicio y Política de Privacidad", fontSize = 10.sp, color = TextMid.copy(alpha = 0.7f), textAlign = TextAlign.Center, lineHeight = 15.sp, modifier = Modifier.fillMaxWidth())

                        Spacer(Modifier.height(20.dp))
                        AuthOrDivider()
                        Spacer(Modifier.height(16.dp))
                        AuthGoogleButton(onClick = onGoogleSignIn)
                        Spacer(Modifier.height(24.dp))

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                            Text("¿Ya tienes cuenta? ", fontSize = 13.sp, color = TextMid)
                            Text("Inicia sesión", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Brand, modifier = Modifier.clickable { onNavigateToLogin() })
                        }
                    }
                }
            }
            Spacer(Modifier.height(48.dp))
        }
    }
}