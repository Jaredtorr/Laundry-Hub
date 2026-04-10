package com.example.mylavanderiapp.features.laundry_reservation.domain.usecases

import com.example.mylavanderiapp.features.laundry_reservation.domain.repositories.IReservationRepository
import javax.inject.Inject

class CompleteReservationUseCase @Inject constructor(
    private val repository: IReservationRepository
) {
    suspend operator fun invoke(id: Int): Result<Unit> =
        repository.completeReservation(id)
}