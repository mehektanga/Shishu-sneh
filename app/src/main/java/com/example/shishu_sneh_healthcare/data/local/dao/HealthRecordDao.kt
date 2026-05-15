package com.example.shishu_sneh_healthcare.data.local.dao

import androidx.room.*
import com.example.shishu_sneh_healthcare.data.local.entity.HealthRecordEntity
import com.example.shishu_sneh_healthcare.data.local.entity.MedicationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface HealthRecordDao {
    @Query("SELECT * FROM health_records WHERE babyId = :babyId ORDER BY date DESC")
    fun getHealthRecords(babyId: Long): Flow<List<HealthRecordEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHealthRecord(record: HealthRecordEntity): Long
}
