package com.example.shishu_sneh_healthcare.data.repository

import com.example.shishu_sneh_healthcare.data.local.dao.GrowthDao
import com.example.shishu_sneh_healthcare.data.local.entity.GrowthEntryEntity
import com.example.shishu_sneh_healthcare.domain.repository.AuthRepository
import com.example.shishu_sneh_healthcare.domain.repository.GrowthRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class GrowthRepositoryImpl @Inject constructor(
    private val growthDao: GrowthDao,
    private val firestore: FirebaseFirestore,
    private val authRepository: AuthRepository
) : GrowthRepository {
    override fun getGrowthEntries(babyId: Long): Flow<List<GrowthEntryEntity>> =
        growthDao.getGrowthEntries(babyId)

    override suspend fun insertGrowthEntry(entry: GrowthEntryEntity): Long {
        val id = growthDao.insertGrowthEntry(entry)
        val userId = authRepository.getCurrentUserId()
        if (userId != null) {
            try {
                firestore.collection("users").document(userId)
                    .collection("baby").document(entry.babyId.toString())
                    .collection("growth").document(id.toString())
                    .set(entry.copy(id = id)).await()
            } catch (e: Exception) {
                // Background save failure
            }
        }
        return id
    }

    override suspend fun deleteGrowthEntry(entry: GrowthEntryEntity): Int =
        growthDao.deleteGrowthEntry(entry)
}
