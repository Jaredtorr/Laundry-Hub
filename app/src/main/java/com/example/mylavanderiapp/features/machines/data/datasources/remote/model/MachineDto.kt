package com.example.mylavanderiapp.features.machines.data.datasources.remote.model

/**
 * Estos modelos representan la estructura de datos que viene del servidor
 */

data class MachineDto(
    val id: String,
    val name: String,
    val status: String,
    val capacity: String,
    val location: String,
    val createdAt: String? = null,
    val updatedAt: String? = null
)

data class MachinesListResponse(
    val success: Boolean,
    val data: List<MachineDto>,
    val message: String? = null
)

data class MachineDetailResponse(
    val success: Boolean,
    val data: MachineDto?,
    val message: String? = null
)

data class CreateMachineDto(
    val name: String,
    val capacity: String,
    val location: String,
    val status: String
)

data class UpdateMachineDto(
    val name: String? = null,
    val capacity: String? = null,
    val location: String? = null,
    val status: String? = null
)