package com.example.mylavanderiapp.features.notifications.domain.usecases

import com.example.mylavanderiapp.features.notifications.domain.repositories.INotificationsRepository
import javax.inject.Inject

class MarkAsReadUseCase @Inject constructor(
    private val repository: INotificationsRepository
) {
    suspend operator fun invoke(id: Int): Result<Unit> = runCatching {
        repository.markAsRead(id)
    }
}