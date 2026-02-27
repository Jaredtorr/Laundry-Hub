package com.example.mylavanderiapp.features.notifications.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mylavanderiapp.features.notifications.domain.usecases.GetMyNotificationsUseCase
import com.example.mylavanderiapp.features.notifications.domain.usecases.MarkAllAsReadUseCase
import com.example.mylavanderiapp.features.notifications.domain.usecases.MarkAsReadUseCase
import com.example.mylavanderiapp.features.notifications.presentation.states.NotificationsUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationsViewModel @Inject constructor(
    private val getMyNotificationsUseCase: GetMyNotificationsUseCase,
    private val markAsReadUseCase: MarkAsReadUseCase,
    private val markAllAsReadUseCase: MarkAllAsReadUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<NotificationsUIState>(NotificationsUIState.Idle)
    val uiState = _uiState.asStateFlow()

    private val _unreadCount = MutableStateFlow(0)
    val unreadCount = _unreadCount.asStateFlow()

    init { loadNotifications() }

    fun loadNotifications() {
        viewModelScope.launch {
            _uiState.value = NotificationsUIState.Loading
            getMyNotificationsUseCase()
                .onSuccess { notifications ->
                    _uiState.value = NotificationsUIState.Success(notifications)
                    _unreadCount.value = notifications.count { !it.isRead }
                }
                .onFailure {
                    _uiState.value = NotificationsUIState.Error(it.message ?: "Error al cargar notificaciones")
                }
        }
    }

    fun markAsRead(id: Int) {
        // Actualización optimista en UI
        val current = (_uiState.value as? NotificationsUIState.Success)?.notifications ?: return
        val updated = current.map { if (it.id == id) it.copy(isRead = true) else it }
        _uiState.value = NotificationsUIState.Success(updated)
        _unreadCount.value = updated.count { !it.isRead }

        viewModelScope.launch {
            markAsReadUseCase(id).onFailure {
                // Revertir si falla
                loadNotifications()
            }
        }
    }

    fun markAllAsRead() {
        // Actualización optimista en UI
        val current = (_uiState.value as? NotificationsUIState.Success)?.notifications ?: return
        _uiState.value = NotificationsUIState.Success(current.map { it.copy(isRead = true) })
        _unreadCount.value = 0

        viewModelScope.launch {
            markAllAsReadUseCase().onFailure {
                // Revertir si falla
                loadNotifications()
            }
        }
    }
}