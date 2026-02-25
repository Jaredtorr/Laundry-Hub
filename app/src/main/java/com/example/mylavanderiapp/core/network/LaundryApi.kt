package com.example.mylavanderiapp.core.network

import retrofit2.http.*


interface LaundryApi {

    @POST("auth/register")
    suspend fun register(
        @Body request: RegisterRequest
    ): AuthResponse

    @POST("auth/login")
    suspend fun login(
        @Body request: LoginRequest
    ): AuthResponse

    @POST("auth/logout")
    suspend fun logout(
        @Header("Authorization") token: String
    )

    @GET("auth/me")
    suspend fun getCurrentUser(
        @Header("Authorization") token: String
    ): UserResponse

    // ============ MACHINES ENDPOINTS ============

    @GET("machines")
    suspend fun getMachines(
        @Header("Authorization") token: String? = null
    ): MachinesResponse

    @GET("machines/{id}")
    suspend fun getMachineById(
        @Path("id") id: String,
        @Header("Authorization") token: String? = null
    ): MachineResponse

    @POST("machines")
    suspend fun createMachine(
        @Header("Authorization") token: String,
        @Body request: CreateMachineRequest
    ): MachineResponse

    @PUT("machines/{id}")
    suspend fun updateMachine(
        @Path("id") id: String,
        @Header("Authorization") token: String,
        @Body request: UpdateMachineRequest
    ): MachineResponse

    @DELETE("machines/{id}")
    suspend fun deleteMachine(
        @Path("id") id: String,
        @Header("Authorization") token: String
    ): DeleteResponse
}

// ============ REQUEST MODELS ============

data class RegisterRequest(
    val name: String,
    val email: String,
    val password: String
)

data class LoginRequest(
    val email: String,
    val password: String
)

data class CreateMachineRequest(
    val name: String,
    val capacity: String,
    val location: String,
    val status: String
)

data class UpdateMachineRequest(
    val name: String?,
    val capacity: String?,
    val location: String?,
    val status: String?
)

// ============ RESPONSE MODELS ============

data class AuthResponse(
    val success: Boolean,
    val data: UserData?,
    val message: String?
)

data class UserData(
    val id: String,
    val name: String,
    val email: String,
    val role: String,
    val token: String,
    val createdAt: String
)

data class UserResponse(
    val success: Boolean,
    val data: UserData?,
    val message: String?
)

data class MachinesResponse(
    val success: Boolean,
    val data: List<MachineData>,
    val message: String?
)

data class MachineResponse(
    val success: Boolean,
    val data: MachineData?,
    val message: String?
)

data class MachineData(
    val id: String,
    val name: String,
    val status: String,
    val capacity: String,
    val location: String,
    val createdAt: String?,
    val updatedAt: String?
)

data class DeleteResponse(
    val success: Boolean,
    val message: String
)