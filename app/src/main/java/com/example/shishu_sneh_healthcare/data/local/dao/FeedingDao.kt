package com.example.shishu_sneh_healthcare.data.local.dao

import androidx.room.*
import com.example.shishu_sneh_healthcare.data.local.entity.FeedingLogEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FeedingDao {
    @Query("SELECT * FROM feeding_logs WHERE babyId = :babyId ORDER BY startTime DESC")
    fun getFeedingLogs(babyId: Long): Flow<List<FeedingLogEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFeedingLog(log: FeedingLogEntity): Long

    @Query("SELECT * FROM feeding_logs WHERE babyId = :babyId AND startTime >= :since")
    fun getFeedingLogsSince(babyId: Long, since: Long): Flow<List<FeedingLogEntity>>
}
