package com.example.mylavanderiapp.features.machines.presentation.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.example.mylavanderiapp.core.navigation.FeatureNavGraph
import com.example.mylavanderiapp.core.navigation.Home
import com.example.mylavanderiapp.core.navigation.Login
import com.example.mylavanderiapp.features.machines.presentation.screens.HomeScreen
import com.example.mylavanderiapp.features.machines.presentation.viewmodels.HomeViewModel
import javax.inject.Inject

class MachinesNavGraph @Inject constructor() : FeatureNavGraph {

    override fun registerGraph(
        navGraphBuilder: NavGraphBuilder,
        navController: NavHostController
    ) {
        navGraphBuilder.composable<Home> {
            val viewModel: HomeViewModel = hiltViewModel()
            HomeScreen(
                viewModel = viewModel,
                onLogout  = {
                    navController.navigate(Login) { popUpTo(Home) { inclusive = true } }
                },
                onMyTurns = {
                    // navController.navigate(MyTurns)
                }
            )
        }
    }
}