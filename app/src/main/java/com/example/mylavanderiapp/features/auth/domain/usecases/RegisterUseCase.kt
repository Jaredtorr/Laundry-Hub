package com.example.mylavanderiapp.features.auth.domain.usecases

import com.example.mylavanderiapp.features.auth.domain.entities.User
import com.example.mylavanderiapp.features.auth.domain.repositories.AuthRepository

class RegisterUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(
        name: String,
        email: String,
        password: String
    ): User {
        // Validaciones adicionales si es necesario
        require(name.isNotBlank()) { "El nombre no puede estar vacío" }
        require(email.isNotBlank()) { "El email no puede estar vacío" }
        require(password.length >= 6) { "La contraseña debe tener al menos 6 caracteres" }

        return authRepository.register(name, email, password)
    }
}