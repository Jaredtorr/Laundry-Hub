package com.example.mylavanderiapp.features.maintenance.presentation.states

sealed class MaintenanceOperationState {
    object Idle    : MaintenanceOperationState()
    object Loading : MaintenanceOperationState()
    data class Success(val message: String) : MaintenanceOperationState()
    data class Error(val message: String)   : MaintenanceOperationState()
}
