package com.example.mylavanderiapp.features.machines.data.repositories

import com.example.mylavanderiapp.core.network.CreateMachineRequest
import com.example.mylavanderiapp.core.network.LaundryApi
import com.example.mylavanderiapp.core.network.UpdateMachineRequest
import com.example.mylavanderiapp.features.machines.data.datasources.remote.mapper.MachineMapper
import com.example.mylavanderiapp.features.machines.domain.entities.Machine
import com.example.mylavanderiapp.features.machines.domain.repositories.MachinesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Maneja la comunicación con la API
 */
class MachinesRepositoryImpl(
    private val api: LaundryApi
) : MachinesRepository {

    override suspend fun getMachines(): Result<List<Machine>> = withContext(Dispatchers.IO) {
        try {
            val response = api.getMachines()

            if (response.success) {
                val machines = response.data.map { machineData ->
                    Machine(
                        id = machineData.id,
                        name = machineData.name,
                        status = MachineMapper.mapStatus(machineData.status),
                        capacity = machineData.capacity,
                        location = machineData.location
                    )
                }
                Result.success(machines)
            } else {
                Result.failure(Exception(response.message ?: "Error al obtener máquinas"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getMachineById(id: String): Result<Machine> = withContext(Dispatchers.IO) {
        try {
            val response = api.getMachineById(id)

            if (response.success && response.data != null) {
                val machineData = response.data
                val machine = Machine(
                    id = machineData.id,
                    name = machineData.name,
                    status = MachineMapper.mapStatus(machineData.status),
                    capacity = machineData.capacity,
                    location = machineData.location
                )
                Result.success(machine)
            } else {
                Result.failure(Exception(response.message ?: "Máquina no encontrada"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun createMachine(machine: Machine): Result<Machine> = withContext(Dispatchers.IO) {
        try {
            // Obtener el token del usuario actual desde SharedPreferences/DataStore
            val token = "Bearer fake_token"

            val request = CreateMachineRequest(
                name = machine.name,
                capacity = machine.capacity,
                location = machine.location,
                status = MachineMapper.mapStatusToString(machine.status)
            )

            val response = api.createMachine(token = token, request = request)

            if (response.success && response.data != null) {
                val machineData = response.data
                val createdMachine = Machine(
                    id = machineData.id,
                    name = machineData.name,
                    status = MachineMapper.mapStatus(machineData.status),
                    capacity = machineData.capacity,
                    location = machineData.location
                )
                Result.success(createdMachine)
            } else {
                Result.failure(Exception(response.message ?: "Error al crear máquina"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateMachine(machine: Machine): Result<Machine> = withContext(Dispatchers.IO) {
        try {
            val token = "Bearer fake_token"

            val request = UpdateMachineRequest(
                name = machine.name,
                capacity = machine.capacity,
                location = machine.location,
                status = MachineMapper.mapStatusToString(machine.status)
            )

            val response = api.updateMachine(
                id = machine.id,
                token = token,
                request = request
            )

            if (response.success && response.data != null) {
                val machineData = response.data
                val updatedMachine = Machine(
                    id = machineData.id,
                    name = machineData.name,
                    status = MachineMapper.mapStatus(machineData.status),
                    capacity = machineData.capacity,
                    location = machineData.location
                )
                Result.success(updatedMachine)
            } else {
                Result.failure(Exception(response.message ?: "Error al actualizar máquina"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteMachine(id: String): Result<Boolean> = withContext(Dispatchers.IO) {
        try {
            val token = "Bearer fake_token"
            val response = api.deleteMachine(id, token)

            if (response.success) {
                Result.success(true)
            } else {
                Result.failure(Exception(response.message))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

// Extension para MachineMapper dentro del mismo archivo
private fun MachineMapper.mapStatus(status: String): com.example.mylavanderiapp.features.machines.domain.entities.MachineStatus {
    return when (status.uppercase()) {
        "AVAILABLE", "DISPONIBLE" -> com.example.mylavanderiapp.features.machines.domain.entities.MachineStatus.AVAILABLE
        "OCCUPIED", "OCUPADA" -> com.example.mylavanderiapp.features.machines.domain.entities.MachineStatus.OCCUPIED
        "MAINTENANCE", "MANTENIMIENTO" -> com.example.mylavanderiapp.features.machines.domain.entities.MachineStatus.MAINTENANCE
        else -> com.example.mylavanderiapp.features.machines.domain.entities.MachineStatus.AVAILABLE
    }
}

private fun MachineMapper.mapStatusToString(status: com.example.mylavanderiapp.features.machines.domain.entities.MachineStatus): String {
    return when (status) {
        com.example.mylavanderiapp.features.machines.domain.entities.MachineStatus.AVAILABLE -> "AVAILABLE"
        com.example.mylavanderiapp.features.machines.domain.entities.MachineStatus.OCCUPIED -> "OCCUPIED"
        com.example.mylavanderiapp.features.machines.domain.entities.MachineStatus.MAINTENANCE -> "MAINTENANCE"
    }
}