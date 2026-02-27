package com.example.mylavanderiapp.features.notifications.data.datasources.remote.model

data class NotificationDto(
    val id: Int,
    val userId: Int,
    val reservationId: Int?,
    val message: String,
    val type: String,
    val isRead: Boolean,
    val createdAt: String
)

data class NotificationsListResponse(
    val notifications: List<NotificationDto>?
)

data class NotificationMessageResponse(
    val message: String
)