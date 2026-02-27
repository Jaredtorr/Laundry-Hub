package com.example.mylavanderiapp.features.laundry_reservation.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mylavanderiapp.features.laundry_reservation.domain.usecases.CancelReservationUseCase
import com.example.mylavanderiapp.features.laundry_reservation.domain.usecases.CreateReservationUseCase
import com.example.mylavanderiapp.features.laundry_reservation.domain.usecases.GetMyReservationsUseCase
import com.example.mylavanderiapp.features.laundry_reservation.presentation.states.MyReservationsUIState
import com.example.mylavanderiapp.features.laundry_reservation.presentation.states.ReservationUIState
import com.example.mylavanderiapp.features.machines.domain.usecases.GetMachinesUseCase
import com.example.mylavanderiapp.features.machines.presentation.states.MachinesUIState
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
    private val cancelReservationUseCase: CancelReservationUseCase,
    private val getMachinesUseCase: GetMachinesUseCase
) : ViewModel() {

    private val _createState = MutableStateFlow<ReservationUIState>(ReservationUIState.Idle)
    val createState: StateFlow<ReservationUIState> = _createState.asStateFlow()

    private val _myReservationsState = MutableStateFlow<MyReservationsUIState>(MyReservationsUIState.Idle)
    val myReservationsState: StateFlow<MyReservationsUIState> = _myReservationsState.asStateFlow()

    private val _machinesState = MutableStateFlow<MachinesUIState>(MachinesUIState.Idle)
    val machinesState: StateFlow<MachinesUIState> = _machinesState.asStateFlow()

    fun loadMachines() {
        viewModelScope.launch {
            _machinesState.value = MachinesUIState.Loading
            getMachinesUseCase().fold(
                onSuccess = { _machinesState.value = MachinesUIState.Success(it) },
                onFailure = { _machinesState.value = MachinesUIState.Error(it.message ?: "Error al cargar lavadoras") }
            )
        }
    }

    fun createReservation(machineId: Int) {
        viewModelScope.launch {
            _createState.value = ReservationUIState.Loading
            createReservationUseCase(machineId).fold(
                onSuccess = { _createState.value = ReservationUIState.Success(it) },
                onFailure = { _createState.value = ReservationUIState.Error(it.message ?: "Error al apartar") }
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