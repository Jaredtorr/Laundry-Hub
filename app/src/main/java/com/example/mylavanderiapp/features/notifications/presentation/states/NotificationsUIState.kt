package com.example.mylavanderiapp.features.notifications.presentation.states

import com.example.mylavanderiapp.features.notifications.domain.entities.AppNotification

sealed class NotificationsUIState {
    data object Idle    : NotificationsUIState()
    data object Loading : NotificationsUIState()
    data class Success(val notifications: List<AppNotification>) : NotificationsUIState()
    data class Error(val message: String) : NotificationsUIState()
}