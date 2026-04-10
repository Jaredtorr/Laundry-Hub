package com.example.mylavanderiapp.features.maintenance.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mylavanderiapp.core.network.WebSocketManager
import com.example.mylavanderiapp.features.machines.domain.usecases.GetMachinesUseCase
import com.example.mylavanderiapp.features.machines.presentation.states.MachinesUIState
import com.example.mylavanderiapp.features.maintenance.domain.entities.MaintenanceRecord
import com.example.mylavanderiapp.features.maintenance.domain.usecases.AddMaintenanceRecordUseCase
import com.example.mylavanderiapp.features.maintenance.domain.usecases.DeleteMaintenanceUseCase
import com.example.mylavanderiapp.features.maintenance.domain.usecases.GetMaintenanceRecordsUseCase
import com.example.mylavanderiapp.features.maintenance.domain.usecases.ResolveMaintenanceUseCase
import com.example.mylavanderiapp.features.maintenance.presentation.states.MaintenanceOperationState
import com.example.mylavanderiapp.features.maintenance.presentation.states.MaintenanceUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MaintenanceViewModel @Inject constructor(
    private val getRecordsUseCase    : GetMaintenanceRecordsUseCase,
    private val addRecordUseCase     : AddMaintenanceRecordUseCase,
    private val resolveRecordUseCase : ResolveMaintenanceUseCase,
    private val deleteRecordUseCase  : DeleteMaintenanceUseCase,
    private val getMachinesUseCase   : GetMachinesUseCase,
    private val webSocketManager     : WebSocketManager
) : ViewModel() {

    private val _uiState = MutableStateFlow<MaintenanceUIState>(MaintenanceUIState.Idle)
    val uiState: StateFlow<MaintenanceUIState> = _uiState.asStateFlow()

    private val _operationState = MutableStateFlow<MaintenanceOperationState>(MaintenanceOperationState.Idle)
    val operationState: StateFlow<MaintenanceOperationState> = _operationState.asStateFlow()

    private val _machinesState = MutableStateFlow<MachinesUIState>(MachinesUIState.Idle)
    val machinesState: StateFlow<MachinesUIState> = _machinesState.asStateFlow()

    private val _activeCount = MutableStateFlow(0)
    val activeCount: StateFlow<Int> = _activeCount.asStateFlow()

    private val _activeRecords = MutableStateFlow<List<MaintenanceRecord>>(emptyList())
    val activeRecords: StateFlow<List<MaintenanceRecord>> = _activeRecords.asStateFlow()

    private val _resolvedRecords = MutableStateFlow<List<MaintenanceRecord>>(emptyList())
    val resolvedRecords: StateFlow<List<MaintenanceRecord>> = _resolvedRecords.asStateFlow()

    init {
        loadRecords()
        loadMachines()
        collectWebSocketEvents()
    }

    private fun collectWebSocketEvents() {
        viewModelScope.launch {
            webSocketManager.notifications.collect {
                loadRecords()
                loadMachines()
            }
        }
    }

    fun loadMachines() {
        viewModelScope.launch {
            _machinesState.value = MachinesUIState.Loading
            getMachinesUseCase()
                .onSuccess { _machinesState.value = MachinesUIState.Success(it) }
                .onFailure { _machinesState.value = MachinesUIState.Error(it.message ?: "Error al cargar máquinas") }
        }
    }

    fun loadRecords() {
        viewModelScope.launch {
            _uiState.value = MaintenanceUIState.Loading
            getRecordsUseCase().fold(
                onSuccess = { records ->
                    _uiState.value      = MaintenanceUIState.Success(records)
                    _activeCount.value  = records.count { !it.isResolved }
                    _activeRecords.value   = records.filter { !it.isResolved }
                    _resolvedRecords.value = records.filter { it.isResolved }
                },
                onFailure = {
                    _uiState.value = MaintenanceUIState.Error(it.message ?: "Error al cargar")
                }
            )
        }
    }

    fun addRecord(record: MaintenanceRecord) {
        viewModelScope.launch {
            _operationState.value = MaintenanceOperationState.Loading
            addRecordUseCase(record).fold(
                onSuccess = {
                    _operationState.value = MaintenanceOperationState.Success("Registro agregado")
                    loadRecords()
                    loadMachines()
                },
                onFailure = { handleFailure(it) }
            )
        }
    }

    fun resolveRecord(id: Int) {
        viewModelScope.launch {
            _operationState.value = MaintenanceOperationState.Loading
            resolveRecordUseCase(id).fold(
                onSuccess = {
                    _operationState.value = MaintenanceOperationState.Success("Marcado como resuelto")
                    loadRecords()
                    loadMachines()
                },
                onFailure = { handleFailure(it) }
            )
        }
    }

    fun deleteRecord(id: Int) {
        viewModelScope.launch {
            _operationState.value = MaintenanceOperationState.Loading
            deleteRecordUseCase(id).fold(
                onSuccess = {
                    _operationState.value = MaintenanceOperationState.Success("Registro eliminado")
                    loadRecords()
                },
                onFailure = { handleFailure(it) }
            )
        }
    }

    private fun handleFailure(error: Throwable) {
        _operationState.value = MaintenanceOperationState.Error(error.message ?: "Error de conexión")
    }

    fun resetOperationState() {
        _operationState.value = MaintenanceOperationState.Idle
    }
}