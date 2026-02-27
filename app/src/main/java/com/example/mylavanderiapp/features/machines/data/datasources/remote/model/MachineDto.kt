package com.example.mylavanderiapp.features.machines.data.datasources.remote.model

data class MachineDto(
    val id: Int,
    val name: String?,
    val status: String?,
    val capacity: String?,
    val location: String?,
    val createdAt: String?,
    val updatedAt: String?
)

data class MachinesListResponse(
    val machines: List<MachineDto>?
)

data class MachineDetailResponse(
    val machine: MachineDto
)

data class CreateMachineDto(
    val name: String,
    val capacity: String,
    val location: String?
)

data class UpdateMachineDto(
    val name: String,
    val status: String,
    val capacity: String,
    val location: String?
)

data class MachineMessageResponse(
    val message: String
)