package com.example.mylavanderiapp.features.laundry_reservation.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalLaundryService
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mylavanderiapp.core.ui.theme.*
import com.example.mylavanderiapp.features.machines.domain.entities.Machine
import com.example.mylavanderiapp.features.machines.domain.entities.MachineStatus

@Composable
fun MachineReservationCard(
    machine: Machine,
    isLoading: Boolean = false,
    onReserve: (Machine) -> Unit
) {
    val isAvailable = machine.status == MachineStatus.AVAILABLE

    val statusColor = when (machine.status) {
        MachineStatus.AVAILABLE   -> SuccessGreen
        MachineStatus.OCCUPIED    -> ErrorRed
        MachineStatus.MAINTENANCE -> Color(0xFFFF9800)
    }
    val statusText = when (machine.status) {
        MachineStatus.AVAILABLE   -> "Disponible"
        MachineStatus.OCCUPIED    -> "Ocupada"
        MachineStatus.MAINTENANCE -> "Mantenimiento"
    }

    Card(
        modifier  = Modifier.fillMaxWidth(),
        shape     = RoundedCornerShape(16.dp),
        colors    = CardDefaults.cardColors(containerColor = BgLight),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icono + info
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(
                            if (isAvailable) BrandPale
                            else statusColor.copy(alpha = 0.12f)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Filled.LocalLaundryService,
                        contentDescription = null,
                        tint     = if (isAvailable) Brand else statusColor,
                        modifier = Modifier.size(28.dp)
                    )
                }

                Column {
                    Text(
                        machine.name,
                        fontFamily = Poppins,
                        fontWeight = FontWeight.SemiBold,
                        fontSize   = 15.sp,
                        color      = TextDark
                    )
                    Text(
                        "${machine.capacity}${machine.location?.let { " • $it" } ?: ""}",
                        fontFamily = Poppins,
                        fontSize   = 12.sp,
                        color      = TextMid
                    )
                    Spacer(Modifier.height(4.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .background(statusColor, CircleShape)
                        )
                        Text(
                            statusText,
                            fontFamily = Poppins,
                            fontSize   = 11.sp,
                            color      = statusColor,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            // Botón apartar
            if (isAvailable) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            Brush.horizontalGradient(listOf(BrandDark, Brand, AccentCyan))
                        )
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .padding(horizontal = 16.dp, vertical = 10.dp)
                                .size(18.dp),
                            color       = Color.White,
                            strokeWidth = 2.dp
                        )
                    } else {
                        TextButton(
                            onClick  = { onReserve(machine) },
                            modifier = Modifier.padding(horizontal = 4.dp)
                        ) {
                            Text(
                                "Apartar",
                                fontFamily = Poppins,
                                fontWeight = FontWeight.Bold,
                                fontSize   = 13.sp,
                                color      = Color.White
                            )
                        }
                    }
                }
            } else {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(statusColor.copy(alpha = 0.10f))
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Text(
                        if (machine.status == MachineStatus.OCCUPIED) "No disponible" else "En mantenimiento",
                        fontFamily = Poppins,
                        fontWeight = FontWeight.Medium,
                        fontSize   = 11.sp,
                        color      = statusColor
                    )
                }
            }
        }
    }
}