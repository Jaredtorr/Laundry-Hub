package com.example.mylavanderiapp.features.auth.presentation.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
    val uiState   by viewModel.uiState.collectAsState()
    val scrollState = rememberScrollState()

    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { visible = true }

    LaunchedEffect(uiState) {
        if (uiState is RegisterUIState.Success) {
            onRegisterSuccess()
            viewModel.resetState()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BgLight)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp)
                .background(
                    Brush.linearGradient(
                        colors = listOf(AccentCyan, Brand, BrandDark),
                        start  = androidx.compose.ui.geometry.Offset(1400f, 0f),
                        end    = androidx.compose.ui.geometry.Offset(0f, 800f)
                    )
                )
        ) {
            // Círculos decorativos
            Box(
                Modifier
                    .size(160.dp)
                    .align(Alignment.TopStart)
                    .offset(x = (-50).dp, y = (-40).dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.08f))
            )
            Box(
                Modifier
                    .size(90.dp)
                    .align(Alignment.BottomEnd)
                    .offset(x = 30.dp, y = 30.dp)
                    .clip(CircleShape)
                    .background(BrandDark.copy(alpha = 0.20f))
            )

            // Botón volver
            Box(
                modifier = Modifier
                    .padding(top = 48.dp, start = 20.dp)
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.18f))
                    .clickable { onNavigateToLogin() },
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Filled.ArrowBack, contentDescription = "Back", tint = Color.White, modifier = Modifier.size(20.dp))
            }

            // Texto cabecera
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 52.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color.White.copy(alpha = 0.18f))
                        .padding(horizontal = 14.dp, vertical = 5.dp)
                ) {
                    Text("NEW ACCOUNT", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color.White, letterSpacing = 2.sp)
                }

                Spacer(Modifier.height(10.dp))

                Text(
                    "Create Account",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White
                )
                Text(
                    "Join Laundry Hub today",
                    fontSize = 12.sp,
                    color = Color.White.copy(alpha = 0.75f),
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(172.dp))

            AnimatedVisibility(
                visible = visible,
                enter   = fadeIn(tween(550)) + slideInVertically(
                    initialOffsetY = { 140 },
                    animationSpec  = tween(550, easing = EaseOutCubic)
                )
            ) {
                Card(
                    modifier  = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    shape     = RoundedCornerShape(28.dp),
                    colors    = CardDefaults.cardColors(containerColor = SurfaceWhite),
                    elevation = CardDefaults.cardElevation(defaultElevation = 18.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp, vertical = 28.dp)
                    ) {

                        Spacer(Modifier.height(22.dp))

                        RegisterFieldLabel("Full Name", Icons.Outlined.Person)
                        AuthTextField(
                            value = formState.fullName,
                            onValueChange = { viewModel.onFullNameChange(it) },
                            label   = "Ingresa tu nombre completo",
                            icon    = Icons.Outlined.Person,
                            enabled = uiState !is RegisterUIState.Loading
                        )
                        formState.fullNameError?.let { AuthError(it) }

                        Spacer(Modifier.height(14.dp))

                        RegisterFieldLabel("Email Address", Icons.Outlined.Email)
                        AuthTextField(
                            value = formState.email,
                            onValueChange = { viewModel.onEmailChange(it) },
                            label   = "john@example.com",
                            icon    = Icons.Outlined.Email,
                            keyboardType = KeyboardType.Email,
                            enabled = uiState !is RegisterUIState.Loading
                        )
                        formState.emailError?.let { AuthError(it) }

                        Spacer(Modifier.height(14.dp))

                        RegisterFieldLabel("Password", Icons.Outlined.Lock)
                        AuthTextField(
                            value = formState.password,
                            onValueChange = { viewModel.onPasswordChange(it) },
                            label      = "Min. 8 characters",
                            icon       = Icons.Outlined.Lock,
                            isPassword = true,
                            enabled    = uiState !is RegisterUIState.Loading
                        )
                        formState.passwordError?.let { AuthError(it) }

                        Spacer(Modifier.height(22.dp))

                        AnimatedVisibility(
                            visible = uiState is RegisterUIState.Loading || uiState is RegisterUIState.Error
                        ) {
                            when (uiState) {
                                is RegisterUIState.Loading -> AuthStateBar("Creating your account…", Brand)
                                is RegisterUIState.Error   -> AuthStateBar((uiState as RegisterUIState.Error).message, ErrorRed)
                                else -> {}
                            }
                        }

                        AuthGradientButton(
                            text      = "CREATE ACCOUNT",
                            isLoading = uiState is RegisterUIState.Loading,
                            onClick   = { viewModel.register() }
                        )

                        Spacer(Modifier.height(18.dp))

                        Text(
                            "By creating an account you agree to our Terms of Service and Privacy Policy",
                            fontSize   = 10.sp,
                            color      = TextMid.copy(alpha = 0.7f),
                            textAlign  = TextAlign.Center,
                            lineHeight = 15.sp,
                            modifier   = Modifier.fillMaxWidth()
                        )

                        Spacer(Modifier.height(20.dp))

                        AuthOrDivider()

                        Spacer(Modifier.height(16.dp))

                        AuthGoogleButton(onClick = onGoogleSignIn)

                        Spacer(Modifier.height(24.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text("Already have an account? ", fontSize = 13.sp, color = TextMid)
                            Text(
                                "Log In",
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

@Composable
private fun RegisterFieldLabel(text: String, icon: androidx.compose.ui.graphics.vector.ImageVector) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 6.dp, start = 2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, tint = Brand, modifier = Modifier.size(14.dp))
        Spacer(Modifier.width(5.dp))
        Text(text, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Brand)
    }
}