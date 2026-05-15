package com.example.shishu_sneh_healthcare.data.repository

import com.example.shishu_sneh_healthcare.data.local.dao.FeedingDao
import com.example.shishu_sneh_healthcare.data.local.entity.FeedingLogEntity
import com.example.shishu_sneh_healthcare.domain.repository.FeedingRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FeedingRepositoryImpl @Inject constructor(
    private val feedingDao: FeedingDao
) : FeedingRepository {
    override fun getFeedingLogs(babyId: Long): Flow<List<FeedingLogEntity>> =
        feedingDao.getFeedingLogs(babyId)

    override suspend fun insertFeedingLog(log: FeedingLogEntity) {
        feedingDao.insertFeedingLog(log)
    }

    override fun getFeedingLogsSince(babyId: Long, since: Long): Flow<List<FeedingLogEntity>> =
        feedingDao.getFeedingLogsSince(babyId, since)
}
