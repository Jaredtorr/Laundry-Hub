package com.example.mylavanderiapp.features.maintenance.domain.usecases

import com.example.mylavanderiapp.features.maintenance.domain.repositories.MaintenanceRepository
import javax.inject.Inject

class ResolveMaintenanceUseCase @Inject constructor(
    private val repository: MaintenanceRepository
) {
    suspend operator fun invoke(id: Int): Result<Unit> = repository.resolve(id)
}