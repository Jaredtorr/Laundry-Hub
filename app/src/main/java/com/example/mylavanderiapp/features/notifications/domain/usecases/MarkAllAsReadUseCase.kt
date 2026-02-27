package com.example.mylavanderiapp.features.notifications.domain.usecases

import com.example.mylavanderiapp.features.notifications.domain.repositories.INotificationsRepository
import javax.inject.Inject

class MarkAllAsReadUseCase @Inject constructor(
    private val repository: INotificationsRepository
) {
    suspend operator fun invoke(): Result<Unit> = runCatching {
        repository.markAllAsRead()
    }
}