package com.example.mylavanderiapp.features.maintenance.domain.usecases

import com.example.mylavanderiapp.features.maintenance.domain.entities.MaintenanceRecord
import com.example.mylavanderiapp.features.maintenance.domain.repositories.MaintenanceRepository
import javax.inject.Inject

class GetMaintenanceRecordsUseCase @Inject constructor(
    private val repository: MaintenanceRepository
) {
    suspend operator fun invoke(): Result<List<MaintenanceRecord>> =
        repository.getAll()
}