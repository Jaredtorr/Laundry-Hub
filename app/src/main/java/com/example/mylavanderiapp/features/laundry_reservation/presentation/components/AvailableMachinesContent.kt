package com.example.mylavanderiapp.features.laundry_reservation.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.mylavanderiapp.core.ui.theme.*
import com.example.mylavanderiapp.features.laundry_reservation.presentation.states.ReservationUIState
import com.example.mylavanderiapp.features.machines.domain.entities.Machine
import com.example.mylavanderiapp.features.machines.presentation.states.MachinesUIState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AvailableMachinesContent(
    machinesState : MachinesUIState,
    createState   : ReservationUIState,
    onReserve     : (Machine) -> Unit,
    onRetry       : () -> Unit
) {
    val isRefreshing = machinesState is MachinesUIState.Loading
    val pullState    = rememberPullToRefreshState()
    val machines     = (machinesState as? MachinesUIState.Success)?.machines ?: emptyList()

    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh    = onRetry,
        state        = pullState,
        modifier     = Modifier.fillMaxSize()
    ) {
        when (machinesState) {
            is MachinesUIState.Loading -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = Brand)
                }
            }
            is MachinesUIState.Success -> {
                if (machines.isEmpty()) {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("No hay lavadoras registradas", fontFamily = Poppins, color = TextMid)
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
                                onReserve = onReserve
                            )
                        }
                    }
                }
            }
            is MachinesUIState.Error -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(machinesState.message, fontFamily = Poppins, color = ErrorRed)
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(14.dp))
                                .background(Brand)
                                .padding(horizontal = 24.dp, vertical = 12.dp)
                        ) {
                            TextButton(onClick = onRetry) {
                                Text("Reintentar", color = Color.White, fontFamily = Poppins, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
            else -> {}
        }
    }
}