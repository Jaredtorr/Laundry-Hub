package com.example.mylavanderiapp.core.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.mylavanderiapp.core.di.AppContainer
import com.example.mylavanderiapp.features.auth.presentation.screens.LoginScreen
import com.example.mylavanderiapp.features.auth.presentation.screens.RegisterScreen
import com.example.mylavanderiapp.features.auth.presentation.viewmodels.LoginViewModel
import com.example.mylavanderiapp.features.auth.presentation.viewmodels.LoginViewModelFactory
import com.example.mylavanderiapp.features.auth.presentation.viewmodels.RegisterViewModel
import com.example.mylavanderiapp.features.auth.presentation.viewmodels.RegisterViewModelFactory
import com.example.mylavanderiapp.features.machines.presentation.screens.HomeScreen
import com.example.mylavanderiapp.features.machines.presentation.screens.MachineDetailScreen
import com.example.mylavanderiapp.features.machines.presentation.viewmodels.HomeViewModel
import com.example.mylavanderiapp.features.machines.presentation.viewmodels.HomeViewModelFactory


 //Maneja todas las rutas y transiciones entre pantallas
@Composable
fun LaundryNavGraph(
    navController: NavHostController,
    appContainer: AppContainer,
    startDestination: String = Routes.Login.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {

        composable(Routes.Login.route) {
            val viewModel: LoginViewModel = viewModel(
                factory = LoginViewModelFactory(
                    loginUseCase = appContainer.authModule.loginUseCase
                )
            )

            LoginScreen(
                viewModel = viewModel,
                onNavigateToRegister = {
                    navController.navigate(Routes.Register.route) {
                        launchSingleTop = true
                    }
                },
                onLoginSuccess = {
                    navController.navigate(Routes.Home.route) {
                        popUpTo(Routes.Login.route) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(Routes.Register.route) {
            val viewModel: RegisterViewModel = viewModel(
                factory = RegisterViewModelFactory(
                    registerUseCase = appContainer.authModule.registerUseCase
                )
            )

            RegisterScreen(
                viewModel = viewModel,
                onNavigateToLogin = {
                    navController.popBackStack()
                },
                onRegisterSuccess = {
                    navController.navigate(Routes.Login.route) {
                        popUpTo(Routes.Register.route) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }


        composable(Routes.Home.route) {
            val viewModel: HomeViewModel = viewModel(
                factory = HomeViewModelFactory(
                    getMachinesUseCase = appContainer.machinesModule.getMachinesUseCase,
                    createMachineUseCase = appContainer.machinesModule.createMachineUseCase,
                    updateMachineUseCase = appContainer.machinesModule.updateMachineUseCase,
                    deleteMachineUseCase = appContainer.machinesModule.deleteMachineUseCase
                )
            )

            HomeScreen(
                viewModel = viewModel,
                onMachineClick = { id ->
                    navController.navigate(Routes.MachineDetail.createRoute(id))
                },
                onLogout = {
                    navController.navigate(Routes.Login.route) {
                        popUpTo(0) { inclusive = true }
                        launchSingleTop = true
                    }
                },
                onMyTurns = {
                    navController.navigate(Routes.MyTurns.route)
                }
            )
        }

        composable(
            route = Routes.MachineDetail.route,
            arguments = listOf(
                navArgument("machineId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val machineId = backStackEntry.arguments?.getString("machineId") ?: ""

            MachineDetailScreen(
                machineId = machineId,
                onBack = {
                    navController.popBackStack()
                }
            )
        }

        // ==========================================
        // FUTURE SCREENS
        // ==========================================

        composable(Routes.MyTurns.route) {
            // Implementar MyTurnsScreen
        }

        composable(Routes.Profile.route) {
            // Implementar ProfileScreen
        }
    }
}