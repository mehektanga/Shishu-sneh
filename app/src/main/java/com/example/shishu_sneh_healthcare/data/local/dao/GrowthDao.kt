package com.example.shishu_sneh_healthcare.data.local.dao

import androidx.room.*
import com.example.shishu_sneh_healthcare.data.local.entity.GrowthEntryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GrowthDao {
    @Query("SELECT * FROM growth_entries WHERE babyId = :babyId ORDER BY date DESC")
    fun getGrowthEntries(babyId: Long): Flow<List<GrowthEntryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGrowthEntry(entry: GrowthEntryEntity): Long

    @Delete
    suspend fun deleteGrowthEntry(entry: GrowthEntryEntity): Int
}
