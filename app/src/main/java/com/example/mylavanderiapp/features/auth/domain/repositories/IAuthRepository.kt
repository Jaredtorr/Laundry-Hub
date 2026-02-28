package com.example.mylavanderiapp.features.auth.domain.repositories

import com.example.mylavanderiapp.features.auth.data.datasource.remote.model.LoginRequest
import com.example.mylavanderiapp.features.auth.data.datasource.remote.model.RegisterRequest
import com.example.mylavanderiapp.features.auth.data.datasource.remote.model.dto.LoginResponse
import com.example.mylavanderiapp.features.auth.data.datasource.remote.model.dto.MessageResponse
import com.example.mylavanderiapp.features.auth.data.datasource.remote.model.dto.UserResponse
import com.example.mylavanderiapp.features.auth.data.datasource.remote.model.dto.UsersListResponse
import com.example.mylavanderiapp.features.auth.data.datasource.remote.model.dto.UpdateUserRequest

interface IAuthRepository {
    suspend fun login(request: LoginRequest): LoginResponse
    suspend fun register(request: RegisterRequest): UserResponse
    suspend fun logout(): MessageResponse
    suspend fun refreshToken(): MessageResponse
    suspend fun getProfile(): UserResponse
    suspend fun verifyToken(): MessageResponse
    suspend fun getAllUsers(): UsersListResponse
    suspend fun getUserById(id: Int): UserResponse
    suspend fun updateUser(id: Int, request: UpdateUserRequest): MessageResponse
    suspend fun deleteUser(id: Int): MessageResponse
    suspend fun googleLogin(idToken: String): Result<com.example.mylavanderiapp.features.auth.domain.entities.User>
}