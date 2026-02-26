package com.example.mylavanderiapp.features.machines.data.datasources.remote.mapper

import com.example.mylavanderiapp.features.machines.data.datasources.remote.model.MachineDto
import com.example.mylavanderiapp.features.machines.domain.entities.Machine
import com.example.mylavanderiapp.features.machines.domain.entities.MachineStatus

fun MachineDto.toDomain(): Machine {
    return Machine(
        id = this.id,
        name = this.name,
        status = mapStatus(this.status),
        capacity = this.capacity,
        location = this.location
    )
}

private fun mapStatus(status: String): MachineStatus {
    return when (status.uppercase()) {
        "AVAILABLE", "DISPONIBLE"       -> MachineStatus.AVAILABLE
        "OCCUPIED", "OCUPADA"           -> MachineStatus.OCCUPIED
        "MAINTENANCE", "MANTENIMIENTO"  -> MachineStatus.MAINTENANCE
        else                            -> MachineStatus.AVAILABLE
    }
}

fun MachineStatus.toApiString(): String {
    return when (this) {
        MachineStatus.AVAILABLE   -> "AVAILABLE"
        MachineStatus.OCCUPIED    -> "OCCUPIED"
        MachineStatus.MAINTENANCE -> "MAINTENANCE"
    }
}