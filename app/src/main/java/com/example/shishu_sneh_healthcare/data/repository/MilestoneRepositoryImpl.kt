package com.example.shishu_sneh_healthcare.data.repository

import com.example.shishu_sneh_healthcare.data.local.dao.MilestoneDao
import com.example.shishu_sneh_healthcare.data.local.entity.MilestoneEntity
import com.example.shishu_sneh_healthcare.domain.repository.AuthRepository
import com.example.shishu_sneh_healthcare.domain.repository.MilestoneRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class MilestoneRepositoryImpl @Inject constructor(
    private val milestoneDao: MilestoneDao,
    private val firestore: FirebaseFirestore,
    private val authRepository: AuthRepository
) : MilestoneRepository {
    override fun getMilestonesForBaby(babyId: Long): Flow<List<MilestoneEntity>> =
        milestoneDao.getMilestonesForBaby(babyId)

    override suspend fun insertMilestones(milestones: List<MilestoneEntity>) {
        milestoneDao.insertMilestones(milestones)
        val userId = authRepository.getCurrentUserId()
        if (userId != null && milestones.isNotEmpty()) {
            val babyId = milestones.first().babyId
            try {
                val batch = firestore.batch()
                milestones.forEach { milestone ->
                    val docRef = firestore.collection("users").document(userId)
                        .collection("baby").document(babyId.toString())
                        .collection("milestones").document(milestone.id.toString())
                    batch.set(docRef, milestone)
                }
                batch.commit().await()
            } catch (e: Exception) {
                // Background save failure
            }
        }
    }

    override suspend fun updateMilestone(milestone: MilestoneEntity) {
        milestoneDao.updateMilestone(milestone)
        val userId = authRepository.getCurrentUserId()
        if (userId != null) {
            try {
                firestore.collection("users").document(userId)
                    .collection("baby").document(milestone.babyId.toString())
                    .collection("milestones").document(milestone.id.toString())
                    .set(milestone).await()
            } catch (e: Exception) {
                // Background save failure
            }
        }
    }
}
