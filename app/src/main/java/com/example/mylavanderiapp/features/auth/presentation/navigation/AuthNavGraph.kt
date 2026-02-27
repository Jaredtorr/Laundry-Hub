package com.example.mylavanderiapp.features.auth.presentation.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.example.mylavanderiapp.core.navigation.FeatureNavGraph
import com.example.mylavanderiapp.core.navigation.Home
import com.example.mylavanderiapp.core.navigation.Login
import com.example.mylavanderiapp.core.navigation.MyReservations
import com.example.mylavanderiapp.core.navigation.Register
import com.example.mylavanderiapp.features.auth.presentation.screens.LoginScreen
import com.example.mylavanderiapp.features.auth.presentation.screens.RegisterScreen
import com.example.mylavanderiapp.features.auth.presentation.viewmodels.LoginViewModel
import com.example.mylavanderiapp.features.auth.presentation.viewmodels.RegisterViewModel
import javax.inject.Inject

class AuthNavGraph @Inject constructor() : FeatureNavGraph {

    override fun registerGraph(
        navGraphBuilder: NavGraphBuilder,
        navController: NavHostController
    ) {
        navGraphBuilder.composable<Login> {
            val viewModel: LoginViewModel = hiltViewModel()
            LoginScreen(
                viewModel = viewModel,
                onNavigateToRegister = {
                    navController.navigate(Register)
                },
                onLoginSuccess = { user ->
                    val destination = if (user.role == "ADMIN") Home else MyReservations
                    navController.navigate(destination) {
                        popUpTo(Login) { inclusive = true }
                    }
                }
            )
        }

        navGraphBuilder.composable<Register> {
            val viewModel: RegisterViewModel = hiltViewModel()
            RegisterScreen(
                viewModel = viewModel,
                onNavigateToLogin = {
                    navController.popBackStack()
                },
                onRegisterSuccess = {
                    navController.navigate(Login) {
                        popUpTo(Register) { inclusive = true }
                    }
                }
            )
        }
    }
}