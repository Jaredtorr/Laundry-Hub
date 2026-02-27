package com.example.mylavanderiapp.features.laundry_reservation.presentation.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.draw.clip
import com.example.mylavanderiapp.core.ui.theme.*
import com.example.mylavanderiapp.features.laundry_reservation.presentation.components.*
import com.example.mylavanderiapp.features.laundry_reservation.presentation.states.ReservationUIState
import com.example.mylavanderiapp.features.laundry_reservation.presentation.viewmodels.ReservationViewModel
import com.example.mylavanderiapp.features.machines.domain.entities.Machine
import com.example.mylavanderiapp.features.machines.domain.entities.MachineStatus
import com.example.mylavanderiapp.features.machines.presentation.states.MachinesUIState

@Composable
fun MyReservationsScreen(
    viewModel: ReservationViewModel,
    onLogout: () -> Unit
) {
    val machinesState  by viewModel.machinesState.collectAsState()
    val createState    by viewModel.createState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    var machineToReserve by remember { mutableStateOf<Machine?>(null) }
    var visible          by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        visible = true
        viewModel.loadMachines()
    }

    LaunchedEffect(createState) {
        when (createState) {
            is ReservationUIState.Success -> {
                snackbarHostState.showSnackbar("✅ Lavadora apartada exitosamente")
                viewModel.resetCreateState()
                viewModel.loadMachines()
            }
            is ReservationUIState.Error -> {
                snackbarHostState.showSnackbar((createState as ReservationUIState.Error).message)
                viewModel.resetCreateState()
            }
            else -> {}
        }
    }

    val machines = (machinesState as? MachinesUIState.Success)?.machines ?: emptyList()
    val available   = machines.count { it.status == MachineStatus.AVAILABLE }
    val occupied    = machines.count { it.status == MachineStatus.OCCUPIED }

    Scaffold(snackbarHost = { SnackbarHost(snackbarHostState) }) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(BgLight)
        ) {
            ReservationHeader(
                available = available,
                occupied  = occupied,
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
                                "Lavadoras disponibles",
                                fontFamily = Poppins,
                                fontWeight = FontWeight.Bold,
                                fontSize   = 16.sp,
                                color      = TextDark,
                                modifier   = Modifier.padding(bottom = 16.dp)
                            )

                            when (machinesState) {
                                is MachinesUIState.Loading -> {
                                    Box(
                                        modifier = Modifier.fillMaxSize(),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        CircularProgressIndicator(color = Brand)
                                    }
                                }
                                is MachinesUIState.Success -> {
                                    if (machines.isEmpty()) {
                                        Box(
                                            modifier = Modifier.fillMaxSize(),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                "No hay lavadoras registradas",
                                                fontFamily = Poppins,
                                                color      = TextMid
                                            )
                                        }
                                    } else {
                                        LazyColumn(
                                            verticalArrangement = Arrangement.spacedBy(12.dp)
                                        ) {
                                            items(machines) { machine ->
                                                MachineReservationCard(
                                                    machine   = machine,
                                                    isLoading = createState is ReservationUIState.Loading,
                                                    onReserve = { machineToReserve = it }
                                                )
                                            }
                                        }
                                    }
                                }
                                is MachinesUIState.Error -> {
                                    Box(
                                        modifier = Modifier.fillMaxSize(),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Column(
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                            verticalArrangement = Arrangement.spacedBy(12.dp)
                                        ) {
                                            Text(
                                                (machinesState as MachinesUIState.Error).message,
                                                fontFamily = Poppins,
                                                color      = ErrorRed
                                            )
                                            Box(
                                                modifier = Modifier
                                                    .clip(RoundedCornerShape(14.dp))
                                                    .background(Brand)
                                                    .padding(horizontal = 24.dp, vertical = 12.dp)
                                            ) {
                                                TextButton(onClick = { viewModel.loadMachines() }) {
                                                    Text(
                                                        "Reintentar",
                                                        color      = Color.White,
                                                        fontFamily = Poppins,
                                                        fontWeight = FontWeight.Bold
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                                else -> {}
                            }
                        }
                    }
                }
            }
        }
    }

    machineToReserve?.let { machine ->
        ReservationConfirmDialog(
            machine   = machine,
            onConfirm = {
                viewModel.createReservation(machine.id)
                machineToReserve = null
            },
            onDismiss = { machineToReserve = null }
        )
    }
}