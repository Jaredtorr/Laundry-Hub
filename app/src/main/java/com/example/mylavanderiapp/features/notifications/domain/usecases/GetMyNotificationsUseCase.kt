package com.example.mylavanderiapp.features.notifications.domain.usecases

import com.example.mylavanderiapp.features.notifications.domain.entities.AppNotification
import com.example.mylavanderiapp.features.notifications.domain.repositories.INotificationsRepository
import javax.inject.Inject

class GetMyNotificationsUseCase @Inject constructor(
    private val repository: INotificationsRepository
) {
    suspend operator fun invoke(): Result<List<AppNotification>> = runCatching {
        repository.getMyNotifications()
    }
}