package com.example.mylavanderiapp.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.mylavanderiapp.core.database.dao.MaintenanceDao
import com.example.mylavanderiapp.core.database.entities.MaintenanceRecordEntity

@Database(
    entities = [MaintenanceRecordEntity::class],
    version  = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun maintenanceDao(): MaintenanceDao
}