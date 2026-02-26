package com.example.mylavanderiapp.core.network

import com.example.mylavanderiapp.features.auth.data.datasource.remote.model.LoginRequest
import com.example.mylavanderiapp.features.auth.data.datasource.remote.model.RegisterRequest
import com.example.mylavanderiapp.features.auth.data.datasource.remote.model.dto.GoogleTokenRequest
import com.example.mylavanderiapp.features.auth.data.datasource.remote.model.dto.LoginResponse
import com.example.mylavanderiapp.features.auth.data.datasource.remote.model.dto.MessageResponse
import com.example.mylavanderiapp.features.auth.data.datasource.remote.model.dto.UpdateUserRequest
import com.example.mylavanderiapp.features.auth.data.datasource.remote.model.dto.UserResponse
import com.example.mylavanderiapp.features.auth.data.datasource.remote.model.dto.UsersListResponse
import com.example.mylavanderiapp.features.machines.data.datasources.remote.model.CreateMachineDto
import com.example.mylavanderiapp.features.machines.data.datasources.remote.model.MachineDetailResponse
import com.example.mylavanderiapp.features.machines.data.datasources.remote.model.MachinesListResponse
import com.example.mylavanderiapp.features.machines.data.datasources.remote.model.UpdateMachineDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface LaundryApi {
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse

    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest): UserResponse

    @POST("auth/logout")
    suspend fun logout(): MessageResponse

    @POST("auth/refresh")
    suspend fun refreshToken(): MessageResponse

    @GET("auth/profile")
    suspend fun getProfile(): UserResponse

    @GET("auth/verify")
    suspend fun verifyToken(): MessageResponse

    @POST("auth/google/mobile")
    suspend fun googleMobileLogin(@Body request: GoogleTokenRequest): LoginResponse

    // ==================== USERS ====================
    @GET("users")
    suspend fun getAllUsers(): UsersListResponse

    @GET("users/{id}")
    suspend fun getUserById(@Path("id") id: Int): UserResponse

    @PUT("users/{id}")
    suspend fun updateUser(@Path("id") id: Int, @Body request: UpdateUserRequest): MessageResponse

    @DELETE("users/{id}")
    suspend fun deleteUser(@Path("id") id: Int): MessageResponse

    // ==================== MACHINES ====================
    @GET("machines")
    suspend fun getAllMachines(): MachinesListResponse

    @GET("machines/{id}")
    suspend fun getMachineById(@Path("id") id: Int): MachineDetailResponse

    @POST("machines")
    suspend fun createMachine(@Body request: CreateMachineDto): MachineDetailResponse

    @PUT("machines/{id}")
    suspend fun updateMachine(@Path("id") id: Int, @Body request: UpdateMachineDto): MachineDetailResponse

    @DELETE("machines/{id}")
    suspend fun deleteMachine(@Path("id") id: Int): MessageResponse

    // ==================== RESERVATIONS ====================
    // @GET("reservations/my")
    // suspend fun getMyReservations(): ReservationsListResponse

    // @GET("reservations/{id}")
    // suspend fun getReservationById(@Path("id") id: Int): ReservationResponse

    // @POST("reservations")
    // suspend fun createReservation(@Body request: CreateReservationRequest): ReservationResponse

    @PUT("reservations/{id}/cancel")
    suspend fun cancelReservation(@Path("id") id: Int): MessageResponse

    @PUT("reservations/{id}/complete")
    suspend fun completeReservation(@Path("id") id: Int): MessageResponse

    // ==================== NOTIFICATIONS ====================
    // @GET("notifications/my")
    // suspend fun getMyNotifications(): NotificationsListResponse

    @PUT("notifications/{id}/read")
    suspend fun markAsRead(@Path("id") id: Int): MessageResponse

    @PUT("notifications/read-all")
    suspend fun markAllAsRead(): MessageResponse
}