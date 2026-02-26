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

    override suspend fun getMachines(): List<Machine> {
        return api.getAllMachines().machines.map { it.toDomain() }
    }

    override suspend fun getMachineById(id: Int): Machine {
        return api.getMachineById(id).machine.toDomain()
    }

    override suspend fun createMachine(machine: Machine): Machine {
        val request = CreateMachineDto(
            name = machine.name,
            capacity = machine.capacity,
            location = machine.location
        )
        return api.createMachine(request).machine.toDomain()
    }

    override suspend fun updateMachine(machine: Machine) {
        val request = UpdateMachineDto(
            name = machine.name,
            status = machine.status.toApiString(),
            capacity = machine.capacity,
            location = machine.location
        )
        api.updateMachine(machine.id, request)
    }

    override suspend fun deleteMachine(id: Int) {
        api.deleteMachine(id)
    }
}