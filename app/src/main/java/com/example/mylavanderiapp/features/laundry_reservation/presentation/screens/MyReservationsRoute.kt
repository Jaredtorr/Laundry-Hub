package com.example.mylavanderiapp.features.laundry_reservation.presentation.screens

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.*
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import com.example.mylavanderiapp.features.laundry_reservation.presentation.states.ReservationUIState
import com.example.mylavanderiapp.features.laundry_reservation.presentation.viewmodels.ReservationViewModel

@Composable
fun MyReservationsRoute(
    viewModel: ReservationViewModel = hiltViewModel(),
    onLogout: () -> Unit
) {
    val machinesState  by viewModel.machinesState.collectAsState()
    val createState    by viewModel.createState.collectAsState()
    val availableCount by viewModel.availableCount.collectAsState()
    val occupiedCount  by viewModel.occupiedCount.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    val lifecycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(lifecycleOwner) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
            viewModel.loadMachines()
        }
    }

    LaunchedEffect(createState) {
        val state = createState
        when (state) {
            is ReservationUIState.Success -> {
                snackbarHostState.showSnackbar("✅ Lavadora apartada exitosamente")
                viewModel.resetCreateState()
                viewModel.loadMachines()
            }
            is ReservationUIState.Error -> {
                snackbarHostState.showSnackbar(state.message)
                viewModel.resetCreateState()
            }
            else -> {}
        }
    }

    MyReservationsScreen(
        machinesState     = machinesState,
        createState       = createState,
        availableCount    = availableCount,
        occupiedCount     = occupiedCount,
        snackbarHostState = snackbarHostState,
        onCreateReservation = viewModel::createReservation,
        onRetryLoad         = viewModel::loadMachines,
        onLogout            = onLogout
    )
}