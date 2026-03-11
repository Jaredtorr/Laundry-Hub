package com.example.mylavanderiapp.features.maintenance.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.LocalLaundryService
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modi
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mylavanderiapp.core.ui.theme.*
import com.example.mylavanderiapp.features.maintenance.domain.entities.MaintenanceRecord

@Composable
fun MaintenanceCard(
    record: MaintenanceRecord,
    onResolve: () -> Unit,
    onDelete: () -> Unit
) {
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
                        text       = record.machineName,
                        fontFamily = Poppins,
                        fontWeight = FontWeight.SemiBold,
                        fontSize   = 15.sp,
                        color      = TextDark
                    )
                    Text(
                        text       = record.description,
                        fontFamily = Poppins,
                        fontSize   = 12.sp,
                        color      = TextMid
                    )
                    Text(
                        text       = record.startDate,
                        fontFamily = Poppins,
                        fontSize   = 12.sp,
                        color      = TextMid
                    )
                    Spacer(Modifier.height(4.dp))
                    Row(
                        verticalAlignment     = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(
                            verticalAlignment     = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector        = Icons.Filled.Build,
                                contentDescription = null,
                                tint               = TextMid,
                                modifier           = Modifier.size(11.dp)
                            )
                            Text(
                                text       = "${record.daysElapsed} días",
                                fontFamily = Poppins,
                                fontSize   = 11.sp,
                                color      = TextMid
                            )
                        }
                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = if (record.isResolved) Color(0xFF4CAF50) else Color(0xFFFF9800)
                        ) {
                            Text(
                                text       = if (record.isResolved) "Resuelto" else "Activo",
                                fontFamily = Poppins,
                                fontSize   = 11.sp,
                                color      = Color.White,
                                fontWeight = FontWeight.Medium,
                                modifier   = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            )
                        }
                    }
                }
            }

            // — Acciones —
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (!record.isResolved) {
                    IconButton(onClick = onResolve) {
                        Icon(
                            imageVector        = Icons.Filled.Check,
                            contentDescription = "Resolver",
                            tint               = Brand
                        )
                    }
                }
                IconButton(onClick = onDelete) {
                    Icon(
                        imageVector        = Icons.Filled.Delete,
                        contentDescription = "Eliminar",
                        tint               = ErrorRed
                    )
                }
            }
        }
    }
}