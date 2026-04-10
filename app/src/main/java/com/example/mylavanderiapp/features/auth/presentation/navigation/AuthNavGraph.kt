package com.example.mylavanderiapp.features.auth.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.example.mylavanderiapp.core.auth.GoogleSignInHelper
import com.example.mylavanderiapp.core.navigation.FeatureNavGraph
import com.example.mylavanderiapp.core.navigation.Home
import com.example.mylavanderiapp.core.navigation.Login
import com.example.mylavanderiapp.core.navigation.MyReservations
import com.example.mylavanderiapp.core.navigation.Register
import com.example.mylavanderiapp.features.auth.presentation.screens.LoginRoute
import com.example.mylavanderiapp.features.auth.presentation.screens.RegisterRoute
import javax.inject.Inject

class AuthNavGraph @Inject constructor() : FeatureNavGraph {

    override fun registerGraph(
        navGraphBuilder: NavGraphBuilder,
        navController: NavHostController,
        googleSignInHelper: GoogleSignInHelper?
    ) {
        val googleSignIn: suspend () -> Result<String> = {
            googleSignInHelper?.signIn() ?: Result.failure(Exception("Google Sign In no disponible"))
        }

        navGraphBuilder.composable<Login> {
            LoginRoute(
                onNavigateToRegister = { navController.navigate(Register) },
                onLoginSuccess = { user ->
                    navController.navigate(if (user.role == "ADMIN") Home else MyReservations) {
                        popUpTo(Login) { inclusive = true }
                    }
                },
                onGoogleSignIn = googleSignIn
            )
        }

        navGraphBuilder.composable<Register> {
            RegisterRoute(
                onNavigateToLogin = { navController.popBackStack() },
                onRegisterSuccess = {
                    navController.navigate(Login) {
                        popUpTo(Register) { inclusive = true }
                    }
                },
                onGoogleSignInSuccess = { user ->
                    navController.navigate(if (user.role == "ADMIN") Home else MyReservations) {
                        popUpTo(Register) { inclusive = true }
                    }
                },
                onGoogleSignIn = googleSignIn
            )
        }
    }
}