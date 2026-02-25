package com.example.mylavanderiapp.core.navigation

//Definición de rutas de navegación de la aplicación

sealed class Routes(val route: String) {
    // Auth Routes
    object Login : Routes("login")
    object Register : Routes("register")

    // Main Routes
    object Home : Routes("home")
    object Machines : Routes("machines")
    object MachineDetail : Routes("machines/{machineId}") {
        fun createRoute(machineId: String) = "machines/$machineId"
    }

    // Future Routes
    object Profile : Routes("profile")
    object MyTurns : Routes("my-turns")
    object Settings : Routes("settings")
}