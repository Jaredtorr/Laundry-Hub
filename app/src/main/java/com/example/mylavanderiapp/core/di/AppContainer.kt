package com.example.mylavanderiapp.core.di

import com.example.mylavanderiapp.features.auth.data.repositories.AuthRepositoryImpl
import com.example.mylavanderiapp.features.auth.domain.repositories.AuthRepository
import com.example.mylavanderiapp.features.auth.domain.usecases.RegisterUseCase

class AppContainer {

    // Repositories
    val authRepository: AuthRepository by lazy {
        AuthRepositoryImpl()
    }

    // Use Cases
    val registerUseCase: RegisterUseCase by lazy {
        RegisterUseCase(authRepository)
    }
}