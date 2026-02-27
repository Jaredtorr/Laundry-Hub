package com.example.mylavanderiapp.features.machines.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LocalLaundryService
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mylavanderiapp.core.ui.theme.*
import com.example.mylavanderiapp.features.machines.domain.entities.Machine
import com.example.mylavanderiapp.features.machines.domain.entities.MachineStatus

@Composable
fun MachineCard(
    machine: Machine,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    val statusColor = when (machine.status) {
        MachineStatus.AVAILABLE   -> Color(0xFF4CAF50)
        MachineStatus.OCCUPIED    -> Color(0xFFF44336)
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
            verticalAlignment     = Alignment.CenterVertically
        ) {
            // — Info de la máquina —
            Row(
                verticalAlignment     = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier              = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(BrandPale),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector        = Icons.Filled.LocalLaundryService,
                        contentDescription = null,
                        tint               = Brand,
                        modifier           = Modifier.size(26.dp)
                    )
                }

                Column {
                    Text(
                        text       = machine.name,
                        fontFamily = Poppins,
                        fontWeight = FontWeight.SemiBold,
                        fontSize   = 15.sp,
                        color      = TextDark
                    )
                    Text(
                        text       = "${machine.capacity}${machine.location?.let { " • $it" } ?: ""}",
                        fontFamily = Poppins,
                        fontSize   = 12.sp,
                        color      = TextMid
                    )
                    Spacer(Modifier.height(4.dp))
                    Row(
                        verticalAlignment     = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .background(statusColor, CircleShape)
                        )
                        Text(
                            text       = statusText,
                            fontFamily = Poppins,
                            fontSize   = 11.sp,
                            color      = statusColor,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            // — Acciones —
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                IconButton(onClick = onEdit) {
                    Icon(Icons.Filled.Edit, contentDescription = "Editar", tint = Brand)
                }
                IconButton(onClick = onDelete) {
                    Icon(Icons.Filled.Delete, contentDescription = "Eliminar", tint = ErrorRed)
                }
            }
        }
    }
}