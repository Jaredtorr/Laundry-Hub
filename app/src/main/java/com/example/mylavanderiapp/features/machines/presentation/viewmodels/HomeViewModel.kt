package com.example.mylavanderiapp.features.machines.presentation.viewmodels

import androidx.lifecycle.ViewModel
import com.example.mylavanderiapp.features.machines.domain.entities.Machine
import com.example.mylavanderiapp.features.machines.domain.entities.MachineStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class HomeViewModel : ViewModel() {

    private val _machines = MutableStateFlow<List<Machine>>(emptyList())
    val machines: StateFlow<List<Machine>> = _machines.asStateFlow()

    init {
        loadMachines()
    }

    private fun loadMachines() {
        // Datos de ejemplo (simulación)
        _machines.value = listOf(
            Machine(
                id = "1",
                name = "Lavadora 1",
                status = MachineStatus.AVAILABLE,
                capacity = "8 kg",
                location = "Piso 1"
            ),
            Machine(
                id = "2",
                name = "Lavadora 2",
                status = MachineStatus.OCCUPIED,
                capacity = "10 kg",
                location = "Piso 1"
            ),
            Machine(
                id = "3",
                name = "Lavadora 3",
                status = MachineStatus.AVAILABLE,
                capacity = "8 kg",
                location = "Piso 2"
            ),
            Machine(
                id = "4",
                name = "Lavadora 4",
                status = MachineStatus.MAINTENANCE,
                capacity = "12 kg",
                location = "Piso 2"
            ),
            Machine(
                id = "5",
                name = "Lavadora 5",
                status = MachineStatus.AVAILABLE,
                capacity = "8 kg",
                location = "Piso 1"
            )
        )
    }

    fun addMachine(machine: Machine) {
        _machines.value = _machines.value + machine
    }

    fun updateMachine(machine: Machine) {
        _machines.value = _machines.value.map {
            if (it.id == machine.id) machine else it
        }
    }

    fun deleteMachine(machineId: String) {
        _machines.value = _machines.value.filter { it.id != machineId }
    }
}