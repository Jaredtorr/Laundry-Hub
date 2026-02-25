package com.example.mylavanderiapp.features.machines.domain.usecases

import com.example.mylavanderiapp.features.machines.domain.entities.Machine
import com.example.mylavanderiapp.features.machines.domain.repositories.MachinesRepository

class GetMachinesUseCase(
    private val repository: MachinesRepository
) {
    suspend operator fun invoke(): Result<List<Machine>> {
        return repository.getMachines()
    }
}