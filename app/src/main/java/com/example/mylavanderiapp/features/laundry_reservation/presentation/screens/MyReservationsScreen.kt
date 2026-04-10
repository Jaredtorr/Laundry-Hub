package com.example.mylavanderiapp.features.laundry_reservation.presentation.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mylavanderiapp.core.ui.theme.*
import com.example.mylavanderiapp.features.laundry_reservation.presentation.components.*
import com.example.mylavanderiapp.features.laundry_reservation.presentation.states.ReservationUIState
import com.example.mylavanderiapp.features.machines.domain.entities.Machine
import com.example.mylavanderiapp.features.machines.presentation.states.MachinesUIState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyReservationsScreen(
    machinesState: MachinesUIState,
    createState: ReservationUIState,
    availableCount: Int,
    occupiedCount: Int,
    snackbarHostState: SnackbarHostState,
    onCreateReservation: (Int) -> Unit,
    onRetryLoad: () -> Unit,
    onLogout: () -> Unit
) {
    var machineToReserve by remember { mutableStateOf<Machine?>(null) }
    var visible          by remember { mutableStateOf(true) }
    val isRefreshing     = machinesState is MachinesUIState.Loading
    val pullState        = rememberPullToRefreshState()
    val machines         = (machinesState as? MachinesUIState.Success)?.machines ?: emptyList()

    Scaffold(snackbarHost = { SnackbarHost(snackbarHostState) }) { padding ->
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
                                "Lavadoras disponibles",
                                fontFamily = Poppins,
                                fontWeight = FontWeight.Bold,
                                fontSize   = 16.sp,
                                color      = TextDark,
                                modifier   = Modifier.padding(bottom = 16.dp)
                            )

                            PullToRefreshBox(
                                isRefreshing = isRefreshing,
                                onRefresh    = onRetryLoad,
                                state        = pullState,
                                modifier     = Modifier.fillMaxSize()
                            ) {
                                when (machinesState) {
                                    is MachinesUIState.Loading -> {
                                        Box(
                                            modifier         = Modifier.fillMaxSize(),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            CircularProgressIndicator(color = Brand)
                                        }
                                    }
                                    is MachinesUIState.Success -> {
                                        if (machines.isEmpty()) {
                                            Box(
                                                modifier         = Modifier.fillMaxSize(),
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
                                                verticalArrangement = Arrangement.spacedBy(12.dp),
                                                modifier            = Modifier.fillMaxSize()
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
                                            modifier         = Modifier.fillMaxSize(),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Column(
                                                horizontalAlignment = Alignment.CenterHorizontally,
                                                verticalArrangement = Arrangement.spacedBy(12.dp)
                                            ) {
                                                Text(
                                                    machinesState.message,
                                                    fontFamily = Poppins,
                                                    color      = ErrorRed
                                                )
                                                Box(
                                                    modifier = Modifier
                                                        .clip(RoundedCornerShape(14.dp))
                                                        .background(Brand)
                                                        .padding(horizontal = 24.dp, vertical = 12.dp)
                                                ) {
                                                    TextButton(onClick = onRetryLoad) {
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
    }

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
}