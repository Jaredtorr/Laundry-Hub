package com.example.mylavanderiapp.features.laundry_reservation.domain.repositories

import com.example.mylavanderiapp.features.laundry_reservation.domain.entities.Reservation

interface IReservationRepository {
    suspend fun createReservation(machineId: Int): Result<Reservation>
    suspend fun getMyReservations(): Result<List<Reservation>>
    suspend fun getReservationById(id: Int): Result<Reservation>
    suspend fun cancelReservation(id: Int): Result<Unit>
    suspend fun completeReservation(id: Int): Result<Unit>
}