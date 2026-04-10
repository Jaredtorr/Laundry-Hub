package com.example.mylavanderiapp.features.maintenance.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.example.mylavanderiapp.core.auth.GoogleSignInHelper
import com.example.mylavanderiapp.core.navigation.FeatureNavGraph
import com.example.mylavanderiapp.core.navigation.Home
import com.example.mylavanderiapp.core.navigation.Login
import com.example.mylavanderiapp.core.navigation.Maintenance
import com.example.mylavanderiapp.features.maintenance.presentation.screens.MaintenanceRoute
import javax.inject.Inject

class MaintenanceNavGraph @Inject constructor() : FeatureNavGraph {

    override fun registerGraph(
        navGraphBuilder: NavGraphBuilder,
        navController: NavHostController,
        googleSignInHelper: GoogleSignInHelper?
    ) {
        navGraphBuilder.composable<Maintenance> {
            MaintenanceRoute(
                onLogout = {
                    navController.navigate(Login) { popUpTo(0) { inclusive = true } }
                },
                onNavigateToHome = {
                    navController.navigate(Home) { popUpTo(Home) { inclusive = false } }
                }
            )
        }
    }
}