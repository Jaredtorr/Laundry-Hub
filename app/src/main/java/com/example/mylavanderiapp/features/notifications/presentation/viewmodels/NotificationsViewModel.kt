package com.example.mylavanderiapp.features.notifications.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mylavanderiapp.core.network.WebSocketManager
import com.example.mylavanderiapp.features.notifications.domain.entities.AppNotification
import com.example.mylavanderiapp.features.notifications.domain.entities.NotificationType
import com.example.mylavanderiapp.features.notifications.domain.usecases.GetMyNotificationsUseCase
import com.example.mylavanderiapp.features.notifications.domain.usecases.MarkAllAsReadUseCase
import com.example.mylavanderiapp.features.notifications.domain.usecases.MarkAsReadUseCase
import com.example.mylavanderiapp.features.notifications.presentation.states.NotificationsUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationsViewModel @Inject constructor(
    private val getMyNotificationsUseCase : GetMyNotificationsUseCase,
    private val markAsReadUseCase         : MarkAsReadUseCase,
    private val markAllAsReadUseCase      : MarkAllAsReadUseCase,
    private val webSocketManager          : WebSocketManager
) : ViewModel() {

    private val _uiState = MutableStateFlow<NotificationsUIState>(NotificationsUIState.Idle)
    val uiState: StateFlow<NotificationsUIState> = _uiState.asStateFlow()

    private val _unreadCount = MutableStateFlow(0)
    val unreadCount: StateFlow<Int> = _unreadCount.asStateFlow()

    private val _hasUnread = MutableStateFlow(false)
    val hasUnread: StateFlow<Boolean> = _hasUnread.asStateFlow()

    init {
        loadNotifications()
        collectWebSocketNotifications()
    }

    private fun collectWebSocketNotifications() {
        viewModelScope.launch {
            webSocketManager.notifications.collect { wsNotification ->
                if (wsNotification.type == "MACHINE_STATUS_CHANGED") return@collect

                val type = when (wsNotification.type) {
                    "RESERVATION_CREATED", "NEW_RESERVATION" -> NotificationType.RESERVATION
                    "AVAILABLE"                              -> NotificationType.AVAILABLE
                    else                                     -> NotificationType.OTHER
                }

                val newNotification = AppNotification(
                    id            = wsNotification.id,
                    userId        = 0,
                    reservationId = wsNotification.reservationId,
                    message       = wsNotification.message,
                    type          = type,
                    isRead        = false,
                    createdAt     = System.currentTimeMillis().toString()
                )

                val current = (_uiState.value as? NotificationsUIState.Success)?.notifications ?: emptyList()
                updateNotifications(listOf(newNotification) + current)
            }
        }
    }

    fun loadNotifications() {
        viewModelScope.launch {
            _uiState.value = NotificationsUIState.Loading
            getMyNotificationsUseCase()
                .onSuccess { updateNotifications(it) }
                .onFailure { _uiState.value = NotificationsUIState.Error(it.message ?: "Error al cargar notificaciones") }
        }
    }

    fun markAsRead(id: Int) {
        val current = (_uiState.value as? NotificationsUIState.Success)?.notifications ?: return
        val updated = current.map { if (it.id == id) it.copy(isRead = true) else it }
        updateNotifications(updated)

        viewModelScope.launch {
            markAsReadUseCase(id).onFailure { loadNotifications() }
        }
    }

    fun markAllAsRead() {
        val current = (_uiState.value as? NotificationsUIState.Success)?.notifications ?: return
        updateNotifications(current.map { it.copy(isRead = true) })

        viewModelScope.launch {
            markAllAsReadUseCase().onFailure { loadNotifications() }
        }
    }

    private fun updateNotifications(notifications: List<AppNotification>) {
        _uiState.value     = NotificationsUIState.Success(notifications)
        _unreadCount.value = notifications.count { !it.isRead }
        _hasUnread.value   = notifications.any { !it.isRead }
    }
}