package com.example.mylavanderiapp.features.auth.domain.repositories

import com.example.mylavanderiapp.features.auth.domain.entities.User

interface AuthRepository {
    suspend fun register(name: String, email: String, password: String): User
    suspend fun login(email: String, password: String): User
    suspend fun logout()
    suspend fun getCurrentUser(): User?
}