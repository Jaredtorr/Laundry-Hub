package com.example.mylavanderiapp.features.maintenance.data.datasources.remote.model.dto

data class MaintenanceRecordDto(
    val id: Int,
    val machineId: Int,
    val machineName: String,
    val description: String,
    val isResolved: Boolean,
    val resolvedAt: String?,
    val startDate: String,
    val daysElapsed: Int
)