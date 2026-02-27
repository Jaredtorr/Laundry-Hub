package com.example.mylavanderiapp.features.laundry_reservation.domain.repositories

import com.example.mylavanderiapp.features.auth.data.datasource.remote.model.dto.MessageResponse
import com.example.mylavanderiapp.features.laundry_reservation.data.datasource.remote.model.CreateReservationRequest
import com.example.mylavanderiapp.features.laundry_reservation.data.datasource.remote.model.dto.ReservationResponse
import com.example.mylavanderiapp.features.laundry_reservation.data.datasource.remote.model.dto.ReservationsListResponse

interface IReservationRepository {
    suspend fun createReservation(request: CreateReservationRequest): ReservationResponse
    suspend fun getReservationById(id: Int): ReservationResponse
    suspend fun getMyReservations(): ReservationsListResponse
    suspend fun cancelReservation(id: Int): MessageResponse
    suspend fun completeReservation(id: Int): MessageResponse
}