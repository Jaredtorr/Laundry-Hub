package com.example.mylavanderiapp.features.maintenance.presentation.states

import com.example.mylavanderiapp.features.maintenance.domain.entities.MaintenanceRecord

sealed class MaintenanceUIState {
    object Idle    : MaintenanceUIState()
    object Loading : MaintenanceUIState()
    data class Success(val records: List<MaintenanceRecord>) : MaintenanceUIState()
    data class Error(val message: String) : MaintenanceUIState()
}