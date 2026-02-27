package com.example.mylavanderiapp.features.notifications.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mylavanderiapp.core.ui.theme.*
import com.example.mylavanderiapp.features.notifications.domain.entities.AppNotification
import com.example.mylavanderiapp.features.notifications.domain.entities.NotificationType
import com.example.mylavanderiapp.features.notifications.presentation.states.NotificationsUIState
import com.example.mylavanderiapp.features.notifications.presentation.viewmodels.NotificationsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationsBottomSheet(
    viewModel: NotificationsViewModel,
    onDismiss: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor   = SurfaceWhite,
        shape            = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(bottom = 32.dp)
        ) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment     = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        "Notificaciones",
                        fontFamily = Poppins,
                        fontWeight = FontWeight.Bold,
                        fontSize   = 20.sp,
                        color      = TextDark
                    )
                    Text(
                        "Actividad reciente",
                        fontFamily = Poppins,
                        fontSize   = 12.sp,
                        color      = TextMid
                    )
                }

                val hasUnread = (uiState as? NotificationsUIState.Success)
                    ?.notifications?.any { !it.isRead } == true

                if (hasUnread) {
                    TextButton(onClick = { viewModel.markAllAsRead() }) {
                        Text(
                            "Marcar todas",
                            fontFamily = Poppins,
                            fontSize   = 12.sp,
                            color      = Brand,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            HorizontalDivider(color = BrandPale)
            Spacer(Modifier.height(12.dp))

            when (uiState) {
                is NotificationsUIState.Loading -> {
                    Box(
                        modifier         = Modifier.fillMaxWidth().height(200.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = Brand)
                    }
                }

                is NotificationsUIState.Success -> {
                    val notifications = (uiState as NotificationsUIState.Success).notifications
                    if (notifications.isEmpty()) {
                        Box(
                            modifier         = Modifier.fillMaxWidth().height(200.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    Icons.Outlined.NotificationsNone,
                                    contentDescription = null,
                                    tint     = BrandLight,
                                    modifier = Modifier.size(48.dp)
                                )
                                Text(
                                    "Sin notificaciones",
                                    fontFamily = Poppins,
                                    color      = TextMid,
                                    fontSize   = 14.sp
                                )
                            }
                        }
                    } else {
                        LazyColumn(
                            modifier            = Modifier.heightIn(max = 480.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(notifications, key = { it.id }) { notification ->
                                NotificationItem(
                                    notification = notification,
                                    onClick      = { viewModel.markAsRead(notification.id) }
                                )
                            }
                        }
                    }
                }

                is NotificationsUIState.Error -> {
                    Box(
                        modifier         = Modifier.fillMaxWidth().height(200.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text(
                                (uiState as NotificationsUIState.Error).message,
                                fontFamily = Poppins,
                                color      = ErrorRed,
                                fontSize   = 13.sp
                            )
                            TextButton(onClick = { viewModel.loadNotifications() }) {
                                Text(
                                    "Reintentar",
                                    fontFamily = Poppins,
                                    color      = Brand,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }
                }

                else -> {}
            }
        }
    }
}

@Composable
private fun NotificationItem(
    notification: AppNotification,
    onClick: () -> Unit
) {
    val (iconVector, iconBg, iconTint) = when (notification.type) {
        NotificationType.RESERVATION -> Triple(
            Icons.Filled.LocalLaundryService as ImageVector,
            BrandPale,
            Brand
        )
        NotificationType.AVAILABLE -> Triple(
            Icons.Filled.CheckCircle as ImageVector,
            Color(0xFFDCF8E8),
            Color(0xFF4CAF50)
        )
        NotificationType.OTHER -> Triple(
            Icons.Filled.Notifications as ImageVector,
            Color(0xFFFFF3E0),
            Color(0xFFFF9800)
        )
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(if (notification.isRead) SurfaceWhite else BgLight)
            .clickable(enabled = !notification.isRead) { onClick() }
            .padding(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment     = Alignment.CenterVertically
    ) {
        // Ícono
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(iconBg),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector        = iconVector,
                contentDescription = null,
                tint               = iconTint,
                modifier           = Modifier.size(22.dp)
            )
        }

        Column(modifier = Modifier.weight(1f)) {
            Text(
                notification.message,
                fontFamily = Poppins,
                fontSize   = 13.sp,
                fontWeight = if (notification.isRead) FontWeight.Normal else FontWeight.SemiBold,
                color      = if (notification.isRead) TextMid else TextDark
            )
            Text(
                notification.createdAt,
                fontFamily = Poppins,
                fontSize   = 11.sp,
                color      = TextMid.copy(alpha = 0.7f)
            )
        }

        // Punto azul si no leída
        if (!notification.isRead) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .background(Brand, CircleShape)
            )
        }
    }
}