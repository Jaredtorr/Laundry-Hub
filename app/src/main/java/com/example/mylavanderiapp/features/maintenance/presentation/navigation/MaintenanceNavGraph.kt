package com.example.mylavanderiapp.features.maintenance.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.example.mylavanderiapp.core.auth.GoogleSignInHelper
import com.example.mylavanderiapp.core.navigation.FeatureNavGraph
import com.example.mylavanderiapp.core.navigation.Maintenance
import com.example.mylavanderiapp.features.maintenance.presentation.screens.MaintenanceScreen
import javax.inject.Inject

class MaintenanceNavGraph @Inject constructor() : FeatureNavGraph {

    override fun registerGraph(
        navGraphBuilder: NavGraphBuilder,
        navController: NavHostController,
        googleSignInHelper: GoogleSignInHelper?
    ) {
        navGraphBuilder.composable<Maintenance> {
            MaintenanceScreen(
                onLogout = {
                    navController.navigate(com.example.mylavanderiapp.core.navigation.Login) {
                        popUpTo(0) { inclusive = true }
                    }
                },
                onNavigateToHome = {
                    navController.navigate(com.example.mylavanderiapp.core.navigation.Home) {
                        popUpTo(com.example.mylavanderiapp.core.navigation.Home) { inclusive = false }
                    }
                }
            )
        }
    }
}