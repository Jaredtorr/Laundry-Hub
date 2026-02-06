package com.example.mylavanderiapp.features.auth.domain.entities

data class User(
    val id: String,
    val name: String,
    val email: String,
    val role: UserRole = UserRole.USER,
    val phone: String? = null,
    val token: String? = null,
    val createdAt: String? = null
)

enum class UserRole {
    USER,
    ADMIN
}