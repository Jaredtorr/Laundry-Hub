package com.example.mylavanderiapp.features.laundry_reservation.presentation.components

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.mylavanderiapp.core.ui.theme.*
import com.example.mylavanderiapp.features.laundry_reservation.domain.entities.Reservation

@Composable
fun CompleteReservationDialog(
    reservation: Reservation,
    onConfirm  : () -> Unit,
    onDismiss  : () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor   = SurfaceWhite,
        shape            = RoundedCornerShape(24.dp),
        icon = {
            Icon(Icons.Filled.CheckCircle, contentDescription = null, tint = Color(0xFF4CAF50))
        },
        title = {
            Text("¿Ya terminaste?", fontFamily = Poppins, color = TextDark)
        },
        text = {
            Text(
                "Confirma que ya dejaste libre ${reservation.machineName}. Su estado volverá a disponible.",
                fontFamily = Poppins,
                color      = TextMid
            )
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors  = ButtonDefaults.buttonColors(containerColor = Brand),
                shape   = RoundedCornerShape(12.dp)
            ) {
                Text("Sí, terminé", fontFamily = Poppins, color = Color.White)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar", fontFamily = Poppins, color = TextMid)
            }
        }
    )
}