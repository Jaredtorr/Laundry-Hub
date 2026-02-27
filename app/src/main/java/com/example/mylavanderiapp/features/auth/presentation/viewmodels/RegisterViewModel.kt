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

    fun onNameChange(name: String) {
        _formState.value = _formState.value.copy(name = name, nameError = null)
    }

    fun onPaternalSurnameChange(paternalSurname: String) {
        _formState.value = _formState.value.copy(paternalSurname = paternalSurname, paternalSurnameError = null)
    }

    fun onEmailChange(email: String) {
        _formState.value = _formState.value.copy(email = email, emailError = null)
    }

    fun onPasswordChange(password: String) {
        _formState.value = _formState.value.copy(password = password, passwordError = null)
    }

    fun register() {
        if (!validateForm()) return

        viewModelScope.launch {
            _uiState.value = RegisterUIState.Loading
            val result = registerUseCase(
                name = _formState.value.name,
                paternalSurname = _formState.value.paternalSurname,
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
        val s = _formState.value
        var isValid = true

        if (s.name.isBlank()) {
            _formState.value = s.copy(nameError = "El nombre es requerido")
            isValid = false
        } else if (s.name.length < 3) {
            _formState.value = s.copy(nameError = "El nombre debe tener al menos 3 caracteres")
            isValid = false
        }

        if (s.paternalSurname.isBlank()) {
            _formState.value = _formState.value.copy(paternalSurnameError = "El apellido paterno es requerido")
            isValid = false
        }

        if (s.email.isBlank()) {
            _formState.value = _formState.value.copy(emailError = "El email es requerido")
            isValid = false
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(s.email).matches()) {
            _formState.value = _formState.value.copy(emailError = "Email inválido")
            isValid = false
        }

        if (s.password.isBlank()) {
            _formState.value = _formState.value.copy(passwordError = "La contraseña es requerida")
            isValid = false
        } else if (s.password.length < 6) {
            _formState.value = _formState.value.copy(passwordError = "Mínimo 6 caracteres")
            isValid = false
        }

        return isValid
    }

    fun resetState() {
        _uiState.value = RegisterUIState.Idle
    }
}