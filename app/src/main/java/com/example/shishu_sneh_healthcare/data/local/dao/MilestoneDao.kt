package com.example.shishu_sneh_healthcare.data.local.dao

import androidx.room.*
import com.example.shishu_sneh_healthcare.data.local.entity.MilestoneEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MilestoneDao {
    @Query("SELECT * FROM milestones WHERE babyId = :babyId ORDER BY month ASC")
    fun getMilestonesForBaby(babyId: Long): Flow<List<MilestoneEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMilestones(milestones: List<MilestoneEntity>): List<Long>

    @Update
    suspend fun updateMilestone(milestone: MilestoneEntity): Int
}
