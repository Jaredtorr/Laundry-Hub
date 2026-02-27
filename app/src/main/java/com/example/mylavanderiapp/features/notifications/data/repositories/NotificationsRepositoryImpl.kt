package com.example.mylavanderiapp.features.notifications.data.repositories

import com.example.mylavanderiapp.core.network.LaundryApi
import com.example.mylavanderiapp.features.notifications.data.datasources.remote.mapper.toDomain
import com.example.mylavanderiapp.features.notifications.domain.entities.AppNotification
import com.example.mylavanderiapp.features.notifications.domain.repositories.INotificationsRepository
import javax.inject.Inject

class NotificationsRepositoryImpl @Inject constructor(
    private val api: LaundryApi
) : INotificationsRepository {

    override suspend fun getMyNotifications(): List<AppNotification> {
        return api.getMyNotifications().notifications?.map { it.toDomain() } ?: emptyList()
    }

    override suspend fun markAsRead(id: Int) {
        api.markNotificationAsRead(id)
    }

    override suspend fun markAllAsRead() {
        api.markAllNotificationsAsRead()
    }
}