package com.example.mylavanderiapp.features.machines.presentation.states

import com.example.mylavanderiapp.features.machines.domain.entities.Machine

sealed class MachinesUIState {
    object Idle    : MachinesUIState()
    object Loading : MachinesUIState()
    data class Success(val machines: List<Machine>) : MachinesUIState()
    data class Error(val message: String)           : MachinesUIState()
}

sealed class MachineOperationState {
    object Idle    : MachineOperationState()
    object Loading : MachineOperationState()
    data class Success(val message: String) : MachineOperationState()
    data class Error(val message: String)   : MachineOperationState()
}