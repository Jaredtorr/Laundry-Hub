package com.example.mylavanderiapp.features.laundry_reservation.di

import com.example.mylavanderiapp.features.laundry_reservation.data.repositories.ReservationRepositoryImpl
import com.example.mylavanderiapp.features.laundry_reservation.domain.repositories.IReservationRepository
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
    abstract fun bindReservationRepository(
        impl: ReservationRepositoryImpl
    ): IReservationRepository
}