package com.example.mylavanderiapp.features.laundry_reservation.domain.entities

data class Reservation(
    val id         : Int,
    val userId     : Int,
    val machineId  : Int,
    val machineName: String,
    val status     : String,
    val createdAt  : String,
    val endedAt    : String?
)