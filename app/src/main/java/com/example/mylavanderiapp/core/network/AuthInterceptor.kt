package com.example.mylavanderiapp.core.network

import android.content.Context
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private val context: Context) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        // Leemos las preferencias dentro del intercept para obtener siempre el valor más reciente
        val prefs = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
        val token = prefs.getString("auth_token", null)

        val requestBuilder = chain.request().newBuilder()

        if (!token.isNullOrBlank()) {
            requestBuilder.addHeader("Authorization", "Bearer $token")
            println("NETWORK_DEBUG: Enviando Token -> $token")
        } else {
            println("NETWORK_DEBUG: No se encontró token en SharedPreferences")
        }

        return chain.proceed(requestBuilder.build())
    }
}