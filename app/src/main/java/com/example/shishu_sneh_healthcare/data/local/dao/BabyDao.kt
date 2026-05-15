package com.example.shishu_sneh_healthcare.data.local.dao

import androidx.room.*
import com.example.shishu_sneh_healthcare.data.local.entity.BabyEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BabyDao {
    @Query("SELECT * FROM babies WHERE userId = :userId")
    fun getBabiesForUser(userId: String): Flow<List<BabyEntity>>

    @Query("SELECT * FROM babies WHERE id = :id")
    suspend fun getBabyById(id: Long): BabyEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBaby(baby: BabyEntity): Long

    @Update
    suspend fun updateBaby(baby: BabyEntity): Int

    @Delete
    suspend fun deleteBaby(baby: BabyEntity): Int
}
