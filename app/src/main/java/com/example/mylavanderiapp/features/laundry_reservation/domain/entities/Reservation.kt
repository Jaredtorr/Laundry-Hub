package com.example.mylavanderiapp.features.laundry_reservation.domain.entities

data class Reservation(
    val id: Int,
    val userId: Int,
    val machineId: Int,
    val status: String,
    val startedAt: String,
    val endedAt: String?
)