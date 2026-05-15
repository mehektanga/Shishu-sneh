package com.example.shishu_sneh_healthcare.domain.repository

import com.example.shishu_sneh_healthcare.data.local.entity.FeedingLogEntity
import kotlinx.coroutines.flow.Flow

interface FeedingRepository {
    fun getFeedingLogs(babyId: Long): Flow<List<FeedingLogEntity>>
    suspend fun insertFeedingLog(log: FeedingLogEntity)
    fun getFeedingLogsSince(babyId: Long, since: Long): Flow<List<FeedingLogEntity>>
}
