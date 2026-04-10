package com.example.mylavanderiapp.features.machines.domain.usecases

import com.example.mylavanderiapp.features.machines.domain.repositories.IMachinesRepository
import javax.inject.Inject

class DeleteMachineUseCase @Inject constructor(
    private val repository: IMachinesRepository
) {
    suspend operator fun invoke(id: Int): Result<Unit> =
        repository.deleteMachine(id)
}