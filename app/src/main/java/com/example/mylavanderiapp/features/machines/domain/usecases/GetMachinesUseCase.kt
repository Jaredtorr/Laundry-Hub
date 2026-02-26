package com.example.mylavanderiapp.features.machines.domain.usecases

import com.example.mylavanderiapp.features.machines.domain.entities.Machine
import com.example.mylavanderiapp.features.machines.domain.repositories.IMachinesRepository
import javax.inject.Inject

class GetMachinesUseCase @Inject constructor(
    private val repository: IMachinesRepository
) {
    suspend operator fun invoke(): Result<List<Machine>> {
        return try {
            Result.success(repository.getMachines())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}