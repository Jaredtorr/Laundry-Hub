package com.example.mylavanderiapp.features.auth.presentation.screens

import androidx.compose.runtime.*
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.mylavanderiapp.features.auth.domain.entities.User
import com.example.mylavanderiapp.features.auth.presentation.states.RegisterUIState
import com.example.mylavanderiapp.features.auth.presentation.viewmodels.RegisterViewModel
import kotlinx.coroutines.launch

@Composable
fun RegisterRoute(
    viewModel: RegisterViewModel = hiltViewModel(),
    onNavigateToLogin: () -> Unit,
    onRegisterSuccess: () -> Unit,
    onGoogleSignInSuccess: (user: User) -> Unit,
    onGoogleSignIn: suspend () -> Result<String>
) {
    val uiState by viewModel.uiState.collectAsState()
    val formState by viewModel.formState.collectAsState()
    val scope = rememberCoroutineScope()

    LaunchedEffect(uiState) {
        when (uiState) {
            is RegisterUIState.Success -> {
                onRegisterSuccess()
                viewModel.resetState()
            }
            is RegisterUIState.GoogleSuccess -> {
                onGoogleSignInSuccess((uiState as RegisterUIState.GoogleSuccess).user)
                viewModel.resetState()
            }
            else -> {}
        }
    }

    RegisterScreen(
        uiState = uiState,
        formState = formState,
        onNameChange = viewModel::onNameChange,
        onPaternalSurnameChange = viewModel::onPaternalSurnameChange,
        onEmailChange = viewModel::onEmailChange,
        onPasswordChange = viewModel::onPasswordChange,
        onRegister = viewModel::register,
        onNavigateToLogin = onNavigateToLogin,
        onGoogleSignIn = {
            scope.launch {
                onGoogleSignIn()
                    .onSuccess { idToken -> viewModel.googleLogin(idToken) }
                    .onFailure { e -> android.util.Log.e("GoogleSignIn", "Error: ${e.message}") }
            }
        }
    )
}