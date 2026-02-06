package com.example.mylavanderiapp.features.machines.domain.usecases

import com.example.mylavanderiapp.features.machines.domain.entities.Machine
import com.example.mylavanderiapp.features.machines.domain.repositories.MachinesRepository

class UpdateMachineUseCase(
    private val repository: MachinesRepository
) {
    suspend operator fun invoke(machine: Machine): Result<Machine> {
        // Validaciones de negocio
        require(machine.id.isNotBlank()) { "El ID de la máquina no puede estar vacío" }
        require(machine.name.isNotBlank()) { "El nombre de la máquina no puede estar vacío" }
        require(machine.capacity.isNotBlank()) { "La capacidad no puede estar vacía" }
        require(machine.location.isNotBlank()) { "La ubicación no puede estar vacía" }

        return repository.updateMachine(machine)
    }
}