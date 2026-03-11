package com.example.mylavanderiapp.features.maintenance.domain.repositories

import com.example.mylavanderiapp.features.maintenance.domain.entities.MaintenanceRecord

interface MaintenanceRepository {
    suspend fun getAll(): Result<List<MaintenanceRecord>>
    suspend fun add(record: MaintenanceRecord): Result<Unit>
    suspend fun resolve(id: Int): Result<Unit>
    suspend fun delete(id: Int): Result<Unit>
}