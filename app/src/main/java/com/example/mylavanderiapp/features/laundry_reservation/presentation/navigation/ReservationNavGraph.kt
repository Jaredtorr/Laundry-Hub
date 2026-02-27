package com.example.mylavanderiapp.features.laundry_reservation.presentation.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.example.mylavanderiapp.core.navigation.FeatureNavGraph
import com.example.mylavanderiapp.core.navigation.Login
import com.example.mylavanderiapp.core.navigation.MyReservations
import com.example.mylavanderiapp.features.laundry_reservation.presentation.screens.MyReservationsScreen
import com.example.mylavanderiapp.features.laundry_reservation.presentation.viewmodels.ReservationViewModel
import javax.inject.Inject

class ReservationNavGraph @Inject constructor() : FeatureNavGraph {

    override fun registerGraph(
        navGraphBuilder: NavGraphBuilder,
        navController: NavHostController
    ) {
        navGraphBuilder.composable<MyReservations> {
            val viewModel: ReservationViewModel = hiltViewModel()
            MyReservationsScreen(
                viewModel = viewModel,
                onLogout = {
                    navController.navigate(Login) { popUpTo(MyReservations) { inclusive = true } }
                }
            )
        }
    }
}