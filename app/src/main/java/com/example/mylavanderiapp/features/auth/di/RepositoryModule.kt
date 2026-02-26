package com.example.mylavanderiapp.features.auth.di

import com.example.mylavanderiapp.features.auth.data.repositories.AuthRepositoryImpl
import com.example.mylavanderiapp.features.auth.domain.repositories.IAuthRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        impl: AuthRepositoryImpl
    ): IAuthRepository
}