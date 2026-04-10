package com.example.mylavanderiapp.features.maintenance.presentation.states

sealed class MaintenanceOperationState {
    data object Idle    : MaintenanceOperationState()
    data object Loading : MaintenanceOperationState()
    data class Success(val message: String) : MaintenanceOperationState()
    data class Error(val message: String)   : MaintenanceOperationState()
}