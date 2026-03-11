package com.example.mylavanderiapp.features.maintenance.domain.usecases

import com.example.mylavanderiapp.features.maintenance.domain.entities.MaintenanceRecord
import com.example.mylavanderiapp.features.maintenance.domain.repositories.MaintenanceRepository
import javax.inject.Inject

class AddMaintenanceRecordUseCase @Inject constructor(
    private val repository: MaintenanceRepository
) {
    suspend operator fun invoke (record: MaintenanceRecord): Result<Unit> = repository.add(record)
}