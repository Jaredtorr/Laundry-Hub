package com.example.mylavanderiapp.features.maintenance.data.datasource.remote.model

data class CreateMaintenanceRequest(
    val machineId: Int,
    val description: String
)