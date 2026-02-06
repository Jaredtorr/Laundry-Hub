package com.example.mylavanderiapp.core.di

import android.content.Context
import com.example.mylavanderiapp.core.network.AuthInterceptor
import com.example.mylavanderiapp.core.network.LaundryApi
import com.example.mylavanderiapp.features.auth.di.AuthModule
import com.example.mylavanderiapp.features.machines.di.MachinesModule
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Contenedor de dependencias de la aplicación
 * Maneja el contexto para la persistencia del token.
 */
class AppContainer(private val context: Context) {

    companion object {
        private const val BASE_URL = "http://192.168.0.43:3000/api/"
    }

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    // Interceptor que inyecta el token en cada petición
    private val authInterceptor = AuthInterceptor(context)

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(authInterceptor)
        .addInterceptor(loggingInterceptor)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private val api: LaundryApi by lazy {
        retrofit.create(LaundryApi::class.java)
    }

    // Pasamos el contexto al AuthModule para que el repositorio guarde el token
    val authModule: AuthModule by lazy {
        AuthModule(api, context)
    }

    // Si MachinesModule necesitara el token (vía interceptor),
    // ya está cubierto porque usa la misma instancia de 'api' configurada con okHttpClient.
    val machinesModule: MachinesModule by lazy {
        MachinesModule(api)
    }
}