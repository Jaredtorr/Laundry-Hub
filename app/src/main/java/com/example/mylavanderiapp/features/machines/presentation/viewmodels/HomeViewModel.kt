package com.example.mylavanderiapp.features.machines.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mylavanderiapp.core.network.WebSocketManager
import com.example.mylavanderiapp.features.machines.domain.entities.Machine
import com.example.mylavanderiapp.features.machines.domain.usecases.CreateMachineUseCase
import com.example.mylavanderiapp.features.machines.domain.usecases.DeleteMachineUseCase
import com.example.mylavanderiapp.features.machines.domain.usecases.GetMachinesUseCase
import com.example.mylavanderiapp.features.machines.domain.usecases.UpdateMachineUseCase
import com.example.mylavanderiapp.features.machines.presentation.states.MachineOperationState
import com.example.mylavanderiapp.features.machines.presentation.states.MachinesUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getMachinesUseCase: GetMachinesUseCase,
    private val createMachineUseCase: CreateMachineUseCase,
    private val updateMachineUseCase: UpdateMachineUseCase,
    private val deleteMachineUseCase: DeleteMachineUseCase,
    private val webSocketManager: WebSocketManager
) : ViewModel() {

    private val _uiState = MutableStateFlow<MachinesUIState>(MachinesUIState.Idle)
    val uiState = _uiState.asStateFlow()

    private val _operationState = MutableStateFlow<MachineOperationState>(MachineOperationState.Idle)
    val operationState = _operationState.asStateFlow()

    init {
        loadMachines()
        collectWebSocketEvents()
    }

    private fun collectWebSocketEvents() {
        viewModelScope.launch {
            webSocketManager.notifications.collect {
                loadMachines()
            }
        }
    }
    fun loadMachines() {
        viewModelScope.launch {
            _uiState.value = MachinesUIState.Loading
            getMachinesUseCase()
                .onSuccess { _uiState.value = MachinesUIState.Success(it) }
                .onFailure { _uiState.value = MachinesUIState.Error(it.message ?: "Error al cargar") }
        }
    }

    fun addMachine(machine: Machine) {
        viewModelScope.launch {
            _operationState.value = MachineOperationState.Loading
            createMachineUseCase(machine)
                .onSuccess {
                    _operationState.value = MachineOperationState.Success("Máquina creada con éxito")
                    loadMachines()
                }
                .onFailure { handleFailure(it) }
        }
    }

    fun updateMachine(machine: Machine) {
        viewModelScope.launch {
            _operationState.value = MachineOperationState.Loading
            updateMachineUseCase(machine)
                .onSuccess {
                    _operationState.value = MachineOperationState.Success("Actualización exitosa")
                    loadMachines()
                }
                .onFailure { handleFailure(it) }
        }
    }

    fun deleteMachine(id: Int) {
        viewModelScope.launch {
            _operationState.value = MachineOperationState.Loading
            deleteMachineUseCase(id)
                .onSuccess {
                    _operationState.value = MachineOperationState.Success("Máquina eliminada")
                    loadMachines()
                }
                .onFailure { handleFailure(it) }
        }
    }

    private fun handleFailure(error: Throwable) {
        val msg = error.message ?: "Error de conexión"
        _operationState.value = MachineOperationState.Error(
            if (msg.contains("401")) "No autorizado (401): Revisa tu sesión" else msg
        )
    }

    fun resetOperationState() {
        _operationState.value = MachineOperationState.Idle
    }
}