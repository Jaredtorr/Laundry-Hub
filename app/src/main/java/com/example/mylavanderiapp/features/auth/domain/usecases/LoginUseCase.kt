package com.example.mylavanderiapp.features.auth.domain.usecases

import com.example.mylavanderiapp.features.auth.domain.entities.User
import com.example.mylavanderiapp.features.auth.domain.repositories.AuthRepository

class LoginUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(
        email: String,
        password: String
    ): User {
        // Validaciones
        require(email.isNotBlank()) { "El email no puede estar vacío" }
        require(password.isNotBlank()) { "La contraseña no puede estar vacía" }
        require(password.length >= 6) { "La contraseña debe tener al menos 6 caracteres" }

        return authRepository.login(email, password)
    }
}