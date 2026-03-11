package com.example.mylavanderiapp.features.maintenance.data.datasources.remote

import com.example.mylavanderiapp.core.network.LaundryApi
import com.example.mylavanderiapp.features.maintenance.data.datasource.remote.model.CreateMaintenanceRequest
import com.example.mylavanderiapp.features.maintenance.data.datasources.remote.mapper.toDomain
import com.example.mylavanderiapp.features.maintenance.domain.entities.MaintenanceRecord
import javax.inject.Inject

class MaintenanceRemoteDataSource @Inject constructor(
    private val api: LaundryApi
) {
    suspend fun getAll(): Result<List<MaintenanceRecord>> = runCatching {
        api.getAllMaintenance().records.map { it.toDomain() }
    }

    suspend fun create(machineId: Int, description: String): Result<MaintenanceRecord> = runCatching {
        api.createMaintenance(CreateMaintenanceRequest(machineId, description)).record.toDomain()
    }

    suspend fun resolve(id: Int): Result<Unit> = runCatching {
        api.resolveMaintenance(id)
        Unit
    }

    suspend fun delete(id: Int): Result<Unit> = runCatching {
        api.deleteMaintenance(id)
        Unit
    }
}