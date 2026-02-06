package com.example.mylavanderiapp.features.machines.domain.usecases

import com.example.mylavanderiapp.features.machines.domain.entities.Machine
import com.example.mylavanderiapp.features.machines.domain.repositories.MachinesRepository

class GetMachineByIdUseCase(
    private val repository: MachinesRepository
) {
    suspend operator fun invoke(id: String): Result<Machine> {
        require(id.isNotBlank()) { "El ID de la máquina no puede estar vacío" }
        return repository.getMachineById(id)
    }
}