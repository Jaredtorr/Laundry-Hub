package com.example.mylavanderiapp.features.laundry_reservation.data.repositories

import com.example.mylavanderiapp.core.network.LaundryApi
import com.example.mylavanderiapp.features.auth.data.datasource.remote.model.dto.MessageResponse
import com.example.mylavanderiapp.features.laundry_reservation.data.datasource.remote.model.CreateReservationRequest
import com.example.mylavanderiapp.features.laundry_reservation.data.datasource.remote.model.dto.ReservationResponse
import com.example.mylavanderiapp.features.laundry_reservation.data.datasource.remote.model.dto.ReservationsListResponse
import com.example.mylavanderiapp.features.laundry_reservation.domain.repositories.IReservationRepository
import javax.inject.Inject

class ReservationRepositoryImpl @Inject constructor(
    private val api: LaundryApi
) : IReservationRepository {

    override suspend fun createReservation(request: CreateReservationRequest): ReservationResponse {
        return api.createReservation(request)
    }

    override suspend fun getReservationById(id: Int): ReservationResponse {
        return api.getReservationById(id)
    }

    override suspend fun getMyReservations(): ReservationsListResponse {
        return api.getMyReservations()
    }

    override suspend fun cancelReservation(id: Int): MessageResponse {
        return api.cancelReservation(id)
    }

    override suspend fun completeReservation(id: Int): MessageResponse {
        return api.completeReservation(id)
    }
}