package com.example.mylavanderiapp.features.maintenance.presentation.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mylavanderiapp.core.ui.theme.*
import com.example.mylavanderiapp.features.machines.presentation.states.MachinesUIState
import com.example.mylavanderiapp.features.maintenance.domain.entities.MaintenanceRecord
import com.example.mylavanderiapp.features.maintenance.presentation.components.*
import com.example.mylavanderiapp.features.maintenance.presentation.states.MaintenanceUIState
import com.example.mylavanderiapp.features.notifications.presentation.components.NotificationsBottomSheet
import com.example.mylavanderiapp.features.notifications.presentation.states.NotificationsUIState
import com.example.mylavanderiapp.features.shared.presentation.components.AdminBottomNavBar
import com.example.mylavanderiapp.features.shared.presentation.components.AdminNavDestination

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MaintenanceScreen(
    uiState                      : MaintenanceUIState,
    machinesState                : MachinesUIState,
    activeCount                  : Int,
    activeRecords                : List<MaintenanceRecord>,
    resolvedRecords              : List<MaintenanceRecord>,
    unreadCount                  : Int,
    notificationsUiState         : NotificationsUIState,
    hasUnread                    : Boolean,
    snackbarHostState            : SnackbarHostState,
    onAddRecord                  : (MaintenanceRecord) -> Unit,
    onResolveRecord              : (Int) -> Unit,
    onDeleteRecord               : (Int) -> Unit,
    onRetryLoad                  : () -> Unit,
    onMarkNotificationAsRead     : (Int) -> Unit,
    onMarkAllNotificationsAsRead : () -> Unit,
    onRetryNotifications         : () -> Unit,
    onLogout                     : () -> Unit,
    onNavigateToHome             : () -> Unit
) {
    var showAddDialog     by remember { mutableStateOf(false) }
    var recordToDelete    by remember { mutableStateOf<MaintenanceRecord?>(null) }
    var selectedTab       by remember { mutableIntStateOf(0) }
    var visible           by remember { mutableStateOf(true) }
    var showNotifications by remember { mutableStateOf(false) }

    val machines = (machinesState as? MachinesUIState.Success)?.machines ?: emptyList()

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            AdminBottomNavBar(
                current    = AdminNavDestination.MAINTENANCE,
                onNavigate = { onNavigateToHome() }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick        = { showAddDialog = true },
                containerColor = Brand,
                shape          = CircleShape
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Agregar", tint = Color.White)
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(BgLight)
        ) {
            MaintenanceHeader(
                activeCount          = activeCount,
                unreadCount          = unreadCount,
                onNotificationsClick = { showNotifications = true },
                onLogout             = onLogout
            )

            AnimatedVisibility(
                visible  = visible,
                enter    = fadeIn(tween(550)) + slideInVertically(
                    initialOffsetY = { 140 },
                    animationSpec  = tween(550, easing = EaseOutCubic)
                ),
                modifier = Modifier.align(Alignment.TopCenter)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 200.dp)
                ) {
                    Card(
                        modifier  = Modifier.fillMaxSize(),
                        shape     = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
                        colors    = CardDefaults.cardColors(containerColor = SurfaceWhite),
                        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 20.dp, vertical = 24.dp)
                        ) {
                            Text(
                                text       = "Registros de mantenimiento",
                                fontFamily = Poppins,
                                fontWeight = FontWeight.Bold,
                                fontSize   = 16.sp,
                                color      = TextDark,
                                modifier   = Modifier.padding(bottom = 12.dp)
                            )

                            TabRow(
                                selectedTabIndex = selectedTab,
                                containerColor   = SurfaceWhite,
                                contentColor     = Brand
                            ) {
                                Tab(
                                    selected = selectedTab == 0,
                                    onClick  = { selectedTab = 0 },
                                    text     = { Text("Activos", fontFamily = Poppins) }
                                )
                                Tab(
                                    selected = selectedTab == 1,
                                    onClick  = { selectedTab = 1 },
                                    text     = { Text("Resueltos", fontFamily = Poppins) }
                                )
                            }

                            Spacer(Modifier.height(16.dp))

                            MaintenanceListContent(
                                uiState         = uiState,
                                selectedTab     = selectedTab,
                                activeRecords   = activeRecords,
                                resolvedRecords = resolvedRecords,
                                onResolve       = { onResolveRecord(it.id) },
                                onDelete        = { recordToDelete = it },
                                onRetry         = onRetryLoad
                            )
                        }
                    }
                }
            }
        }
    }

    if (showNotifications) {
        NotificationsBottomSheet(
            uiState         = notificationsUiState,
            hasUnread       = hasUnread,
            onMarkAsRead    = onMarkNotificationAsRead,
            onMarkAllAsRead = onMarkAllNotificationsAsRead,
            onRetry         = onRetryNotifications,
            onDismiss       = { showNotifications = false }
        )
    }

    if (showAddDialog) {
        MaintenanceDialog(
            machines  = machines,
            onDismiss = { showAddDialog = false },
            onConfirm = {
                onAddRecord(it)
                showAddDialog = false
            }
        )
    }

    recordToDelete?.let { record ->
        DeleteMaintenanceDialog(
            record    = record,
            onConfirm = {
                onDeleteRecord(record.id)
                recordToDelete = null
            },
            onDismiss = { recordToDelete = null }
        )
    }
}