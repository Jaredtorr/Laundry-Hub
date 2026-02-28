package com.example.mylavanderiapp.features.auth.presentation.navigation

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.example.mylavanderiapp.core.auth.GoogleSignInHelper
import com.example.mylavanderiapp.core.navigation.FeatureNavGraph
import com.example.mylavanderiapp.core.navigation.Home
import com.example.mylavanderiapp.core.navigation.Login
import com.example.mylavanderiapp.core.navigation.MyReservations
import com.example.mylavanderiapp.core.navigation.Register
import com.example.mylavanderiapp.features.auth.presentation.screens.LoginScreen
import com.example.mylavanderiapp.features.auth.presentation.screens.RegisterScreen
import com.example.mylavanderiapp.features.auth.presentation.states.LoginUIState
import com.example.mylavanderiapp.features.auth.presentation.viewmodels.LoginViewModel
import com.example.mylavanderiapp.features.auth.presentation.viewmodels.RegisterViewModel
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class AuthNavGraph @Inject constructor() : FeatureNavGraph {

    override fun registerGraph(
        navGraphBuilder: NavGraphBuilder,
        navController: NavHostController,
        googleSignInHelper: GoogleSignInHelper?
    ) {
        navGraphBuilder.composable<Login> {
            val viewModel: LoginViewModel = hiltViewModel()

            LoginScreen(
                viewModel = viewModel,
                onNavigateToRegister = { navController.navigate(Register) },
                onLoginSuccess = { user ->
                    val destination = if (user.role == "ADMIN") Home else MyReservations
                    navController.navigate(destination) {
                        popUpTo(Login) { inclusive = true }
                    }
                },
                onGoogleSignIn = {
                    MainScope().launch {
                        googleSignInHelper?.signIn()
                            ?.onSuccess { idToken -> viewModel.googleLogin(idToken) }
                            ?.onFailure { e -> android.util.Log.e("GoogleSignIn", "Error: ${e.message}") }
                    }
                }
            )
        }

        navGraphBuilder.composable<Register> {
            val viewModel: RegisterViewModel = hiltViewModel()
            val loginViewModel: LoginViewModel = hiltViewModel()
            val loginState = loginViewModel.uiState.collectAsState()

            LaunchedEffect(loginState.value) {
                if (loginState.value is LoginUIState.Success) {
                    val user = (loginState.value as LoginUIState.Success).user
                    val destination = if (user.role == "ADMIN") Home else MyReservations
                    navController.navigate(destination) {
                        popUpTo(Register) { inclusive = true }
                    }
                }
            }

            RegisterScreen(
                viewModel = viewModel,
                onNavigateToLogin = { navController.popBackStack() },
                onRegisterSuccess = {
                    navController.navigate(Login) {
                        popUpTo(Register) { inclusive = true }
                    }
                },
                onGoogleSignIn = {
                    MainScope().launch {
                        googleSignInHelper?.signIn()
                            ?.onSuccess { idToken -> loginViewModel.googleLogin(idToken) }
                            ?.onFailure { e -> android.util.Log.e("GoogleSignIn", "Error: ${e.message}") }
                    }
                }
            )
        }
    }
}