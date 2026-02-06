package com.example.mylavanderiapp.features.machines.domain.entities

data class Machine(
    val id: String,
    val name: String,
    val status: MachineStatus,
    val capacity: String = "8 kg",
    val location: String = "Piso 1"
)

enum class MachineStatus {
    AVAILABLE,
    OCCUPIED,
    MAINTENANCE
}