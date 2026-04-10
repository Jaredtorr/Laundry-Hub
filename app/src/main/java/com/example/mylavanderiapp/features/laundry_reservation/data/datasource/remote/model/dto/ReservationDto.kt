package com.example.mylavanderiapp.features.laundry_reservation.data.datasource.remote.model.dto

data class ReservationDto(
    val id         : Int,
    val userId     : Int,
    val machineId  : Int,
    val machineName: String?,   // si el backend lo manda plano
    val machine    : MachineRefDto?, // si el backend lo manda anidado
    val status     : String,
    val startedAt  : String,
    val endedAt    : String?
)

data class MachineRefDto(
    val id  : Int,
    val name: String?
)