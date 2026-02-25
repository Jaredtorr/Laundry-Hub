package com.example.mylavanderiapp.features.machines.data.datasources.remote.mapper

import com.example.mylavanderiapp.features.machines.data.datasources.remote.model.CreateMachineDto
import com.example.mylavanderiapp.features.machines.data.datasources.remote.model.MachineDto
import com.example.mylavanderiapp.features.machines.data.datasources.remote.model.UpdateMachineDto
import com.example.mylavanderiapp.features.machines.domain.entities.Machine
import com.example.mylavanderiapp.features.machines.domain.entities.MachineStatus

/**
 * Mapper para convertir entre DTOs y Entities
 */
object MachineMapper {

    fun toDomain(dto: MachineDto): Machine {
        return Machine(
            id = dto.id,
            name = dto.name,
            status = mapStatus(dto.status),
            capacity = dto.capacity,
            location = dto.location
        )
    }

    fun toDomainList(dtos: List<MachineDto>): List<Machine> {
        return dtos.map { toDomain(it) }
    }

    fun toCreateDto(machine: Machine): CreateMachineDto {
        return CreateMachineDto(
            name = machine.name,
            capacity = machine.capacity,
            location = machine.location,
            status = mapStatusToString(machine.status)
        )
    }

    fun toUpdateDto(machine: Machine): UpdateMachineDto {
        return UpdateMachineDto(
            name = machine.name,
            capacity = machine.capacity,
            location = machine.location,
            status = mapStatusToString(machine.status)
        )
    }

    private fun mapStatus(status: String): MachineStatus {
        return when (status.uppercase()) {
            "AVAILABLE", "DISPONIBLE" -> MachineStatus.AVAILABLE
            "OCCUPIED", "OCUPADA" -> MachineStatus.OCCUPIED
            "MAINTENANCE", "MANTENIMIENTO" -> MachineStatus.MAINTENANCE
            else -> MachineStatus.AVAILABLE
        }
    }

    private fun mapStatusToString(status: MachineStatus): String {
        return when (status) {
            MachineStatus.AVAILABLE -> "AVAILABLE"
            MachineStatus.OCCUPIED -> "OCCUPIED"
            MachineStatus.MAINTENANCE -> "MAINTENANCE"
        }
    }
}