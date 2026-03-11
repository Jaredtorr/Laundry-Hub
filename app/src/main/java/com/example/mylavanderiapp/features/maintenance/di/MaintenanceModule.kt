package com.example.mylavanderiapp.features.maintenance.di

import android.content.Context
import androidx.room.Room
import com.example.mylavanderiapp.core.database.AppDatabase
import com.example.mylavanderiapp.core.database.dao.MaintenanceDao
import com.example.mylavanderiapp.features.maintenance.data.repositories.MaintenanceRepositoryImpl
import com.example.mylavanderiapp.features.maintenance.domain.repositories.MaintenanceRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MaintenanceModule {

    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ): AppDatabase = Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        "laundry_db"
    ).build()

    @Provides
    @Singleton
    fun provideMaintenanceDao(
        database: AppDatabase
    ): MaintenanceDao = database.maintenanceDao()

    @Provides
    @Singleton
    fun provideMaintenanceRepository(
        dao: MaintenanceDao
    ): MaintenanceRepository = MaintenanceRepositoryImpl(dao)
}