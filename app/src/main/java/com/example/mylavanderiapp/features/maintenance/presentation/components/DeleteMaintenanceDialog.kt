package com.example.mylavanderiapp.features.maintenance.presentation.components

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.example.mylavanderiapp.core.ui.theme.*
import com.example.mylavanderiapp.features.maintenance.domain.entities.MaintenanceRecord

@Composable
fun DeleteMaintenanceDialog(
    record: MaintenanceRecord,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        shape            = RoundedCornerShape(20.dp),
        title = {
            Text(
                text       = "Eliminar registro",
                fontFamily = Poppins,
                color      = TextDark
            )
        },
        text = {
            Text(
                text       = "¿Estás seguro de eliminar el registro de \"${record.machineName}\"?",
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
                Text("Eliminar", fontFamily = Poppins, color = androidx.compose.ui.graphics.Color.White)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar", fontFamily = Poppins, color = TextMid)
            }
        }
    )
}