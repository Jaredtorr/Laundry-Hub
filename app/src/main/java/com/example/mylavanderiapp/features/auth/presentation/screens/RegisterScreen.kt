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
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mylavanderiapp.core.ui.theme.*
import com.example.mylavanderiapp.features.auth.presentation.components.*
import com.example.mylavanderiapp.features.auth.presentation.states.RegisterFormState
import com.example.mylavanderiapp.features.auth.presentation.states.RegisterUIState

@Composable
fun RegisterScreen(
    uiState: RegisterUIState,
    formState: RegisterFormState,
    onNameChange: (String) -> Unit,
    onPaternalSurnameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onRegister: () -> Unit,
    onNavigateToLogin: () -> Unit,
    onGoogleSignIn: () -> Unit
) {
    val scrollState = rememberScrollState()
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { visible = true }

    Box(modifier = Modifier.fillMaxSize().background(SurfaceWhite)) {
        RegisterHeader(onNavigateToLogin = onNavigateToLogin)

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
                    shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
                    colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 28.dp)
                    ) {
                        Text("Crear cuenta 🎉", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = TextDark)
                        Text(
                            "Completa los datos para registrarte",
                            fontSize = 12.sp,
                            color = TextMid,
                            modifier = Modifier.padding(top = 4.dp, bottom = 22.dp)
                        )

                        AuthTextField(
                            value = formState.name,
                            onValueChange = onNameChange,
                            label = "Nombre(s)",
                            icon = Icons.Outlined.Person,
                            enabled = uiState !is RegisterUIState.Loading
                        )
                        formState.nameError?.let { AuthError(it) }

                        Spacer(Modifier.height(12.dp))

                        AuthTextField(
                            value = formState.paternalSurname,
                            onValueChange = onPaternalSurnameChange,
                            label = "Apellido paterno",
                            icon = Icons.Outlined.Badge,
                            enabled = uiState !is RegisterUIState.Loading
                        )
                        formState.paternalSurnameError?.let { AuthError(it) }

                        Spacer(Modifier.height(12.dp))

                        AuthTextField(
                            value = formState.email,
                            onValueChange = onEmailChange,
                            label = "Correo electrónico",
                            icon = Icons.Outlined.Email,
                            keyboardType = KeyboardType.Email,
                            enabled = uiState !is RegisterUIState.Loading
                        )
                        formState.emailError?.let { AuthError(it) }

                        Spacer(Modifier.height(12.dp))

                        AuthTextField(
                            value = formState.password,
                            onValueChange = onPasswordChange,
                            label = "Contraseña",
                            icon = Icons.Outlined.Lock,
                            isPassword = true,
                            enabled = uiState !is RegisterUIState.Loading
                        )
                        formState.passwordError?.let { AuthError(it) }

                        Spacer(Modifier.height(18.dp))

                        AnimatedVisibility(
                            visible = uiState is RegisterUIState.Loading || uiState is RegisterUIState.Error
                        ) {
                            when (uiState) {
                                is RegisterUIState.Loading -> AuthStateBar("Creando tu cuenta…", Brand)
                                is RegisterUIState.Error -> AuthStateBar(uiState.message, ErrorRed)
                                else -> {}
                            }
                        }

                        AuthGradientButton(
                            text = "CREAR CUENTA",
                            isLoading = uiState is RegisterUIState.Loading,
                            onClick = onRegister
                        )

                        Spacer(Modifier.height(18.dp))

                        Text(
                            "Al crear una cuenta aceptas nuestros Términos de Servicio y Política de Privacidad",
                            fontSize = 10.sp,
                            color = TextMid.copy(alpha = 0.7f),
                            textAlign = TextAlign.Center,
                            lineHeight = 15.sp,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(Modifier.height(20.dp))
                        AuthOrDivider()
                        Spacer(Modifier.height(16.dp))
                        AuthGoogleButton(onClick = onGoogleSignIn)
                        Spacer(Modifier.height(24.dp))

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                            Text("¿Ya tienes cuenta? ", fontSize = 13.sp, color = TextMid)
                            Text(
                                "Inicia sesión",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = Brand,
                                modifier = Modifier.clickable { onNavigateToLogin() }
                            )
                        }
                    }
                }
            }
            Spacer(Modifier.height(48.dp))
        }
    }
}