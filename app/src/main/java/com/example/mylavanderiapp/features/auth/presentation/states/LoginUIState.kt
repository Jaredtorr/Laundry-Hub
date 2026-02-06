package com.example.mylavanderiapp.features.auth.presentation.states

import com.example.mylavanderiapp.features.auth.domain.entities.User

sealed class LoginUIState {
    data object Idle : LoginUIState()
    data object Loading : LoginUIState()
    data class Success(val user: User) : LoginUIState()
    data class Error(val message: String) : LoginUIState()
}

data class LoginFormState(
    val email: String = "",
    val password: String = "",
    val rememberMe: Boolean = false,
    val emailError: String? = null,
    val passwordError: String? = null
)