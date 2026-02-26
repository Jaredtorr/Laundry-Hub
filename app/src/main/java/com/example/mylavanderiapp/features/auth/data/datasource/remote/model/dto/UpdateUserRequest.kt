package com.example.mylavanderiapp.features.auth.data.datasource.remote.model.dto

data class UpdateUserRequest(
    val name: String,
    val secondName: String? = null,
    val paternalSurname: String,
    val maternalSurname: String? = null,
    val email: String,
    val imageProfile: String? = null
)