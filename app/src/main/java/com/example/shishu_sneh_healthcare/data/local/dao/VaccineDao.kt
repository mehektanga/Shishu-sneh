package com.example.shishu_sneh_healthcare.data.local.dao

import androidx.room.*
import com.example.shishu_sneh_healthcare.data.local.entity.VaccineEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface VaccineDao {
    @Query("SELECT * FROM vaccines WHERE babyId = :babyId ORDER BY scheduledDate ASC")
    fun getVaccinesForBaby(babyId: Long): Flow<List<VaccineEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVaccines(vaccines: List<VaccineEntity>): List<Long>

    @Update
    suspend fun updateVaccine(vaccine: VaccineEntity): Int

    @Query("SELECT * FROM vaccines WHERE status = 'Overdue' AND babyId = :babyId")
    fun getOverdueVaccines(babyId: Long): Flow<List<VaccineEntity>>
}
