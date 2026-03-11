package com.example.mylavanderiapp.features.maintenance.domain.entities

data class MaintenanceRecord(
    val id: Int,
    val machineId: Int,
    val machineName: String,
    val description: String,
    val startDate: String,
    val daysElapsed: Int,
    val isResolved: Boolean = false
)