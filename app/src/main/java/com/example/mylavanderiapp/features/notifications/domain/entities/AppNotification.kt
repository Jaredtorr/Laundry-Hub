package com.example.mylavanderiapp.features.notifications.domain.entities

data class AppNotification(
    val id: Int,
    val userId: Int,
    val reservationId: Int?,
    val message: String,
    val type: NotificationType,
    val isRead: Boolean,
    val createdAt: String
)

enum class NotificationType {
    RESERVATION,
    AVAILABLE,
    OTHER
}