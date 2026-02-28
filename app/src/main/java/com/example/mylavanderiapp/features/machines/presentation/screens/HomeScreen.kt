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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.mylavanderiapp.core.ui.theme.*
import com.example.mylavanderiapp.features.machines.domain.entities.Machine
import com.example.mylavanderiapp.features.machines.presentation.components.*
import com.example.mylavanderiapp.features.machines.presentation.states.MachineOperationState
import com.example.mylavanderiapp.features.machines.presentation.states.MachinesUIState
import com.example.mylavanderiapp.features.machines.presentation.viewmodels.HomeViewModel
import com.example.mylavanderiapp.features.notifications.presentation.components.NotificationsBottomSheet
import com.example.mylavanderiapp.features.notifications.presentation.viewmodels.NotificationsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    notificationsViewModel: NotificationsViewModel = hiltViewModel(),
    onLogout: () -> Unit
) {
    val uiState        by viewModel.uiState.collectAsState()
    val operationState by viewModel.operationState.collectAsState()
    val unreadCount    by notificationsViewModel.unreadCount.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    var showAddDialog     by remember { mutableStateOf(false) }
    var machineToEdit     by remember { mutableStateOf<Machine?>(null) }
    var machineToDelete   by remember { mutableStateOf<Machine?>(null) }
    var showNotifications by remember { mutableStateOf(false) }

    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { visible = true }

    // Snackbar para éxito/error
    LaunchedEffect(operationState) {
        when (operationState) {
            is MachineOperationState.Success -> {
                snackbarHostState.showSnackbar((operationState as MachineOperationState.Success).message)
                viewModel.resetOperationState()
            }
            is MachineOperationState.Error -> {
                snackbarHostState.showSnackbar(
                    message         = (operationState as MachineOperationState.Error).message,
                    duration        = SnackbarDuration.Long,
                    withDismissAction = true
                )
                viewModel.resetOperationState()
            }
            else -> {}
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
            FloatingActionButton(
                onClick        = { showAddDialog = true },
                containerColor = Brand,
                shape          = CircleShape
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Añadir máquina", tint = androidx.compose.ui.graphics.Color.White)
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(BgLight)
        ) {
            // — Header con gradiente —
            HomeHeader(
                machines             = (uiState as? MachinesUIState.Success)?.machines ?: emptyList(),
                unreadCount          = unreadCount,
                onNotificationsClick = { showNotifications = true },
                onLogout             = onLogout
            )

            // — Contenido principal animado —
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
                                onRetry  = { viewModel.loadMachines() }
                            )
                        }
                    }
                }
            }
        }
    }

    // — BottomSheet de notificaciones —
    if (showNotifications) {
        NotificationsBottomSheet(
            viewModel = notificationsViewModel,
            onDismiss = { showNotifications = false }
        )
    }

    // — Diálogos —
    if (showAddDialog) {
        MachineDialog(
            onDismiss = { showAddDialog = false },
            onConfirm = {
                viewModel.addMachine(it)
                showAddDialog = false
            }
        )
    }

    if (machineToEdit != null) {
        MachineDialog(
            machine   = machineToEdit,
            onDismiss = { machineToEdit = null },
            onConfirm = {
                viewModel.updateMachine(it)
                machineToEdit = null
            }
        )
    }

    if (machineToDelete != null) {
        DeleteMachineDialog(
            machine   = machineToDelete!!,
            onConfirm = {
                viewModel.deleteMachine(machineToDelete!!.id)
                machineToDelete = null
            },
            onDismiss = { machineToDelete = null }
        )
    }
}