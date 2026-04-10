package com.example.mylavanderiapp.features.laundry_reservation.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.example.mylavanderiapp.core.auth.GoogleSignInHelper
import com.example.mylavanderiapp.core.navigation.FeatureNavGraph
import com.example.mylavanderiapp.core.navigation.Login
import com.example.mylavanderiapp.core.navigation.MyReservations
import com.example.mylavanderiapp.features.laundry_reservation.presentation.screens.MyReservationsRoute
import javax.inject.Inject

class ReservationNavGraph @Inject constructor() : FeatureNavGraph {

    override fun registerGraph(
        navGraphBuilder: NavGraphBuilder,
        navController: NavHostController,
        googleSignInHelper: GoogleSignInHelper?
    ) {
        navGraphBuilder.composable<MyReservations> {
            MyReservationsRoute(
                onLogout = {
                    navController.navigate(Login) { popUpTo(MyReservations) { inclusive = true } }
                }
            )
        }
    }
}