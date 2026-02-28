package com.example.mylavanderiapp.features.auth.domain.usecases

import com.example.mylavanderiapp.features.auth.domain.entities.User
import com.example.mylavanderiapp.features.auth.domain.repositories.IAuthRepository
import javax.inject.Inject

class GoogleLoginUseCase @Inject constructor(
    private val authRepository: IAuthRepository
) {
    suspend operator fun invoke(idToken: String): Result<User> {
        return authRepository.googleLogin(idToken)
    }
}