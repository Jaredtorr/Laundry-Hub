package com.example.mylavanderiapp.features.notifications.di

import com.example.mylavanderiapp.features.notifications.data.repositories.NotificationsRepositoryImpl
import com.example.mylavanderiapp.features.notifications.domain.repositories.INotificationsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class NotificationsRepositoryModule {

    @Binds
    @Singleton
    abstract fun bindNotificationsRepository(
        impl: NotificationsRepositoryImpl
    ): INotificationsRepository
}