package com.example.mylavanderiapp.features.laundry_reservation.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mylavanderiapp.features.laundry_reservation.domain.usecases.CancelReservationUseCase
import com.example.mylavanderiapp.features.laundry_reservation.domain.usecases.CreateReservationUseCase
import com.example.mylavanderiapp.features.laundry_reservation.domain.usecases.GetMyReservationsUseCase
import com.example.mylavanderiapp.features.laundry_reservation.presentation.states.MyReservationsUIState
import com.example.mylavanderiapp.features.laundry_reservation.presentation.states.ReservationUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReservationViewModel @Inject constructor(
    private val createReservationUseCase: CreateReservationUseCase,
    private val getMyReservationsUseCase: GetMyReservationsUseCase,
    private val cancelReservationUseCase: CancelReservationUseCase
) : ViewModel() {

    private val _createState = MutableStateFlow<ReservationUIState>(ReservationUIState.Idle)
    val createState: StateFlow<ReservationUIState> = _createState.asStateFlow()

    private val _myReservationsState = MutableStateFlow<MyReservationsUIState>(MyReservationsUIState.Idle)
    val myReservationsState: StateFlow<MyReservationsUIState> = _myReservationsState.asStateFlow()

    fun createReservation(machineId: Int) {
        viewModelScope.launch {
            _createState.value = ReservationUIState.Loading
            createReservationUseCase(machineId).fold(
                onSuccess = { _createState.value = ReservationUIState.Success(it) },
                onFailure = { _createState.value = ReservationUIState.Error(it.message ?: "Error al reservar") }
            )
        }
    }

    fun getMyReservations() {
        viewModelScope.launch {
            _myReservationsState.value = MyReservationsUIState.Loading
            getMyReservationsUseCase().fold(
                onSuccess = { _myReservationsState.value = MyReservationsUIState.Success(it) },
                onFailure = { _myReservationsState.value = MyReservationsUIState.Error(it.message ?: "Error al obtener reservaciones") }
            )
        }
    }

    fun cancelReservation(id: Int) {
        viewModelScope.launch {
            cancelReservationUseCase(id).fold(
                onSuccess = { getMyReservations() },
                onFailure = { _myReservationsState.value = MyReservationsUIState.Error(it.message ?: "Error al cancelar") }
            )
        }
    }

    fun resetCreateState() {
        _createState.value = ReservationUIState.Idle
    }
}