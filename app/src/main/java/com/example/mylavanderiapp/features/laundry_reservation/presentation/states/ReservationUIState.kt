package com.example.mylavanderiapp.features.laundry_reservation.presentation.states

import com.example.mylavanderiapp.features.laundry_reservation.domain.entities.Reservation

sealed class ReservationUIState {
    data object Idle : ReservationUIState()
    data object Loading : ReservationUIState()
    data class Success(val reservation: Reservation) : ReservationUIState()
    data class Error(val message: String) : ReservationUIState()
}

sealed class MyReservationsUIState {
    data object Idle : MyReservationsUIState()
    data object Loading : MyReservationsUIState()
    data class Success(val reservations: List<Reservation>) : MyReservationsUIState()
    data class Error(val message: String) : MyReservationsUIState()
}