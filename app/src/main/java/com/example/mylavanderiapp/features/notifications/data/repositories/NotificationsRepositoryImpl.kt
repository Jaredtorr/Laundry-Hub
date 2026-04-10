package com.example.mylavanderiapp.features.notifications.data.repositories

import com.example.mylavanderiapp.core.network.LaundryApi
import com.example.mylavanderiapp.features.notifications.data.datasources.remote.mapper.toDomain
import com.example.mylavanderiapp.features.notifications.domain.entities.AppNotification
import com.example.mylavanderiapp.features.notifications.domain.repositories.INotificationsRepository
import javax.inject.Inject

class NotificationsRepositoryImpl @Inject constructor(
    private val api: LaundryApi
) : INotificationsRepository {

    override suspend fun getMyNotifications(): Result<List<AppNotification>> = runCatching {
        api.getMyNotifications().notifications?.map { it.toDomain() } ?: emptyList()
    }

    override suspend fun markAsRead(id: Int): Result<Unit> = runCatching {
        api.markNotificationAsRead(id)
        Unit
    }

    override suspend fun markAllAsRead(): Result<Unit> = runCatching {
        api.markAllNotificationsAsRead()
        Unit
    }
}