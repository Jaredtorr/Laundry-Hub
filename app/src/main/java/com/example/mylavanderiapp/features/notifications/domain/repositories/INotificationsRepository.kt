package com.example.mylavanderiapp.features.notifications.domain.repositories

import com.example.mylavanderiapp.features.notifications.domain.entities.AppNotification

interface INotificationsRepository {
    suspend fun getMyNotifications(): Result<List<AppNotification>>
    suspend fun markAsRead(id: Int): Result<Unit>
    suspend fun markAllAsRead(): Result<Unit>
}