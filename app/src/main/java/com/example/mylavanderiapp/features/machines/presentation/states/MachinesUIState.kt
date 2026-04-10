package com.example.mylavanderiapp.features.machines.presentation.states

import com.example.mylavanderiapp.features.machines.domain.entities.Machine

sealed class MachinesUIState {
    data object Idle    : MachinesUIState()
    data object Loading : MachinesUIState()
    data class Success(val machines: List<Machine>) : MachinesUIState()
    data class Error(val message: String)           : MachinesUIState()
}

sealed class MachineOperationState {
    data object Idle    : MachineOperationState()
    data object Loading : MachineOperationState()
    data class Success(val message: String) : MachineOperationState()
    data class Error(val message: String)   : MachineOperationState()
}