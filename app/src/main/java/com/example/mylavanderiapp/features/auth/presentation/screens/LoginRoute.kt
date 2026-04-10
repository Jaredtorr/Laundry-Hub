package com.example.mylavanderiapp.features.auth.presentation.screens

import androidx.compose.runtime.*
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.mylavanderiapp.features.auth.domain.entities.User
import com.example.mylavanderiapp.features.auth.presentation.states.LoginUIState
import com.example.mylavanderiapp.features.auth.presentation.viewmodels.LoginViewModel
import kotlinx.coroutines.launch

@Composable
fun LoginRoute(
    viewModel: LoginViewModel = hiltViewModel(),
    onNavigateToRegister: () -> Unit,
    onLoginSuccess: (user: User) -> Unit,
    onGoogleSignIn: suspend () -> Result<String>
) {
    val uiState by viewModel.uiState.collectAsState()
    val formState by viewModel.formState.collectAsState()
    val scope = rememberCoroutineScope()

    LaunchedEffect(uiState) {
        if (uiState is LoginUIState.Success) {
            onLoginSuccess((uiState as LoginUIState.Success).user)
            viewModel.resetState()
        }
    }

    LoginScreen(
        uiState = uiState,
        formState = formState,
        onEmailChange = viewModel::onEmailChange,
        onPasswordChange = viewModel::onPasswordChange,
        onLogin = viewModel::login,
        onNavigateToRegister = onNavigateToRegister,
        onGoogleSignIn = {
            scope.launch {
                onGoogleSignIn()
                    .onSuccess { idToken -> viewModel.googleLogin(idToken) }
                    .onFailure { e -> android.util.Log.e("GoogleSignIn", "Error: ${e.message}") }
            }
        }
    )
}