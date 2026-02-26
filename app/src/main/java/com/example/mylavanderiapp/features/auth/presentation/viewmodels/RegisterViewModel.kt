package com.example.mylavanderiapp.features.auth.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mylavanderiapp.features.auth.domain.usecases.RegisterUseCase
import com.example.mylavanderiapp.features.auth.presentation.states.RegisterFormState
import com.example.mylavanderiapp.features.auth.presentation.states.RegisterUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<RegisterUIState>(RegisterUIState.Idle)
    val uiState: StateFlow<RegisterUIState> = _uiState.asStateFlow()

    private val _formState = MutableStateFlow(RegisterFormState())
    val formState: StateFlow<RegisterFormState> = _formState.asStateFlow()

    fun onFullNameChange(fullName: String) {
        _formState.value = _formState.value.copy(fullName = fullName, fullNameError = null)
    }

    fun onEmailChange(email: String) {
        _formState.value = _formState.value.copy(email = email, emailError = null)
    }

    fun onPasswordChange(password: String) {
        _formState.value = _formState.value.copy(password = password, passwordError = null)
    }

    fun onRememberMeChange(rememberMe: Boolean) {
        _formState.value = _formState.value.copy(rememberMe = rememberMe)
    }

    fun register() {
        if (!validateForm()) return

        viewModelScope.launch {
            _uiState.value = RegisterUIState.Loading
            val result = registerUseCase(
                name = _formState.value.fullName,
                paternalSurname = "",
                email = _formState.value.email,
                password = _formState.value.password
            )
            result.fold(
                onSuccess = { user -> _uiState.value = RegisterUIState.Success("¡Cuenta creada para ${user.name}!") },
                onFailure = { e -> _uiState.value = RegisterUIState.Error(e.message ?: "Error al registrar") }
            )
        }
    }

    private fun validateForm(): Boolean {
        val currentState = _formState.value
        var isValid = true

        if (currentState.fullName.isBlank()) {
            _formState.value = currentState.copy(fullNameError = "El nombre es requerido")
            isValid = false
        } else if (currentState.fullName.length < 3) {
            _formState.value = currentState.copy(fullNameError = "El nombre debe tener al menos 3 caracteres")
            isValid = false
        }

        if (currentState.email.isBlank()) {
            _formState.value = _formState.value.copy(emailError = "El email es requerido")
            isValid = false
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(currentState.email).matches()) {
            _formState.value = _formState.value.copy(emailError = "Email inválido")
            isValid = false
        }

        if (currentState.password.isBlank()) {
            _formState.value = _formState.value.copy(passwordError = "La contraseña es requerida")
            isValid = false
        } else if (currentState.password.length < 6) {
            _formState.value = _formState.value.copy(passwordError = "La contraseña debe tener al menos 6 caracteres")
            isValid = false
        }

        return isValid
    }

    fun resetState() {
        _uiState.value = RegisterUIState.Idle
    }
}