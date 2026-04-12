package com.example.mylavanderiapp.features.machines.presentation.screens

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import com.example.mylavanderiapp.core.service.WebSocketForegroundService
import com.example.mylavanderiapp.features.machines.presentation.states.MachineOperationState
import com.example.mylavanderiapp.features.machines.presentation.viewmodels.HomeViewModel
import com.example.mylavanderiapp.features.notifications.presentation.viewmodels.NotificationsViewModel

@Composable
fun HomeRoute(
    viewModel              : HomeViewModel = hiltViewModel(),
    notificationsViewModel : NotificationsViewModel = hiltViewModel(),
    onLogout               : () -> Unit,
    onNavigateToMaintenance: () -> Unit = {}
) {
    val context = LocalContext.current
    val uiState              by viewModel.uiState.collectAsState()
    val operationState       by viewModel.operationState.collectAsState()
    val availableCount       by viewModel.availableCount.collectAsState()
    val occupiedCount        by viewModel.occupiedCount.collectAsState()
    val maintenanceCount     by viewModel.maintenanceCount.collectAsState()
    val unreadCount          by notificationsViewModel.unreadCount.collectAsState()
    val notificationsUiState by notificationsViewModel.uiState.collectAsState()
    val hasUnread            by notificationsViewModel.hasUnread.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    val lifecycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(lifecycleOwner) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
            viewModel.loadMachines()
        }
    }

    LaunchedEffect(operationState) {
        val state = operationState
        when (state) {
            is MachineOperationState.Success -> {
                snackbarHostState.showSnackbar(state.message)
                viewModel.resetOperationState()
            }
            is MachineOperationState.Error -> {
                snackbarHostState.showSnackbar(
                    message           = state.message,
                    duration          = SnackbarDuration.Long,
                    withDismissAction = true
                )
                viewModel.resetOperationState()
            }
            else -> {}
        }
    }

    HomeScreen(
        uiState                      = uiState,
        availableCount               = availableCount,
        occupiedCount                = occupiedCount,
        maintenanceCount             = maintenanceCount,
        unreadCount                  = unreadCount,
        notificationsUiState         = notificationsUiState,
        hasUnread                    = hasUnread,
        snackbarHostState            = snackbarHostState,
        onAddMachine                 = viewModel::addMachine,
        onUpdateMachine              = viewModel::updateMachine,
        onDeleteMachine              = viewModel::deleteMachine,
        onRetryLoad                  = viewModel::loadMachines,
        onMarkNotificationAsRead     = notificationsViewModel::markAsRead,
        onMarkAllNotificationsAsRead = notificationsViewModel::markAllAsRead,
        onRetryNotifications         = notificationsViewModel::loadNotifications,
        onLogout = {
            context.startService(
                WebSocketForegroundService.stopIntent(context)
            )
            onLogout()
        },
        onNavigateToMaintenance      = onNavigateToMaintenance
    )
}