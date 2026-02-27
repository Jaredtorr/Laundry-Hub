package com.example.mylavanderiapp.features.auth.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mylavanderiapp.core.network.WebSocketManager
import com.example.mylavanderiapp.features.auth.domain.usecases.LoginUseCase
import com.example.mylavanderiapp.features.auth.presentation.states.LoginFormState
import com.example.mylavanderiapp.features.auth.presentation.states.LoginUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val webSocketManager: WebSocketManager
) : ViewModel() {

    private val _uiState = MutableStateFlow<LoginUIState>(LoginUIState.Idle)
    val uiState: StateFlow<LoginUIState> = _uiState.asStateFlow()

    private val _formState = MutableStateFlow(LoginFormState())
    val formState: StateFlow<LoginFormState> = _formState.asStateFlow()

    fun onEmailChange(email: String) {
        _formState.value = _formState.value.copy(email = email, emailError = null)
    }

    fun onPasswordChange(password: String) {
        _formState.value = _formState.value.copy(password = password, passwordError = null)
    }

    fun onRememberMeChange(rememberMe: Boolean) {
        _formState.value = _formState.value.copy(rememberMe = rememberMe)
    }

    fun login() {
        if (!validateForm()) return

        viewModelScope.launch {
            _uiState.value = LoginUIState.Loading
            val result = loginUseCase(
                email = _formState.value.email,
                password = _formState.value.password
            )
            result.fold(
                onSuccess = { user ->
                    webSocketManager.connect()
                    _uiState.value = LoginUIState.Success(user)
                },
                onFailure = { e -> _uiState.value = LoginUIState.Error(e.message ?: "Error al iniciar sesión") }
            )
        }
    }

    private fun validateForm(): Boolean {
        val email = _formState.value.email
        val password = _formState.value.password
        var isValid = true

        if (email.isBlank()) {
            _formState.value = _formState.value.copy(emailError = "El email es requerido")
            isValid = false
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _formState.value = _formState.value.copy(emailError = "Email inválido")
            isValid = false
        }

        if (password.isBlank()) {
            _formState.value = _formState.value.copy(passwordError = "La contraseña es requerida")
            isValid = false
        } else if (password.length < 6) {
            _formState.value = _formState.value.copy(passwordError = "La contraseña debe tener al menos 6 caracteres")
            isValid = false
        }

        return isValid
    }

    fun resetState() {
        _uiState.value = LoginUIState.Idle
    }
}