package com.example.mylavanderiapp.features.maintenance.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mylavanderiapp.features.maintenance.domain.entities.MaintenanceRecord
import com.example.mylavanderiapp.features.maintenance.domain.usecases.AddMaintenanceRecordUseCase
import com.example.mylavanderiapp.features.maintenance.domain.usecases.DeleteMaintenanceUseCase
import com.example.mylavanderiapp.features.maintenance.domain.usecases.GetMaintenanceRecordsUseCase
import com.example.mylavanderiapp.features.maintenance.domain.usecases.ResolveMaintenanceUseCase
import com.example.mylavanderiapp.features.maintenance.presentation.states.MaintenanceOperationState
import com.example.mylavanderiapp.features.maintenance.presentation.states.MaintenanceUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MaintenanceViewModel @Inject constructor(
    private val getRecordsUseCase    : GetMaintenanceRecordsUseCase,
    private val addRecordUseCase     : AddMaintenanceRecordUseCase,
    private val resolveRecordUseCase : ResolveMaintenanceUseCase,
    private val deleteRecordUseCase  : DeleteMaintenanceUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<MaintenanceUIState>(MaintenanceUIState.Idle)
    val uiState = _uiState.asStateFlow()

    private val _operationState = MutableStateFlow<MaintenanceOperationState>(MaintenanceOperationState.Idle)
    val operationState = _operationState.asStateFlow()

    init { loadRecords() }

    fun loadRecords() {
        viewModelScope.launch {
            _uiState.value = MaintenanceUIState.Loading
            val result = getRecordsUseCase()
            if (result.isSuccess) {
                _uiState.value = MaintenanceUIState.Success(result.getOrDefault(emptyList()))
            } else {
                _uiState.value = MaintenanceUIState.Error(result.exceptionOrNull()?.message ?: "Error al cargar")
            }
        }
    }

    fun addRecord(record: MaintenanceRecord) {
        viewModelScope.launch {
            _operationState.value = MaintenanceOperationState.Loading
            val result = addRecordUseCase(record)
            if (result.isSuccess) {
                _operationState.value = MaintenanceOperationState.Success("Registro agregado")
                loadRecords()
            } else {
                handleFailure(result.exceptionOrNull())
            }
        }
    }

    fun resolveRecord(id: Int) {
        viewModelScope.launch {
            _operationState.value = MaintenanceOperationState.Loading
            val result = resolveRecordUseCase(id)
            if (result.isSuccess) {
                _operationState.value = MaintenanceOperationState.Success("Marcado como resuelto")
                loadRecords()
            } else {
                handleFailure(result.exceptionOrNull())
            }
        }
    }

    fun deleteRecord(id: Int) {
        viewModelScope.launch {
            _operationState.value = MaintenanceOperationState.Loading
            val result = deleteRecordUseCase(id)
            if (result.isSuccess) {
                _operationState.value = MaintenanceOperationState.Success("Registro eliminado")
                loadRecords()
            } else {
                handleFailure(result.exceptionOrNull())
            }
        }
    }

    private fun handleFailure(error: Throwable?) {
        _operationState.value = MaintenanceOperationState.Error(
            error?.message ?: "Error de conexión"
        )
    }

    fun resetOperationState() {
        _operationState.value = MaintenanceOperationState.Idle
    }
}