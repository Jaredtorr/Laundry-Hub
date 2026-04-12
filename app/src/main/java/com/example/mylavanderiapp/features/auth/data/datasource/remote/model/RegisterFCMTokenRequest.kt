package com.example.mylavanderiapp.features.auth.data.datasource.remote.model

data class RegisterFCMTokenRequest(
    val userId: Int,
    val token: String
)