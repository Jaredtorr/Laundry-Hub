package com.example.mylavanderiapp.features.laundry_reservation.domain.usecases

import com.example.mylavanderiapp.features.laundry_reservation.domain.repositories.IReservationRepository
import javax.inject.Inject

class CancelReservationUseCase @Inject constructor(
    private val repository: IReservationRepository
) {
    suspend operator fun invoke(id: Int): Result<String> {
        return try {
            val response = repository.cancelReservation(id)
            Result.success(response.message)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}