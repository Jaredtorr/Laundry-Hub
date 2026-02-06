package com.example.mylavanderiapp.features.auth.di

import android.content.Context
import com.example.mylavanderiapp.core.network.LaundryApi
import com.example.mylavanderiapp.features.auth.data.repositories.AuthRepositoryImpl
import com.example.mylavanderiapp.features.auth.domain.repositories.AuthRepository
import com.example.mylavanderiapp.features.auth.domain.usecases.LoginUseCase
import com.example.mylavanderiapp.features.auth.domain.usecases.RegisterUseCase

/**
 * Módulo de inyección de dependencias para el feature de Auth
 */
class AuthModule(
    private val api: LaundryApi,
    private val context: Context
) {

    // Repository: Ahora le pasamos la api y el contexto
    val authRepository: AuthRepository by lazy {
        AuthRepositoryImpl(api, context)
    }

    // Use Cases
    val registerUseCase: RegisterUseCase by lazy {
        RegisterUseCase(authRepository)
    }

    val loginUseCase: LoginUseCase by lazy {
        LoginUseCase(authRepository)
    }
}