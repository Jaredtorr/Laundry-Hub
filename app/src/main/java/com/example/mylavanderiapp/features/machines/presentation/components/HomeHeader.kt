package com.example.mylavanderiapp.features.machines.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
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
fun HomeHeader(
    machines: List<Machine>,
    unreadCount: Int,
    onNotificationsClick: () -> Unit,
    onLogout: () -> Unit
) {
    val available   = machines.count { it.status == MachineStatus.AVAILABLE }
    val occupied    = machines.count { it.status == MachineStatus.OCCUPIED }
    val maintenance = machines.count { it.status == MachineStatus.MAINTENANCE }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp)
            .background(
                Brush.linearGradient(
                    colors = listOf(BrandDark, Brand, AccentCyan),
                    start  = androidx.compose.ui.geometry.Offset(0f, 0f),
                    end    = androidx.compose.ui.geometry.Offset(1400f, 800f)
                )
            )
    ) {
        // — Burbujas decorativas —
        Box(
            Modifier
                .size(180.dp)
                .align(Alignment.TopEnd)
                .offset(x = 60.dp, y = (-50).dp)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.07f))
        )
        Box(
            Modifier
                .size(100.dp)
                .align(Alignment.BottomStart)
                .offset(x = (-30).dp, y = 30.dp)
                .clip(CircleShape)
                .background(AccentCyan.copy(alpha = 0.15f))
        )

        // — Contenido —
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 52.dp, start = 24.dp, end = 24.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment     = Alignment.CenterVertically
            ) {
                // Título
                Column {
                    Text(
                        text       = "Lavandería 🧺",
                        fontFamily = Poppins,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize   = 22.sp,
                        color      = Color.White
                    )
                    Text(
                        text       = "Panel de administración",
                        fontFamily = Poppins,
                        fontWeight = FontWeight.Normal,
                        fontSize   = 13.sp,
                        color      = Color.White.copy(alpha = 0.80f)
                    )
                }

                // Botones de acción
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    NotificationButton(
                        unreadCount = unreadCount,
                        onClick     = onNotificationsClick
                    )
                    HeaderIconButton(
                        icon               = Icons.Filled.Logout,
                        contentDescription = "Salir",
                        onClick            = onLogout
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            // Chips de estadísticas
            Row(
                modifier              = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                MachineStatChip("$available Disponibles", Color(0xFF4CAF50), Modifier.weight(1f))
                MachineStatChip("$occupied Ocupadas",     Color(0xFFF44336), Modifier.weight(1f))
                MachineStatChip("$maintenance Mant.",     Color(0xFFFF9800), Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun NotificationButton(
    unreadCount: Int,
    onClick: () -> Unit
) {
    Box(
        modifier         = Modifier
            .size(40.dp)
            .clip(CircleShape)
            .background(Color.White.copy(alpha = 0.15f))
            .border(1.dp, Color.White.copy(alpha = 0.25f), CircleShape),
        contentAlignment = Alignment.Center
    ) {
        IconButton(onClick = onClick) {
            Icon(
                imageVector        = Icons.Filled.Notifications,
                contentDescription = "Notificaciones",
                tint               = Color.White,
                modifier           = Modifier.size(20.dp)
            )
        }
        if (unreadCount > 0) {
            Box(
                modifier = Modifier
                    .size(16.dp)
                    .align(Alignment.TopEnd)
                    .offset(x = (-2).dp, y = 2.dp)
                    .background(Color(0xFFF44336), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text       = if (unreadCount > 9) "9+" else "$unreadCount",
                    fontSize   = 8.sp,
                    fontWeight = FontWeight.Bold,
                    color      = Color.White
                )
            }
        }
    }
}

@Composable
private fun HeaderIconButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    contentDescription: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(40.dp)
            .clip(CircleShape)
            .background(Color.White.copy(alpha = 0.15f))
            .border(1.dp, Color.White.copy(alpha = 0.25f), CircleShape),
        contentAlignment = Alignment.Center
    ) {
        IconButton(onClick = onClick) {
            Icon(
                imageVector        = icon,
                contentDescription = contentDescription,
                tint               = Color.White,
                modifier           = Modifier.size(20.dp)
            )
        }
    }
}