package com.example.mylavanderiapp.features.maintenance.data.repositories

import com.example.mylavanderiapp.core.database.dao.MaintenanceDao
import com.example.mylavanderiapp.core.database.entities.toEntity
import com.example.mylavanderiapp.features.maintenance.domain.entities.MaintenanceRecord
import com.example.mylavanderiapp.features.maintenance.domain.repositories.MaintenanceRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class MaintenanceRepositoryImpl @Inject constructor(
    private val dao: MaintenanceDao
) : MaintenanceRepository {

    override suspend fun getAll(): Result<List<MaintenanceRecord>> = runCatching {
        dao.getAll().first().map { it.toDomain() }
    }

    override suspend fun add(record: MaintenanceRecord): Result<Unit> = runCatching {
        dao.insert(record.toEntity())
    }

    override suspend fun resolve(id: Int): Result<Unit> = runCatching {
        dao.resolve(id)
    }

    override suspend fun delete(id: Int): Result<Unit> = runCatching {
        dao.delete(id)
    }
}