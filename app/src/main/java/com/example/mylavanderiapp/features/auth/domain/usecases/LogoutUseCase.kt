package com.example.mylavanderiapp.features.auth.domain.usecases

import com.example.mylavanderiapp.features.auth.domain.repositories.IAuthRepository
import javax.inject.Inject

class LogoutUseCase @Inject constructor(
    private val repository: IAuthRepository
) {
    suspend operator fun invoke(): Result<String> {
        return try {
            val response = repository.logout()
            Result.success(response.message)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}