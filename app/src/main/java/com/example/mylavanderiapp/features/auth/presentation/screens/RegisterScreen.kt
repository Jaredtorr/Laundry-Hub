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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mylavanderiapp.core.ui.theme.*
import com.example.mylavanderiapp.features.auth.presentation.components.CustomTextField
import com.example.mylavanderiapp.features.auth.presentation.states.RegisterUIState
import com.example.mylavanderiapp.features.auth.presentation.viewmodels.RegisterViewModel

@Composable
fun RegisterScreen(
    viewModel: RegisterViewModel,
    onNavigateToLogin: () -> Unit,
    onRegisterSuccess: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val formState by viewModel.formState.collectAsState()
    val scrollState = rememberScrollState()

    LaunchedEffect(uiState) {
        when (uiState) {
            is RegisterUIState.Success -> {
                onRegisterSuccess()
                viewModel.resetState()
            }
            else -> {}
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundTeal)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(60.dp))

            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.LocalLaundryService,
                    contentDescription = "Laundry Icon",
                    modifier = Modifier.size(70.dp),
                    tint = Color.White
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "LAUNDRY HUB",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                letterSpacing = 2.sp
            )

            Spacer(modifier = Modifier.height(40.dp))

            Text(
                text = "CREATE ACCOUNT",
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.White,
                letterSpacing = 1.sp
            )

            Spacer(modifier = Modifier.height(32.dp))

            CustomTextField(
                value = formState.fullName,
                onValueChange = { viewModel.onFullNameChange(it) },
                placeholder = "Full Name",
                leadingIcon = Icons.Filled.Person,
                enabled = uiState !is RegisterUIState.Loading
            )

            if (formState.fullNameError != null) {
                Text(
                    text = formState.fullNameError!!,
                    color = Error,
                    fontSize = 12.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, top = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            CustomTextField(
                value = formState.email,
                onValueChange = { viewModel.onEmailChange(it) },
                placeholder = "Email Address",
                leadingIcon = Icons.Filled.Email,
                keyboardType = KeyboardType.Email,
                enabled = uiState !is RegisterUIState.Loading
            )

            if (formState.emailError != null) {
                Text(
                    text = formState.emailError!!,
                    color = Error,
                    fontSize = 12.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, top = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            CustomTextField(
                value = formState.password,
                onValueChange = { viewModel.onPasswordChange(it) },
                placeholder = "Password",
                isPassword = true,
                keyboardType = KeyboardType.Password,
                enabled = uiState !is RegisterUIState.Loading
            )

            if (formState.passwordError != null) {
                Text(
                    text = formState.passwordError!!,
                    color = Error,
                    fontSize = 12.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, top = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Text(
                    text = "Remember Me",
                    color = Color.White,
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.weight(1f))
                Switch(
                    checked = formState.rememberMe,
                    onCheckedChange = { viewModel.onRememberMeChange(it) },
                    enabled = uiState !is RegisterUIState.Loading,
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = PrimaryTealDark,
                        uncheckedThumbColor = Color.White,
                        uncheckedTrackColor = Color.White.copy(alpha = 0.3f)
                    )
                )
            }




            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Already have you account? ",
                    color = Color.White,
                    fontSize = 14.sp
                )
                Text(
                    text = "Log In",
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { onNavigateToLogin() }
                )
            }

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}