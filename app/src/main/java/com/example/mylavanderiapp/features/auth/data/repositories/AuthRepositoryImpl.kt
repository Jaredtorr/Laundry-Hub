package com.example.mylavanderiapp.features.auth.data.repositories

import com.example.mylavanderiapp.features.auth.domain.entities.User
import com.example.mylavanderiapp.features.auth.domain.repositories.AuthRepository
import kotlinx.coroutines.delay

class AuthRepositoryImpl : AuthRepository {

    override suspend fun register(name: String, email: String, password: String): User {
        // Simulación de llamada a API (2 segundos de delay)
        delay(2000)

        // Por ahora retornamos un usuario simulado
        return User(
            id = "user_${System.currentTimeMillis()}",
            name = name,
            email = email,
            phone = null,
            token = "fake_token_${System.currentTimeMillis()}",
            createdAt = System.currentTimeMillis().toString()
        )
    }

    override suspend fun login(email: String, password: String): User {
        delay(2000)
        return User(
            id = "user_123",
            name = "Usuario Demo",
            email = email,
            phone = null,
            token = "fake_token_login",
            createdAt = System.currentTimeMillis().toString()
        )
    }

    override suspend fun logout() {
        delay(500)
    }

    override suspend fun getCurrentUser(): User? {
        return null
    }
}