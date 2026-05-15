package com.example.shishu_sneh_healthcare.data.repository

import com.example.shishu_sneh_healthcare.data.local.dao.VaccineDao
import com.example.shishu_sneh_healthcare.data.local.entity.VaccineEntity
import com.example.shishu_sneh_healthcare.domain.repository.AuthRepository
import com.example.shishu_sneh_healthcare.domain.repository.VaccineRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class VaccineRepositoryImpl @Inject constructor(
    private val vaccineDao: VaccineDao,
    private val firestore: FirebaseFirestore,
    private val authRepository: AuthRepository
) : VaccineRepository {
    override fun getVaccinesForBaby(babyId: Long): Flow<List<VaccineEntity>> =
        vaccineDao.getVaccinesForBaby(babyId)

    override suspend fun insertVaccines(vaccines: List<VaccineEntity>) {
        vaccineDao.insertVaccines(vaccines)
        val userId = authRepository.getCurrentUserId()
        if (userId != null && vaccines.isNotEmpty()) {
            val babyId = vaccines.first().babyId
            try {
                val batch = firestore.batch()
                vaccines.forEach { vaccine ->
                    val docRef = firestore.collection("users").document(userId)
                        .collection("baby").document(babyId.toString())
                        .collection("vaccines").document(vaccine.id.toString())
                    batch.set(docRef, vaccine)
                }
                batch.commit().await()
            } catch (e: Exception) {
                // Background save failure
            }
        }
    }

    override suspend fun updateVaccine(vaccine: VaccineEntity) {
        vaccineDao.updateVaccine(vaccine)
        val userId = authRepository.getCurrentUserId()
        if (userId != null) {
            try {
                firestore.collection("users").document(userId)
                    .collection("baby").document(vaccine.babyId.toString())
                    .collection("vaccines").document(vaccine.id.toString())
                    .set(vaccine).await()
            } catch (e: Exception) {
                // Background save failure
            }
        }
    }

    override fun getOverdueVaccines(babyId: Long): Flow<List<VaccineEntity>> =
        vaccineDao.getOverdueVaccines(babyId)
}
