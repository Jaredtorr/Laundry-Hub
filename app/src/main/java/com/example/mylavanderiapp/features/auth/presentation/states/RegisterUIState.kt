package com.example.mylavanderiapp.features.auth.presentation.states

import com.example.mylavanderiapp.features.auth.domain.entities.User

sealed class RegisterUIState {
    data object Idle : RegisterUIState()
    data object Loading : RegisterUIState()
    data class Success(val message: String) : RegisterUIState()
    data class GoogleSuccess(val user: User) : RegisterUIState()
    data class Error(val message: String) : RegisterUIState()
}

data class RegisterFormState(
    val name: String = "",
    val nameError: String? = null,
    val paternalSurname: String = "",
    val paternalSurnameError: String? = null,
    val email: String = "",
    val emailError: String? = null,
    val password: String = "",
    val passwordError: String? = null
)