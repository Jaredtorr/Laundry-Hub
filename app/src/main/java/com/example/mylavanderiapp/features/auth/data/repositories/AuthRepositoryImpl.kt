package com.example.mylavanderiapp.features.auth.data.repositories

import com.example.mylavanderiapp.core.network.LaundryApi
import com.example.mylavanderiapp.features.auth.data.datasource.remote.mapper.toDomain
import com.example.mylavanderiapp.features.auth.data.datasource.remote.model.LoginRequest
import com.example.mylavanderiapp.features.auth.data.datasource.remote.model.RegisterRequest
import com.example.mylavanderiapp.features.auth.data.datasource.remote.model.dto.LoginResponse
import com.example.mylavanderiapp.features.auth.data.datasource.remote.model.dto.MessageResponse
import com.example.mylavanderiapp.features.auth.data.datasource.remote.model.dto.UpdateUserRequest
import com.example.mylavanderiapp.features.auth.data.datasource.remote.model.dto.UserResponse
import com.example.mylavanderiapp.features.auth.data.datasource.remote.model.dto.UsersListResponse
import com.example.mylavanderiapp.features.auth.domain.repositories.IAuthRepository
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val api: LaundryApi
) : IAuthRepository {

    override suspend fun login(request: LoginRequest): LoginResponse {
        return api.login(request)
    }

    override suspend fun register(request: RegisterRequest): UserResponse {
        fun String.toRequestBody() = toRequestBody("text/plain".toMediaType())
        return api.register(
            name = request.name.toRequestBody(),
            paternalSurname = request.paternalSurname.toRequestBody(),
            email = request.email.toRequestBody(),
            password = request.password.toRequestBody()
        )
    }

    override suspend fun logout(): MessageResponse {
        return api.logout()
    }

    override suspend fun refreshToken(): MessageResponse {
        return api.refreshToken()
    }

    override suspend fun getProfile(): UserResponse {
        return api.getProfile()
    }

    override suspend fun verifyToken(): MessageResponse {
        return api.verifyToken()
    }

    override suspend fun getAllUsers(): UsersListResponse {
        return api.getAllUsers()
    }

    override suspend fun getUserById(id: Int): UserResponse {
        return api.getUserById(id)
    }

    override suspend fun updateUser(id: Int, request: UpdateUserRequest): MessageResponse {
        return api.updateUser(id, request)
    }

    override suspend fun deleteUser(id: Int): MessageResponse {
        return api.deleteUser(id)
    }

    override suspend fun googleLogin(idToken: String): Result<com.example.mylavanderiapp.features.auth.domain.entities.User> {
        return try {
            val response = api.googleMobileLogin(
                com.example.mylavanderiapp.features.auth.data.datasource.remote.model.dto.GoogleTokenRequest(idToken)
            )
            Result.success(response.user.toDomain())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}