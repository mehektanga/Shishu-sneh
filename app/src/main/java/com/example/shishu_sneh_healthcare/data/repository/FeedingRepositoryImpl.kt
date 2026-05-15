package com.example.shishu_sneh_healthcare.data.repository

import com.example.shishu_sneh_healthcare.data.local.dao.FeedingDao
import com.example.shishu_sneh_healthcare.data.local.entity.FeedingLogEntity
import com.example.shishu_sneh_healthcare.domain.repository.AuthRepository
import com.example.shishu_sneh_healthcare.domain.repository.FeedingRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FeedingRepositoryImpl @Inject constructor(
    private val feedingDao: FeedingDao,
    private val firestore: FirebaseFirestore,
    private val authRepository: AuthRepository
) : FeedingRepository {
    override fun getFeedingLogs(babyId: Long): Flow<List<FeedingLogEntity>> =
        feedingDao.getFeedingLogs(babyId)

    override suspend fun insertFeedingLog(log: FeedingLogEntity) {
        val id = feedingDao.insertFeedingLog(log)
        val userId = authRepository.getCurrentUserId()
        if (userId != null) {
            try {
                firestore.collection("users").document(userId)
                    .collection("baby").document(log.babyId.toString())
                    .collection("feeding").document(id.toString())
                    .set(log.copy(id = id)).await()
            } catch (e: Exception) {
                // Background save failure
            }
        }
    }

    override fun getFeedingLogsSince(babyId: Long, since: Long): Flow<List<FeedingLogEntity>> =
        feedingDao.getFeedingLogsSince(babyId, since)
}
