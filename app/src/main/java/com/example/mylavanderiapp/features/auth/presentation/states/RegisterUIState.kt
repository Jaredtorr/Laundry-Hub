package com.example.mylavanderiapp.features.auth.presentation.states

sealed class RegisterUIState {
    object Idle : RegisterUIState()
    object Loading : RegisterUIState()
    data class Success(val message: String) : RegisterUIState()
    data class Error(val message: String) : RegisterUIState()
}

data class RegisterFormState(
    val fullName: String = "",
    val email: String = "",
    val password: String = "",
    val rememberMe: Boolean = false,
    val fullNameError: String? = null,
    val emailError: String? = null,
    val passwordError: String? = null
)