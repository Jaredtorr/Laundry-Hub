package com.example.mylavanderiapp.features.maintenance.data.repositories

import com.example.mylavanderiapp.core.database.dao.MaintenanceDao
import com.example.mylavanderiapp.core.database.entities.toEntity
import com.example.mylavanderiapp.features.maintenance.data.datasources.remote.MaintenanceRemoteDataSource
import com.example.mylavanderiapp.features.maintenance.domain.entities.MaintenanceRecord
import com.example.mylavanderiapp.features.maintenance.domain.repositories.MaintenanceRepository
import javax.inject.Inject

class MaintenanceRepositoryImpl @Inject constructor(
    private val remote: MaintenanceRemoteDataSource,
    private val dao:    MaintenanceDao
) : MaintenanceRepository {

    override suspend fun getAll(): Result<List<MaintenanceRecord>> {
        val result = remote.getAll()
        if (result.isSuccess) {
            val records = result.getOrDefault(emptyList())
            // sincroniza Room como caché
            dao.clearAll()
            records.forEach { dao.insert(it.toEntity()) }
        }
        return result
    }

    override suspend fun add(record: MaintenanceRecord): Result<Unit> {
        return remote.create(record.machineId, record.description).map { Unit }
    }

    override suspend fun resolve(id: Int): Result<Unit> {
        return remote.resolve(id)
    }

    override suspend fun delete(id: Int): Result<Unit> {
        return remote.delete(id)
    }
}