package com.example.mylavanderiapp.features.auth.data.datasource.remote.model.dto

data class UserDto(
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