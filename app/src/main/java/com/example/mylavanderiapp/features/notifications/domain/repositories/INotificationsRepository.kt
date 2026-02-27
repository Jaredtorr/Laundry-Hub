package com.example.mylavanderiapp.features.notifications.domain.repositories

import com.example.mylavanderiapp.features.notifications.domain.entities.AppNotification

interface INotificationsRepository {
    suspend fun getMyNotifications(): List<AppNotification>  // GET /notifications/my
    suspend fun markAsRead(id: Int)                          // PUT /notifications/{id}/read
    suspend fun markAllAsRead()                              // PUT /notifications/read-all
}