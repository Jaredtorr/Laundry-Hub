package com.example.mylavanderiapp.features.machines.data.repositories

import com.example.mylavanderiapp.core.network.LaundryApi
import com.example.mylavanderiapp.features.machines.data.datasources.remote.mapper.toDomain
import com.example.mylavanderiapp.features.machines.data.datasources.remote.mapper.toApiString
import com.example.mylavanderiapp.features.machines.data.datasources.remote.model.CreateMachineDto
import com.example.mylavanderiapp.features.machines.data.datasources.remote.model.UpdateMachineDto
import com.example.mylavanderiapp.features.machines.domain.entities.Machine
import com.example.mylavanderiapp.features.machines.domain.repositories.IMachinesRepository
import javax.inject.Inject

class MachinesRepositoryImpl @Inject constructor(
    private val api: LaundryApi
) : IMachinesRepository {

    override suspend fun getMachines(): Result<List<Machine>> = runCatching {
        api.getAllMachines().machines?.map { it.toDomain() } ?: emptyList()
    }

    override suspend fun getMachineById(id: Int): Result<Machine> = runCatching {
        api.getMachineById(id).machine.toDomain()
    }

    override suspend fun createMachine(machine: Machine): Result<Machine> = runCatching {
        api.createMachine(
            CreateMachineDto(
                name     = machine.name,
                capacity = machine.capacity,
                location = machine.location
            )
        ).machine.toDomain()
    }

    override suspend fun updateMachine(machine: Machine): Result<Unit> = runCatching {
        api.updateMachine(
            machine.id,
            UpdateMachineDto(
                name     = machine.name,
                status   = machine.status.toApiString(),
                capacity = machine.capacity,
                location = machine.location
            )
        )
        Unit
    }

    override suspend fun deleteMachine(id: Int): Result<Unit> = runCatching {
        api.deleteMachine(id)
        Unit
    }
}