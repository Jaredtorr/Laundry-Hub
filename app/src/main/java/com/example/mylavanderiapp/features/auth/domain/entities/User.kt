package com.example.mylavanderiapp.features.auth.domain.entities

data class User(
    val id: Int,
    val name: String,
    val secondName: String?,
    val paternalSurname: String,
    val maternalSurname: String?,
    val email: String,
    val imageProfile: String?,
    val oauthProvider: String,
    val role: String,
    val createdAt: String,
    val updatedAt: String
)
