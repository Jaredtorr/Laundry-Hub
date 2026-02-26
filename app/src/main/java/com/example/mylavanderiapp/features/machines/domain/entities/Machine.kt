package com.example.mylavanderiapp.features.machines.domain.entities

data class Machine(
    val id: Int,
    val name: String,
    val status: MachineStatus,
    val capacity: String,
    val location: String?
)

enum class MachineStatus {
    AVAILABLE,
    OCCUPIED,
    MAINTENANCE
}