package com.example.mylavanderiapp.features.machines.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mylavanderiapp.features.machines.domain.usecases.CreateMachineUseCase
import com.example.mylavanderiapp.features.machines.domain.usecases.DeleteMachineUseCase
import com.example.mylavanderiapp.features.machines.domain.usecases.GetMachinesUseCase
import com.example.mylavanderiapp.features.machines.domain.usecases.UpdateMachineUseCase

/**
 * Inyecta dependencias en el ViewModel
 */
class HomeViewModelFactory(
    private val getMachinesUseCase: GetMachinesUseCase,
    private val createMachineUseCase: CreateMachineUseCase,
    private val updateMachineUseCase: UpdateMachineUseCase,
    private val deleteMachineUseCase: DeleteMachineUseCase
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(
                getMachinesUseCase = getMachinesUseCase,
                createMachineUseCase = createMachineUseCase,
                updateMachineUseCase = updateMachineUseCase,
                deleteMachineUseCase = deleteMachineUseCase
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}