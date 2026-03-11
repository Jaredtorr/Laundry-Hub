package com.example.mylavanderiapp.core.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.mylavanderiapp.features.maintenance.domain.entities.MaintenanceRecord

@Entity(tableName = "maintenance_records")
data class MaintenanceRecordEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val machineId: Int,
    val machineName: String,
    val description: String,
    val startDate: String,
    val daysElapsed: Int,
    val isResolved: Boolean = false
) {
    fun toDomain() = MaintenanceRecord(
        id          = id,
        machineId   = machineId,
        machineName = machineName,
        description = description,
        startDate   = startDate,
        daysElapsed = daysElapsed,
        isResolved  = isResolved
    )
}

fun MaintenanceRecord.toEntity() = MaintenanceRecordEntity(
    id          = id,
    machineId   = machineId,
    machineName = machineName,
    description = description,
    startDate   = startDate,
    daysElapsed = daysElapsed,
    isResolved  = isResolved
)