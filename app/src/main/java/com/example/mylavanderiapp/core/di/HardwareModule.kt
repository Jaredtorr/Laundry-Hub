package com.example.mylavanderiapp.core.di

import com.example.mylavanderiapp.core.hardware.data.AndroidNotificationAlerter
import com.example.mylavanderiapp.core.hardware.domain.NotificationAlerter
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class HardwareModule {

    @Binds
    @Singleton
    abstract fun bindNotificationAlerter(
        impl: AndroidNotificationAlerter
    ): NotificationAlerter
}