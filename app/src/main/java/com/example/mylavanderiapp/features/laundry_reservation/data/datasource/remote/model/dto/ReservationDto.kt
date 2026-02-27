package com.example.mylavanderiapp.features.laundry_reservation.data.datasource.remote.model.dto

data class ReservationDto(
    val id: Int,
    val userId: Int,
    val machineId: Int,
    val status: String,
    val startedAt: String,
    val endedAt: String?
)