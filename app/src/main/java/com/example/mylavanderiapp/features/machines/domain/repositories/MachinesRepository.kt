package com.example.mylavanderiapp.features.machines.domain.repositories

import com.example.mylavanderiapp.features.machines.domain.entities.Machine

interface MachinesRepository {

    suspend fun getMachines(): Result<List<Machine>>

    suspend fun getMachineById(id: String): Result<Machine>

    suspend fun createMachine(machine: Machine): Result<Machine>

    suspend fun updateMachine(machine: Machine): Result<Machine>

    suspend fun deleteMachine(id: String): Result<Boolean>
}