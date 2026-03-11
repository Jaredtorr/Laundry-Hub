package com.example.mylavanderiapp.features.maintenance.data.datasources.remote.mapper

import com.example.mylavanderiapp.features.maintenance.data.datasources.remote.model.dto.MaintenanceRecordDto
import com.example.mylavanderiapp.features.maintenance.domain.entities.MaintenanceRecord

fun MaintenanceRecordDto.toDomain(): MaintenanceRecord = MaintenanceRecord(
    id          = id,
    machineId   = machineId,
    machineName = machineName,
    description = description,
    startDate   = startDate,
    daysElapsed = daysElapsed,
    isResolved  = isResolved
)