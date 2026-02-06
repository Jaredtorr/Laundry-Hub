package com.example.mylavanderiapp.features.machines.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mylavanderiapp.features.machines.domain.entities.Machine
import com.example.mylavanderiapp.features.machines.domain.usecases.*
import com.example.mylavanderiapp.features.machines.presentation.states.MachineOperationState
import com.example.mylavanderiapp.features.machines.presentation.states.MachinesUIState
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class HomeViewModel(
    private val getMachinesUseCase: GetMachinesUseCase,
    private val createMachineUseCase: CreateMachineUseCase,
    private val updateMachineUseCase: UpdateMachineUseCase,
    private val deleteMachineUseCase: DeleteMachineUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<MachinesUIState>(MachinesUIState.Idle)
    val uiState = _uiState.asStateFlow()

    private val _operationState = MutableStateFlow<MachineOperationState>(MachineOperationState.Idle)
    val operationState = _operationState.asStateFlow()

    init { loadMachines() }

    fun loadMachines() {
        viewModelScope.launch {
            _uiState.value = MachinesUIState.Loading
            getMachinesUseCase().onSuccess { _uiState.value = MachinesUIState.Success(it) }
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

    fun deleteMachine(id: String) {
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

    fun resetOperationState() { _operationState.value = MachineOperationState.Idle }
}