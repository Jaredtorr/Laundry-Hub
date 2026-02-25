package com.example.mylavanderiapp.features.machines.di

import com.example.mylavanderiapp.core.network.LaundryApi
import com.example.mylavanderiapp.features.machines.data.repositories.MachinesRepositoryImpl
import com.example.mylavanderiapp.features.machines.domain.repositories.MachinesRepository
import com.example.mylavanderiapp.features.machines.domain.usecases.CreateMachineUseCase
import com.example.mylavanderiapp.features.machines.domain.usecases.DeleteMachineUseCase
import com.example.mylavanderiapp.features.machines.domain.usecases.GetMachineByIdUseCase
import com.example.mylavanderiapp.features.machines.domain.usecases.GetMachinesUseCase
import com.example.mylavanderiapp.features.machines.domain.usecases.UpdateMachineUseCase

/**
 * Módulo de inyección de dependencias para el feature de Machines
 */
class MachinesModule(
    private val api: LaundryApi
) {

    // Repository
    val machinesRepository: MachinesRepository by lazy {
        MachinesRepositoryImpl(api)
    }

    // Use Cases
    val getMachinesUseCase: GetMachinesUseCase by lazy {
        GetMachinesUseCase(machinesRepository)
    }

    val getMachineByIdUseCase: GetMachineByIdUseCase by lazy {
        GetMachineByIdUseCase(machinesRepository)
    }

    val createMachineUseCase: CreateMachineUseCase by lazy {
        CreateMachineUseCase(machinesRepository)
    }

    val updateMachineUseCase: UpdateMachineUseCase by lazy {
        UpdateMachineUseCase(machinesRepository)
    }

    val deleteMachineUseCase: DeleteMachineUseCase by lazy {
        DeleteMachineUseCase(machinesRepository)
    }
}