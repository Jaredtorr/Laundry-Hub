package com.example.mylavanderiapp.features.machines.di

import com.example.mylavanderiapp.features.machines.data.repositories.MachinesRepositoryImpl
import com.example.mylavanderiapp.features.machines.domain.repositories.IMachinesRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class MachinesRepositoryModule {

    @Binds
    @Singleton
    abstract fun bindMachinesRepository(
        impl: MachinesRepositoryImpl
    ): IMachinesRepository
}