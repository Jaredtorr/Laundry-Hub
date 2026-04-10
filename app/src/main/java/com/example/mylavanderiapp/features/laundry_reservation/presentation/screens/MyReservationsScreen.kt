package com.example.mylavanderiapp.features.laundry_reservation.presentation.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mylavanderiapp.core.ui.theme.*
import com.example.mylavanderiapp.features.laundry_reservation.domain.entities.Reservation
import com.example.mylavanderiapp.features.laundry_reservation.presentation.components.*
import com.example.mylavanderiapp.features.laundry_reservation.presentation.states.MyReservationsUIState
import com.example.mylavanderiapp.features.laundry_reservation.presentation.states.ReservationUIState
import com.example.mylavanderiapp.features.machines.domain.entities.Machine
import com.example.mylavanderiapp.features.machines.presentation.states.MachinesUIState
import com.example.mylavanderiapp.features.maintenance.domain.entities.MaintenanceRecord
import com.example.mylavanderiapp.features.laundry_reservation.presentation.components.UserBottomNavBar
import com.example.mylavanderiapp.features.laundry_reservation.presentation.components.UserNavDestination

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyReservationsScreen(
    machinesState         : MachinesUIState,
    createState           : ReservationUIState,
    myReservationsState   : MyReservationsUIState,
    availableCount        : Int,
    occupiedCount         : Int,
    snackbarHostState     : SnackbarHostState,
    onCreateReservation   : (Int) -> Unit,
    onCancelReservation   : (Int) -> Unit,
    onCompleteReservation : (Int) -> Unit,
    onReportFault         : (MaintenanceRecord) -> Unit,
    onRetryMachines       : () -> Unit,
    onRetryReservations   : () -> Unit,
    onLogout              : () -> Unit
) {
    var currentTab          by remember { mutableStateOf(UserNavDestination.MACHINES) }
    var machineToReserve    by remember { mutableStateOf<Machine?>(null) }
    var reservationToCancel by remember { mutableStateOf<Reservation?>(null) }
    var reservationToComplete by remember { mutableStateOf<Reservation?>(null) }
    var reservationToReport by remember { mutableStateOf<Reservation?>(null) }
    var visible             by remember { mutableStateOf(true) }

    val tabTitle = when (currentTab) {
        UserNavDestination.MACHINES        -> "Lavadoras disponibles"
        UserNavDestination.MY_RESERVATIONS -> "Mis reservaciones"
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            UserBottomNavBar(
                current    = currentTab,
                onNavigate = { currentTab = it }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(BgLight)
        ) {
            ReservationHeader(
                available = availableCount,
                occupied  = occupiedCount,
                onLogout  = onLogout
            )

            AnimatedVisibility(
                visible  = visible,
                enter    = fadeIn(tween(550)) + slideInVertically(
                    initialOffsetY = { 140 },
                    animationSpec  = tween(550, easing = EaseOutCubic)
                ),
                modifier = Modifier.align(Alignment.TopCenter)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 200.dp)
                ) {
                    Card(
                        modifier  = Modifier.fillMaxSize(),
                        shape     = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
                        colors    = CardDefaults.cardColors(containerColor = SurfaceWhite),
                        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 20.dp, vertical = 24.dp)
                        ) {
                            Text(
                                text       = tabTitle,
                                fontFamily = Poppins,
                                fontWeight = FontWeight.Bold,
                                fontSize   = 16.sp,
                                color      = TextDark,
                                modifier   = Modifier.padding(bottom = 16.dp)
                            )

                            AnimatedContent(
                                targetState = currentTab,
                                transitionSpec = {
                                    fadeIn(tween(200)) togetherWith fadeOut(tween(200))
                                },
                                label = "tab_content"
                            ) { tab ->
                                when (tab) {
                                    UserNavDestination.MACHINES -> {
                                        AvailableMachinesContent(
                                            machinesState = machinesState,
                                            createState   = createState,
                                            onReserve     = { machineToReserve = it },
                                            onRetry       = onRetryMachines
                                        )
                                    }
                                    UserNavDestination.MY_RESERVATIONS -> {
                                        UserReservationsContent(
                                            myReservationsState = myReservationsState,
                                            onCancel            = { reservationToCancel = it },
                                            onComplete          = { reservationToComplete = it },
                                            onReportFault       = { reservationToReport = it },
                                            onRetry             = onRetryReservations
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Dialogs
    machineToReserve?.let { machine ->
        ReservationConfirmDialog(
            machine   = machine,
            onConfirm = {
                onCreateReservation(machine.id)
                machineToReserve = null
            },
            onDismiss = { machineToReserve = null }
        )
    }

    reservationToCancel?.let { reservation ->
        CancelReservationDialog(
            reservation = reservation,
            onConfirm   = {
                onCancelReservation(reservation.id)
                reservationToCancel = null
            },
            onDismiss   = { reservationToCancel = null }
        )
    }

    reservationToComplete?.let { reservation ->
        CompleteReservationDialog(
            reservation = reservation,
            onConfirm   = {
                onCompleteReservation(reservation.id)
                reservationToComplete = null
            },
            onDismiss   = { reservationToComplete = null }
        )
    }

    reservationToReport?.let { reservation ->
        ReportFaultDialog(
            reservation = reservation,
            onConfirm   = { record ->
                onReportFault(record)
                reservationToReport = null
            },
            onDismiss   = { reservationToReport = null }
        )
    }
}