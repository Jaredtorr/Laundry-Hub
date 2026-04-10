package com.example.mylavanderiapp.features.laundry_reservation.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mylavanderiapp.core.ui.theme.*
import com.example.mylavanderiapp.features.laundry_reservation.domain.entities.Reservation
import com.example.mylavanderiapp.features.maintenance.domain.entities.MaintenanceRecord
import java.util.Calendar

@Composable
fun ReportFaultDialog(
    reservation: Reservation,
    onConfirm  : (MaintenanceRecord) -> Unit,
    onDismiss  : () -> Unit
) {
    var description by remember { mutableStateOf("") }

    val calendar = Calendar.getInstance()
    val today = "${calendar.get(Calendar.DAY_OF_MONTH)} ${
        arrayOf("Ene","Feb","Mar","Abr","May","Jun","Jul","Ago","Sep","Oct","Nov","Dic")[calendar.get(Calendar.MONTH)]
    } ${calendar.get(Calendar.YEAR)}"

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor   = SurfaceWhite,
        shape            = RoundedCornerShape(24.dp),
        icon = {
            Icon(Icons.Filled.Build, contentDescription = null, tint = Color(0xFFFF9800))
        },
        title = {
            Text("Reportar falla", fontFamily = Poppins, color = TextDark)
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(
                    "Máquina: ${reservation.machineName}",
                    fontFamily = Poppins,
                    fontSize   = 13.sp,
                    color      = TextMid
                )
                OutlinedTextField(
                    value         = description,
                    onValueChange = { description = it },
                    label         = { Text("Describe la falla", fontFamily = Poppins) },
                    placeholder   = { Text("Ej: No enciende, hace ruido raro...", fontFamily = Poppins) },
                    minLines      = 3,
                    modifier      = Modifier.fillMaxWidth(),
                    shape         = RoundedCornerShape(12.dp),
                    colors        = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor   = Brand,
                        unfocusedBorderColor = BrandPale,
                        focusedLabelColor    = Brand
                    )
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (description.isNotBlank()) {
                        onConfirm(
                            MaintenanceRecord(
                                id          = 0,
                                machineId   = reservation.machineId,
                                machineName = reservation.machineName,
                                description = description,
                                startDate   = today,
                                daysElapsed = 0,
                                isResolved  = false
                            )
                        )
                    }
                },
                enabled = description.isNotBlank(),
                colors  = ButtonDefaults.buttonColors(containerColor = Brand),
                shape   = RoundedCornerShape(12.dp)
            ) {
                Text("Reportar", fontFamily = Poppins, color = androidx.compose.ui.graphics.Color.White)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar", fontFamily = Poppins, color = TextMid)
            }
        }
    )
}