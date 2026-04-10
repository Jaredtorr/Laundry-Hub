package com.example.mylavanderiapp.features.laundry_reservation.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ListAlt
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mylavanderiapp.core.ui.theme.*
import com.example.mylavanderiapp.features.laundry_reservation.domain.entities.Reservation
import com.example.mylavanderiapp.features.laundry_reservation.presentation.states.MyReservationsUIState

@Composable
fun UserReservationsContent(
    myReservationsState: MyReservationsUIState,
    onCancel           : (Reservation) -> Unit,
    onComplete         : (Reservation) -> Unit,
    onReportFault      : (Reservation) -> Unit,
    onRetry            : () -> Unit
) {
    when (myReservationsState) {
        is MyReservationsUIState.Loading -> {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Brand)
            }
        }
        is MyReservationsUIState.Success -> {
            if (myReservationsState.reservations.isEmpty()) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            Icons.Outlined.ListAlt,
                            contentDescription = null,
                            tint     = BrandLight,
                            modifier = Modifier.size(48.dp)
                        )
                        Text("Sin reservaciones activas", fontFamily = Poppins, color = TextMid)
                    }
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier            = Modifier.fillMaxSize()
                ) {
                    items(myReservationsState.reservations, key = { it.id }) { reservation ->
                        ReservationCard(
                            reservation   = reservation,
                            onCancel      = onCancel,
                            onComplete    = onComplete,
                            onReportFault = onReportFault
                        )
                    }
                }
            }
        }
        is MyReservationsUIState.Error -> {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(myReservationsState.message, fontFamily = Poppins, color = ErrorRed)
                    TextButton(onClick = onRetry) {
                        Text("Reintentar", fontFamily = Poppins, color = Brand)
                    }
                }
            }
        }
        else -> {}
    }
}