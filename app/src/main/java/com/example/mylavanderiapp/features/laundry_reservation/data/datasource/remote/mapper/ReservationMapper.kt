package com.example.mylavanderiapp.features.laundry_reservation.data.datasource.remote.mapper

import com.example.mylavanderiapp.features.laundry_reservation.data.datasource.remote.model.dto.ReservationDto
import com.example.mylavanderiapp.features.laundry_reservation.domain.entities.Reservation

fun ReservationDto.toDomain(): Reservation = Reservation(
    id          = id,
    userId      = userId,
    machineId   = machineId,
    machineName = when {
        !machineName.isNullOrBlank() -> machineName
        !machine?.name.isNullOrBlank() -> machine!!.name!!
        else -> "Lavadora #$machineId"
    },
    status    = status,
    createdAt = startedAt,
    endedAt   = endedAt
)