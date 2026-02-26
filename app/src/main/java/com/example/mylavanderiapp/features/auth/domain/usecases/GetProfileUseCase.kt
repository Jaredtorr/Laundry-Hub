package com.example.mylavanderiapp.features.auth.domain.usecases

import com.example.mylavanderiapp.features.auth.data.datasource.remote.mapper.toDomain
import com.example.mylavanderiapp.features.auth.domain.entities.User
import com.example.mylavanderiapp.features.auth.domain.repositories.IAuthRepository
import javax.inject.Inject

class GetProfileUseCase @Inject constructor(
    private val repository: IAuthRepository
) {
    suspend operator fun invoke(): Result<User> {
        return try {
            val response = repository.getProfile()
            Result.success(response.user.toDomain())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}