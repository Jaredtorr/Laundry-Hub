package com.example.mylavanderiapp.features.maintenance.presentation.screens

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.*
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.mylavanderiapp.features.maintenance.presentation.states.MaintenanceOperationState
import com.example.mylavanderiapp.features.maintenance.presentation.viewmodels.MaintenanceViewModel
import com.example.mylavanderiapp.features.notifications.presentation.viewmodels.NotificationsViewModel

@Composable
fun MaintenanceRoute(
    viewModel              : MaintenanceViewModel = hiltViewModel(),
    notificationsViewModel : NotificationsViewModel = hiltViewModel(),
    onLogout               : () -> Unit,
    onNavigateToHome       : () -> Unit = {}
) {
    val uiState              by viewModel.uiState.collectAsState()
    val operationState       by viewModel.operationState.collectAsState()
    val machinesState        by viewModel.machinesState.collectAsState()
    val activeCount          by viewModel.activeCount.collectAsState()
    val activeRecords        by viewModel.activeRecords.collectAsState()
    val resolvedRecords      by viewModel.resolvedRecords.collectAsState()
    val unreadCount          by notificationsViewModel.unreadCount.collectAsState()
    val notificationsUiState by notificationsViewModel.uiState.collectAsState()
    val hasUnread            by notificationsViewModel.hasUnread.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(operationState) {
        val state = operationState
        when (state) {
            is MaintenanceOperationState.Success -> {
                snackbarHostState.showSnackbar(state.message)
                viewModel.resetOperationState()
            }
            is MaintenanceOperationState.Error -> {
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

    MaintenanceScreen(
        uiState                      = uiState,
        machinesState                = machinesState,
        activeCount                  = activeCount,
        activeRecords                = activeRecords,
        resolvedRecords              = resolvedRecords,
        unreadCount                  = unreadCount,
        notificationsUiState         = notificationsUiState,
        hasUnread                    = hasUnread,
        snackbarHostState            = snackbarHostState,
        onAddRecord                  = viewModel::addRecord,
        onResolveRecord              = { viewModel.resolveRecord(it) },
        onDeleteRecord               = { viewModel.deleteRecord(it) },
        onRetryLoad                  = viewModel::loadRecords,
        onMarkNotificationAsRead     = notificationsViewModel::markAsRead,
        onMarkAllNotificationsAsRead = notificationsViewModel::markAllAsRead,
        onRetryNotifications         = notificationsViewModel::loadNotifications,
        onLogout                     = onLogout,
        onNavigateToHome             = onNavigateToHome
    )
}