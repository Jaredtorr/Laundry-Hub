package com.example.mylavanderiapp.features.maintenance.data.repositories

import com.example.mylavanderiapp.core.database.dao.MaintenanceDao
import com.example.mylavanderiapp.core.database.entities.toEntity
import com.example.mylavanderiapp.features.maintenance.data.datasources.remote.MaintenanceRemoteDataSource
import com.example.mylavanderiapp.features.maintenance.domain.entities.MaintenanceRecord
import com.example.mylavanderiapp.features.maintenance.domain.repositories.MaintenanceRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class MaintenanceRepositoryImpl @Inject constructor(
    private val remote: MaintenanceRemoteDataSource,
    private val dao:    MaintenanceDao
) : MaintenanceRepository {

    override suspend fun getAll(): Result<List<MaintenanceRecord>> {
        val result = remote.getAll()

        return if (result.isSuccess) {
            val records = result.getOrDefault(emptyList())
            // Estrategia Cache-Aside: sincroniza Room con datos frescos
            dao.clearAll()
            records.forEach { dao.insert(it.toEntity()) }
            result
        } else {
            // Fallback offline: lee lo que hay en Room
            try {
                val cached = dao.getAll()
                    .first()                    // toma el primer emit del Flow
                    .map { it.toDomain() }
                if (cached.isNotEmpty()) {
                    Result.success(cached)
                } else {
                    result                      // si Room también está vacío, propaga el error original
                }
            } catch (e: Exception) {
                result                          // si Room falla, propaga el error original
            }
        }
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