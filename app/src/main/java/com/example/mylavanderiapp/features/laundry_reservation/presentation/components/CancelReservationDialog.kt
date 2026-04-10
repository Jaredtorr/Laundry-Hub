package com.example.mylavanderiapp.features.laundry_reservation.presentation.components

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.example.mylavanderiapp.core.ui.theme.*
import com.example.mylavanderiapp.features.laundry_reservation.domain.entities.Reservation

@Composable
fun CancelReservationDialog(
    reservation: Reservation,
    onConfirm  : () -> Unit,
    onDismiss  : () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor   = SurfaceWhite,
        shape            = RoundedCornerShape(24.dp),
        icon = {
            Icon(Icons.Filled.Cancel, contentDescription = null, tint = ErrorRed)
        },
        title = {
            Text("¿Cancelar reservación?", fontFamily = Poppins, color = TextDark)
        },
        text = {
            Text(
                "¿Estás seguro de cancelar la reservación de ${reservation.machineName}?",
                fontFamily = Poppins,
                color      = TextMid
            )
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors  = ButtonDefaults.buttonColors(containerColor = ErrorRed),
                shape   = RoundedCornerShape(12.dp)
            ) {
                Text("Cancelar reserva", fontFamily = Poppins, color = androidx.compose.ui.graphics.Color.White)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Volver", fontFamily = Poppins, color = TextMid)
            }
        }
    )
}