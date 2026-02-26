package com.example.mylavanderiapp.core.di

import com.example.mylavanderiapp.BuildConfig
import com.example.mylavanderiapp.core.network.CookieJarManager
import com.example.mylavanderiapp.core.network.LaundryApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.CookieJar
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideCookieJar(): CookieJar {
        return CookieJarManager()
    }

    @Provides
    @Singleton
    @LaundryWebSocket
    fun provideOkHttpClient(
        cookieJar: CookieJar
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .cookieJar(cookieJar)
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    @LaundryRetrofit
    fun provideRetrofit(
        @LaundryWebSocket okHttpClient: OkHttpClient
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideLaundryApi(
        @LaundryRetrofit retrofit: Retrofit
    ): LaundryApi {
        return retrofit.create(LaundryApi::class.java)
    }
}
