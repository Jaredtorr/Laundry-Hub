package com.example.mylavanderiapp.features.laundry_reservation.data.repositories

import com.example.mylavanderiapp.core.network.LaundryApi
import com.example.mylavanderiapp.features.laundry_reservation.data.datasource.remote.mapper.toDomain
import com.example.mylavanderiapp.features.laundry_reservation.data.datasource.remote.model.CreateReservationRequest
import com.example.mylavanderiapp.features.laundry_reservation.domain.entities.Reservation
import com.example.mylavanderiapp.features.laundry_reservation.domain.repositories.IReservationRepository
import javax.inject.Inject

class ReservationRepositoryImpl @Inject constructor(
    private val api: LaundryApi
) : IReservationRepository {

    override suspend fun createReservation(machineId: Int): Result<Reservation> = runCatching {
        api.createReservation(CreateReservationRequest(machineId)).reservation.toDomain()
    }

    override suspend fun getMyReservations(): Result<List<Reservation>> = runCatching {
        api.getMyReservations().reservations.map { it.toDomain() }
    }

    override suspend fun getReservationById(id: Int): Result<Reservation> = runCatching {
        api.getReservationById(id).reservation.toDomain()
    }

    override suspend fun cancelReservation(id: Int): Result<Unit> = runCatching {
        api.cancelReservation(id)
        Unit
    }

    override suspend fun completeReservation(id: Int): Result<Unit> = runCatching {
        api.completeReservation(id)
        Unit
    }
}