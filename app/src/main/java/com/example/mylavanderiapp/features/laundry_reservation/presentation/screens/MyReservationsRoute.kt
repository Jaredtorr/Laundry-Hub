package com.example.mylavanderiapp.features.laundry_reservation.presentation.screens

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.*
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import com.example.mylavanderiapp.features.laundry_reservation.presentation.states.ReservationOperationState
import com.example.mylavanderiapp.features.laundry_reservation.presentation.states.ReservationUIState
import com.example.mylavanderiapp.features.laundry_reservation.presentation.viewmodels.ReservationViewModel

@Composable
fun MyReservationsRoute(
    viewModel: ReservationViewModel = hiltViewModel(),
    onLogout : () -> Unit
) {
    val machinesState      by viewModel.machinesState.collectAsState()
    val createState        by viewModel.createState.collectAsState()
    val myReservationsState by viewModel.myReservationsState.collectAsState()
    val operationState     by viewModel.operationState.collectAsState()
    val availableCount     by viewModel.availableCount.collectAsState()
    val occupiedCount      by viewModel.occupiedCount.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    val lifecycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(lifecycleOwner) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
            viewModel.loadMachines()
            viewModel.loadMyReservations()
        }
    }

    LaunchedEffect(createState) {
        val state = createState
        when (state) {
            is ReservationUIState.Success -> {
                snackbarHostState.showSnackbar("✅ Lavadora apartada exitosamente")
                viewModel.resetCreateState()
                viewModel.loadMachines()
                viewModel.loadMyReservations()
            }
            is ReservationUIState.Error -> {
                snackbarHostState.showSnackbar(state.message)
                viewModel.resetCreateState()
            }
            else -> {}
        }
    }

    LaunchedEffect(operationState) {
        val state = operationState
        when (state) {
            is ReservationOperationState.Success -> {
                snackbarHostState.showSnackbar(state.message)
                viewModel.resetOperationState()
            }
            is ReservationOperationState.Error -> {
                snackbarHostState.showSnackbar(
                    message           = state.message,
                    duration          = SnackbarDuration.Long,
                    withDismissAction = true
                )
                viewModel.resetOperationState()
            }
            else -> {}
        }
    }

    MyReservationsScreen(
        machinesState         = machinesState,
        createState           = createState,
        myReservationsState   = myReservationsState,
        availableCount        = availableCount,
        occupiedCount         = occupiedCount,
        snackbarHostState     = snackbarHostState,
        onCreateReservation   = viewModel::createReservation,
        onCancelReservation   = viewModel::cancelReservation,
        onCompleteReservation = viewModel::completeReservation,
        onReportFault         = viewModel::reportFault,
        onRetryMachines       = viewModel::loadMachines,
        onRetryReservations   = viewModel::loadMyReservations,
        onLogout              = onLogout
    )
}