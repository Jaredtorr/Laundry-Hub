package com.example.mylavanderiapp.features.laundry_reservation.domain.usecases

import com.example.mylavanderiapp.features.laundry_reservation.data.datasource.remote.mapper.toDomain
import com.example.mylavanderiapp.features.laundry_reservation.domain.entities.Reservation
import com.example.mylavanderiapp.features.laundry_reservation.domain.repositories.IReservationRepository
import javax.inject.Inject

class GetMyReservationsUseCase @Inject constructor(
    private val repository: IReservationRepository
) {
    suspend operator fun invoke(): Result<List<Reservation>> {
        return try {
            val response = repository.getMyReservations()
            Result.success(response.reservations.map { it.toDomain() })
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}