package com.example.mylavanderiapp.features.laundry_reservation.data.datasource.remote.mapper

import com.example.mylavanderiapp.features.laundry_reservation.data.datasource.remote.model.dto.ReservationDto
import com.example.mylavanderiapp.features.laundry_reservation.domain.entities.Reservation

fun ReservationDto.toDomain(): Reservation {
    return Reservation(
        id = this.id,
        userId = this.userId,
        machineId = this.machineId,
        status = this.status,
        startedAt = this.startedAt,
        endedAt = this.endedAt
    )
}