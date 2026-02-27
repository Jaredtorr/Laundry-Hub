package com.example.mylavanderiapp.features.laundry_reservation.presentation.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.mylavanderiapp.features.laundry_reservation.presentation.viewmodels.ReservationViewModel

@Composable
fun MyReservationsScreen(
    viewModel: ReservationViewModel,
    onBack: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Mis Reservaciones")
    }
}