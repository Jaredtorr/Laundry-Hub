package com.example.mylavanderiapp.features.machines.domain.usecases

import com.example.mylavanderiapp.features.machines.domain.repositories.MachinesRepository

class DeleteMachineUseCase(
    private val repository: MachinesRepository
) {
    suspend operator fun invoke(id: String): Result<Boolean> {
        require(id.isNotBlank()) { "El ID de la máquina no puede estar vacío" }
        return repository.deleteMachine(id)
    }
}