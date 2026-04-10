package com.example.mylavanderiapp.features.machines.domain.repositories

import com.example.mylavanderiapp.features.machines.domain.entities.Machine

interface IMachinesRepository {
    suspend fun getMachines(): Result<List<Machine>>
    suspend fun getMachineById(id: Int): Result<Machine>
    suspend fun createMachine(machine: Machine): Result<Machine>
    suspend fun updateMachine(machine: Machine): Result<Unit>
    suspend fun deleteMachine(id: Int): Result<Unit>
}