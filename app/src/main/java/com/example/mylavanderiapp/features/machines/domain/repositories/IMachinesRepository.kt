package com.example.mylavanderiapp.features.machines.domain.repositories

import com.example.mylavanderiapp.features.machines.domain.entities.Machine

interface IMachinesRepository {
    suspend fun getMachines(): List<Machine>
    suspend fun getMachineById(id: Int): Machine
    suspend fun createMachine(machine: Machine): Machine
    suspend fun updateMachine(machine: Machine): Unit
    suspend fun deleteMachine(id: Int): Unit
}