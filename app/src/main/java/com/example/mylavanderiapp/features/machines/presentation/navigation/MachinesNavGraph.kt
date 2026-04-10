package com.example.mylavanderiapp.features.machines.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.example.mylavanderiapp.core.auth.GoogleSignInHelper
import com.example.mylavanderiapp.core.navigation.FeatureNavGraph
import com.example.mylavanderiapp.core.navigation.Home
import com.example.mylavanderiapp.core.navigation.Login
import com.example.mylavanderiapp.core.navigation.Maintenance
import com.example.mylavanderiapp.features.machines.presentation.screens.HomeRoute
import javax.inject.Inject

class MachinesNavGraph @Inject constructor() : FeatureNavGraph {

    override fun registerGraph(
        navGraphBuilder: NavGraphBuilder,
        navController: NavHostController,
        googleSignInHelper: GoogleSignInHelper?
    ) {
        navGraphBuilder.composable<Home> {
            HomeRoute(
                onLogout = {
                    navController.navigate(Login) { popUpTo(Home) { inclusive = true } }
                },
                onNavigateToMaintenance = {
                    navController.navigate(Maintenance)
                }
            )
        }
    }
}