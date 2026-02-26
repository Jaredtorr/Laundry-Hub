package com.example.mylavanderiapp.features.auth.data.datasource.remote.model

data class RegisterRequest(
    val name: String,
    val secondName: String? = null,
    val paternalSurname: String,
    val maternalSurname: String? = null,
    val email: String,
    val password: String,
    val imageProfile: String? = null
)