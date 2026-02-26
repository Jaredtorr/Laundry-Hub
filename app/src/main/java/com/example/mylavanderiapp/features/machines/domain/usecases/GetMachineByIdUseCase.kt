package com.example.mylavanderiapp.features.machines.domain.usecases

import com.example.mylavanderiapp.features.machines.domain.entities.Machine
import com.example.mylavanderiapp.features.machines.domain.repositories.IMachinesRepository
import javax.inject.Inject

class GetMachineByIdUseCase @Inject constructor(
    private val repository: IMachinesRepository
) {
    suspend operator fun invoke(id: Int): Result<Machine> {
        return try {
            Result.success(repository.getMachineById(id))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}