package com.example.mylavanderiapp.features.auth.domain.usecases

import com.example.mylavanderiapp.features.auth.data.datasource.remote.mapper.toDomain
import com.example.mylavanderiapp.features.auth.data.datasource.remote.model.RegisterRequest
import com.example.mylavanderiapp.features.auth.domain.entities.User
import com.example.mylavanderiapp.features.auth.domain.repositories.IAuthRepository
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val repository: IAuthRepository
) {
    suspend operator fun invoke(
        name: String,
        paternalSurname: String,
        email: String,
        password: String,
        secondName: String? = null,
        maternalSurname: String? = null
    ): Result<User> {
        return try {
            val response = repository.register(
                RegisterRequest(
                    name = name,
                    paternalSurname = paternalSurname,
                    email = email,
                    password = password,
                    secondName = secondName,
                    maternalSurname = maternalSurname
                )
            )
            Result.success(response.user.toDomain())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}