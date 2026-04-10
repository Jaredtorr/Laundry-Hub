package com.example.mylavanderiapp.features.machines.presentation.screens

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
import com.example.mylavanderiapp.features.machines.domain.entities.Machine
import com.example.mylavanderiapp.features.machines.presentation.components.*
import com.example.mylavanderiapp.features.machines.presentation.states.MachinesUIState
import com.example.mylavanderiapp.features.notifications.presentation.components.NotificationsBottomSheet
import com.example.mylavanderiapp.features.notifications.presentation.states.NotificationsUIState
import com.example.mylavanderiapp.features.shared.presentation.components.AdminBottomNavBar
import com.example.mylavanderiapp.features.shared.presentation.components.AdminNavDestination

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    uiState                      : MachinesUIState,
    availableCount               : Int,
    occupiedCount                : Int,
    maintenanceCount             : Int,
    unreadCount                  : Int,
    notificationsUiState         : NotificationsUIState,
    hasUnread                    : Boolean,
    snackbarHostState            : SnackbarHostState,
    onAddMachine                 : (Machine) -> Unit,
    onUpdateMachine              : (Machine) -> Unit,
    onDeleteMachine              : (Int) -> Unit,
    onRetryLoad                  : () -> Unit,
    onMarkNotificationAsRead     : (Int) -> Unit,
    onMarkAllNotificationsAsRead : () -> Unit,
    onRetryNotifications         : () -> Unit,
    onLogout                     : () -> Unit,
    onNavigateToMaintenance      : () -> Unit
) {
    var showAddDialog     by remember { mutableStateOf(false) }
    var machineToEdit     by remember { mutableStateOf<Machine?>(null) }
    var machineToDelete   by remember { mutableStateOf<Machine?>(null) }
    var showNotifications by remember { mutableStateOf(false) }
    var visible           by remember { mutableStateOf(true) }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            AdminBottomNavBar(
                current    = AdminNavDestination.HOME,
                onNavigate = { onNavigateToMaintenance() }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick        = { showAddDialog = true },
                containerColor = Brand,
                shape          = CircleShape
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Añadir máquina", tint = Color.White)
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(BgLight)
        ) {
            HomeHeader(
                availableCount       = availableCount,
                occupiedCount        = occupiedCount,
                maintenanceCount     = maintenanceCount,
                unreadCount          = unreadCount,
                onNotificationsClick = { showNotifications = true },
                onLogout             = onLogout,
                onMaintenanceClick   = onNavigateToMaintenance
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
                                text       = "Máquinas registradas",
                                fontFamily = Poppins,
                                fontWeight = FontWeight.Bold,
                                fontSize   = 16.sp,
                                color      = TextDark,
                                modifier   = Modifier.padding(bottom = 16.dp)
                            )

                            MachineListContent(
                                uiState  = uiState,
                                onEdit   = { machineToEdit = it },
                                onDelete = { machineToDelete = it },
                                onRetry  = onRetryLoad
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
        MachineDialog(
            onDismiss = { showAddDialog = false },
            onConfirm = {
                onAddMachine(it)
                showAddDialog = false
            }
        )
    }

    machineToEdit?.let { machine ->
        MachineDialog(
            machine   = machine,
            onDismiss = { machineToEdit = null },
            onConfirm = {
                onUpdateMachine(it)
                machineToEdit = null
            }
        )
    }

    machineToDelete?.let { machine ->
        DeleteMachineDialog(
            machine   = machine,
            onConfirm = {
                onDeleteMachine(machine.id)
                machineToDelete = null
            },
            onDismiss = { machineToDelete = null }
        )
    }
}