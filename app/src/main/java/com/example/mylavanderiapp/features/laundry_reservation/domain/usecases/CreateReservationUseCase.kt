package com.example.mylavanderiapp.features.laundry_reservation.domain.usecases

import com.example.mylavanderiapp.features.laundry_reservation.data.datasource.remote.mapper.toDomain
import com.example.mylavanderiapp.features.laundry_reservation.data.datasource.remote.model.CreateReservationRequest
import com.example.mylavanderiapp.features.laundry_reservation.domain.entities.Reservation
import com.example.mylavanderiapp.features.laundry_reservation.domain.repositories.IReservationRepository
import javax.inject.Inject

class CreateReservationUseCase @Inject constructor(
    private val repository: IReservationRepository
) {
    suspend operator fun invoke(machineId: Int): Result<Reservation> {
        return try {
            val response = repository.createReservation(CreateReservationRequest(machineId))
            Result.success(response.reservation.toDomain())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}