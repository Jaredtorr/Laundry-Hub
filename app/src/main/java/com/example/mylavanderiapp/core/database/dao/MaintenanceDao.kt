package com.example.mylavanderiapp.core.database.dao

import androidx.room.*
import com.example.mylavanderiapp.core.database.entities.MaintenanceRecordEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MaintenanceDao {

    @Query("SELECT * FROM maintenance_records ORDER BY id DESC")
    fun getAll(): Flow<List<MaintenanceRecordEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(record: MaintenanceRecordEntity)

    @Query("UPDATE maintenance_records SET isResolved = 1 WHERE id = :id")
    suspend fun resolve(id: Int)

    @Query("DELETE FROM maintenance_records WHERE id = :id")
    suspend fun delete(id: Int)
}