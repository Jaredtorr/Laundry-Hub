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
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mylavanderiapp.features.auth.presentation.states.LoginUIState
import com.example.mylavanderiapp.features.auth.presentation.viewmodels.LoginViewModel

internal val Brand        = Color(0xFF3487F2)
internal val BrandDark    = Color(0xFF1A5FBF)
internal val BrandLight   = Color(0xFF6BAAF7)
internal val BrandPale    = Color(0xFFD6E8FD)
internal val AccentCyan   = Color(0xFF00C6FF)
internal val SurfaceWhite = Color(0xFFFFFFFF)
internal val BgLight      = Color(0xFFF4F8FF)
internal val TextDark     = Color(0xFF0D1B3E)
internal val TextMid      = Color(0xFF4A5A7A)
internal val ErrorRed     = Color(0xFFFF4D6D)
internal val SuccessGreen = Color(0xFF00C896)

@Composable
fun LoginScreen(
    viewModel: LoginViewModel,
    onNavigateToRegister: () -> Unit,
    onLoginSuccess: () -> Unit,
    onGoogleSignIn: () -> Unit = {}
) {
    val uiState   by viewModel.uiState.collectAsState()
    val formState by viewModel.formState.collectAsState()
    val scrollState = rememberScrollState()

    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { visible = true }

    LaunchedEffect(uiState) {
        if (uiState is LoginUIState.Success) {
            onLoginSuccess()
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
                .height(320.dp)
                .background(
                    Brush.linearGradient(
                        colors = listOf(BrandDark, Brand, AccentCyan),
                        start   = androidx.compose.ui.geometry.Offset(0f, 0f),
                        end     = androidx.compose.ui.geometry.Offset(1400f, 800f)
                    )
                )
        ) {
            // Burbujas decorativas
            Box(
                Modifier
                    .size(200.dp)
                    .align(Alignment.TopEnd)
                    .offset(x = 70.dp, y = (-50).dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.07f))
            )
            Box(
                Modifier
                    .size(120.dp)
                    .align(Alignment.BottomStart)
                    .offset(x = (-30).dp, y = 40.dp)
                    .clip(CircleShape)
                    .background(AccentCyan.copy(alpha = 0.15f))
            )
            Box(
                Modifier
                    .size(60.dp)
                    .align(Alignment.TopStart)
                    .offset(x = 40.dp, y = 40.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.05f))
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 60.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.15f))
                        .border(1.5.dp, Color.White.copy(alpha = 0.30f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Filled.LocalLaundryService,
                        contentDescription = null,
                        modifier = Modifier.size(42.dp),
                        tint = Color.White
                    )
                }
                Spacer(Modifier.height(14.dp))
                Text(
                    "LAUNDRY HUB",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White,
                    letterSpacing = 4.sp
                )
                Text(
                    "Sign in to manage your orders",
                    fontSize = 12.sp,
                    color = Color.White.copy(alpha = 0.72f),
                    modifier = Modifier.padding(top = 5.dp)
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(252.dp))

            AnimatedVisibility(
                visible = visible,
                enter = fadeIn(tween(550)) + slideInVertically(
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
                            .padding(horizontal = 24.dp, vertical = 28.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Text("Welcome back 👋", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = TextDark)
                        Text(
                            "Enter your credentials to continue",
                            fontSize = 12.sp, color = TextMid,
                            modifier = Modifier.padding(top = 4.dp, bottom = 22.dp)
                        )

                        AuthTextField(
                            value = formState.email,
                            onValueChange = { viewModel.onEmailChange(it) },
                            label = "Email address",
                            icon  = Icons.Outlined.Email,
                            keyboardType = KeyboardType.Email,
                            enabled = uiState !is LoginUIState.Loading
                        )
                        formState.emailError?.let { AuthError(it) }

                        Spacer(Modifier.height(12.dp))

                        AuthTextField(
                            value = formState.password,
                            onValueChange = { viewModel.onPasswordChange(it) },
                            label = "Password",
                            icon  = Icons.Outlined.Lock,
                            isPassword = true,
                            enabled = uiState !is LoginUIState.Loading
                        )
                        formState.passwordError?.let { AuthError(it) }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Checkbox(
                                    checked = formState.rememberMe,
                                    onCheckedChange = { viewModel.onRememberMeChange(it) },
                                    enabled = uiState !is LoginUIState.Loading,
                                    colors  = CheckboxDefaults.colors(
                                        checkedColor   = Brand,
                                        uncheckedColor = TextMid.copy(alpha = 0.45f),
                                        checkmarkColor = Color.White
                                    )
                                )
                                Text("Remember me", fontSize = 12.sp, color = TextMid)
                            }
                            Text(
                                "Forgot password?",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Brand,
                                modifier = Modifier.clickable {}
                            )
                        }

                        Spacer(Modifier.height(18.dp))

                        AnimatedVisibility(
                            visible = uiState is LoginUIState.Loading
                                    || uiState is LoginUIState.Error
                                    || uiState is LoginUIState.Success
                        ) {
                            when (uiState) {
                                is LoginUIState.Loading -> AuthStateBar("Verifying credentials…",   Brand)
                                is LoginUIState.Error   -> AuthStateBar((uiState as LoginUIState.Error).message, ErrorRed)
                                is LoginUIState.Success -> AuthStateBar("Welcome back! ✓", SuccessGreen)
                                else -> {}
                            }
                        }

                        AuthGradientButton(
                            text      = "LOG IN",
                            isLoading = uiState is LoginUIState.Loading,
                            onClick   = { viewModel.login() }
                        )

                        Spacer(Modifier.height(20.dp))

                        AuthOrDivider()

                        Spacer(Modifier.height(16.dp))

                        AuthGoogleButton(onClick = onGoogleSignIn)

                        Spacer(Modifier.height(24.dp))

                        Row {
                            Text("Don't have an account? ", fontSize = 13.sp, color = TextMid)
                            Text(
                                "Sign up",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = Brand,
                                modifier = Modifier.clickable { onNavigateToRegister() }
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(48.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    icon: ImageVector,
    isPassword: Boolean   = false,
    keyboardType: KeyboardType = KeyboardType.Text,
    enabled: Boolean      = true
) {
    var show by remember { mutableStateOf(false) }
    OutlinedTextField(
        value          = value,
        onValueChange  = onValueChange,
        label          = { Text(label, fontSize = 13.sp) },
        leadingIcon    = {
            Icon(icon, contentDescription = null, tint = Brand, modifier = Modifier.size(20.dp))
        },
        trailingIcon   = if (isPassword) {{
            IconButton(onClick = { show = !show }) {
                Icon(
                    if (show) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                    contentDescription = null,
                    tint = TextMid,
                    modifier = Modifier.size(20.dp)
                )
            }
        }} else null,
        visualTransformation = if (isPassword && !show) PasswordVisualTransformation() else VisualTransformation.None,
        keyboardOptions      = KeyboardOptions(keyboardType = keyboardType),
        enabled  = enabled,
        singleLine = true,
        modifier   = Modifier.fillMaxWidth(),
        shape      = RoundedCornerShape(14.dp),
        colors     = OutlinedTextFieldDefaults.colors(
            focusedBorderColor        = Brand,
            unfocusedBorderColor      = BrandPale,
            focusedLabelColor         = Brand,
            cursorColor               = Brand,
            focusedLeadingIconColor   = Brand,
            unfocusedLeadingIconColor = BrandLight
        )
    )
}

@Composable
fun AuthError(message: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 6.dp, top = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(Icons.Filled.ErrorOutline, null, tint = ErrorRed, modifier = Modifier.size(13.dp))
        Spacer(Modifier.width(4.dp))
        Text(message, color = ErrorRed, fontSize = 11.sp)
    }
}

@Composable
fun AuthStateBar(message: String, color: Color) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(color.copy(alpha = 0.09f))
            .border(1.dp, color.copy(alpha = 0.25f), RoundedCornerShape(10.dp))
            .padding(horizontal = 14.dp, vertical = 10.dp)
    ) {
        Text(
            message, color = color, fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            textAlign  = TextAlign.Center,
            modifier   = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun AuthGradientButton(text: String, isLoading: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(Brush.horizontalGradient(listOf(BrandDark, Brand, AccentCyan)))
            .clickable(enabled = !isLoading, onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier    = Modifier.size(24.dp),
                color       = Color.White,
                strokeWidth = 2.5.dp
            )
        } else {
            Text(text, color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.5.sp)
        }
    }
}

@Composable
fun AuthOrDivider() {
    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Text("  or  ", fontSize = 12.sp, color = TextMid)
    }
}

@Composable
fun AuthGoogleButton(onClick: () -> Unit) {
    OutlinedButton(
        onClick  = onClick,
        modifier = Modifier.fillMaxWidth().height(52.dp),
        shape    = RoundedCornerShape(14.dp),
        colors   = ButtonDefaults.outlinedButtonColors(containerColor = Color.White),
    ) {
        Box(
            modifier = Modifier
                .size(28.dp)
                .clip(CircleShape)
                .background(Color(0xFF4285F4)),
            contentAlignment = Alignment.Center
        ) {
            Text("G", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.ExtraBold)
        }
        Spacer(Modifier.width(12.dp))
        Text("Continue with Google", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = TextDark)
    }
}