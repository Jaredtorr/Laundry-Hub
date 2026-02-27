package com.example.mylavanderiapp.features.notifications.data.datasources.remote.mapper

import com.example.mylavanderiapp.features.notifications.data.datasources.remote.model.NotificationDto
import com.example.mylavanderiapp.features.notifications.domain.entities.AppNotification
import com.example.mylavanderiapp.features.notifications.domain.entities.NotificationType

fun NotificationDto.toDomain(): AppNotification {
    return AppNotification(
        id            = this.id,
        userId        = this.userId,
        reservationId = this.reservationId,
        message       = this.message,
        type          = mapType(this.type),
        isRead        = this.isRead,
        createdAt     = this.createdAt
    )
}

private fun mapType(type: String): NotificationType {
    return when (type.uppercase()) {
        "RESERVATION" -> NotificationType.RESERVATION
        "AVAILABLE"   -> NotificationType.AVAILABLE
        else          -> NotificationType.OTHER
    }
}