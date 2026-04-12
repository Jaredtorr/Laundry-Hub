package com.example.mylavanderiapp.features.auth.domain.usecases

import com.example.mylavanderiapp.features.auth.domain.repositories.IAuthRepository
import javax.inject.Inject

class RegisterFCMTokenUseCase @Inject constructor(
    private val repository: IAuthRepository
) {
    suspend operator fun invoke(userId: Int, fcmToken: String): Result<Unit> {
        return try {
            repository.registerFCMToken(userId, fcmToken)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}