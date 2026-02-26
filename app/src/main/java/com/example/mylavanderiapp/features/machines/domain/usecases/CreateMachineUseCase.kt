package com.example.mylavanderiapp.features.machines.domain.usecases

import com.example.mylavanderiapp.features.machines.domain.entities.Machine
import com.example.mylavanderiapp.features.machines.domain.repositories.IMachinesRepository
import javax.inject.Inject

class CreateMachineUseCase @Inject constructor(
    private val repository: IMachinesRepository
) {
    suspend operator fun invoke(machine: Machine): Result<Machine> {
        return try {
            require(machine.name.isNotBlank())     { "El nombre de la máquina no puede estar vacío" }
            require(machine.capacity.isNotBlank()) { "La capacidad no puede estar vacía" }
            Result.success(repository.createMachine(machine))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}