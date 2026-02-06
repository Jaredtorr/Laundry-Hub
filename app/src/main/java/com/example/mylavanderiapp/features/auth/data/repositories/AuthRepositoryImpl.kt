package com.example.mylavanderiapp.features.auth.data.repositories

import android.content.Context
import com.example.mylavanderiapp.core.network.LaundryApi
import com.example.mylavanderiapp.core.network.LoginRequest
import com.example.mylavanderiapp.core.network.RegisterRequest
import com.example.mylavanderiapp.features.auth.domain.entities.User
import com.example.mylavanderiapp.features.auth.domain.entities.UserRole
import com.example.mylavanderiapp.features.auth.domain.repositories.AuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Implementación del repositorio de autenticación con persistencia de Token
 */
class AuthRepositoryImpl(
    private val api: LaundryApi,
    private val context: Context // Necesario para SharedPreferences
) : AuthRepository {

    private val sharedPreferences = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)

    override suspend fun register(name: String, email: String, password: String): User = withContext(Dispatchers.IO) {
        try {
            val response = api.register(
                RegisterRequest(name = name, email = email, password = password)
            )

            if (response.success && response.data != null) {
                val data = response.data

                // Guardamos el token si viene en la respuesta
                data.token?.let { saveToken(it) }

                User(
                    id = data.id,
                    name = data.name,
                    email = data.email,
                    role = mapRole(data.role),
                    token = data.token,
                    createdAt = data.createdAt
                )
            } else {
                throw Exception(response.message ?: "Error al registrar usuario")
            }
        } catch (e: Exception) {
            throw Exception("Error de conexión: ${e.message}")
        }
    }

    override suspend fun login(email: String, password: String): User = withContext(Dispatchers.IO) {
        try {
            val response = api.login(
                LoginRequest(email = email, password = password)
            )

            if (response.success && response.data != null) {
                val data = response.data

                // PERSISTENCIA: Guardamos el token en el dispositivo
                data.token?.let { saveToken(it) }

                User(
                    id = data.id,
                    name = data.name,
                    email = data.email,
                    role = mapRole(data.role),
                    token = data.token,
                    createdAt = data.createdAt
                )
            } else {
                throw Exception(response.message ?: "Credenciales inválidas")
            }
        } catch (e: Exception) {
            throw Exception("Error de conexión: ${e.message}")
        }
    }

    override suspend fun logout() {
        withContext(Dispatchers.IO) {
            // Limpiamos el token del almacenamiento
            sharedPreferences.edit().remove("auth_token").apply()
        }
    }

    override suspend fun getCurrentUser(): User? {
        // Podrías implementar lógica para recuperar el usuario de la DB local o del token
        return null
    }

    // --- Funciones de Ayuda ---

    private fun saveToken(token: String) {
        sharedPreferences.edit().putString("auth_token", token).apply()
    }

    private fun mapRole(role: String): UserRole {
        return when (role.uppercase()) {
            "ADMIN" -> UserRole.ADMIN
            "USER" -> UserRole.USER
            else -> UserRole.USER
        }
    }
}