package com.example.shishu_sneh_healthcare.data.repository

import com.example.shishu_sneh_healthcare.data.local.dao.HealthRecordDao
import com.example.shishu_sneh_healthcare.data.local.dao.MedicationDao
import com.example.shishu_sneh_healthcare.data.local.entity.HealthRecordEntity
import com.example.shishu_sneh_healthcare.data.local.entity.MedicationEntity
import com.example.shishu_sneh_healthcare.domain.repository.AuthRepository
import com.example.shishu_sneh_healthcare.domain.repository.HealthRecordRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class HealthRecordRepositoryImpl @Inject constructor(
    private val healthRecordDao: HealthRecordDao,
    private val medicationDao: MedicationDao,
    private val firestore: FirebaseFirestore,
    private val authRepository: AuthRepository
) : HealthRecordRepository {
    override fun getHealthRecords(babyId: Long): Flow<List<HealthRecordEntity>> =
        healthRecordDao.getHealthRecords(babyId)

    override suspend fun insertHealthRecord(record: HealthRecordEntity) {
        val id = healthRecordDao.insertHealthRecord(record)
        val userId = authRepository.getCurrentUserId()
        if (userId != null) {
            try {
                firestore.collection("users").document(userId)
                    .collection("baby").document(record.babyId.toString())
                    .collection("records").document(id.toString())
                    .set(record.copy(id = id)).await()
            } catch (e: Exception) {
                // Background save failure
            }
        }
    }

    override fun getMedications(babyId: Long): Flow<List<MedicationEntity>> =
        medicationDao.getMedications(babyId)

    override suspend fun insertMedication(medication: MedicationEntity) {
        val id = medicationDao.insertMedication(medication)
        val userId = authRepository.getCurrentUserId()
        if (userId != null) {
            try {
                firestore.collection("users").document(userId)
                    .collection("baby").document(medication.babyId.toString())
                    .collection("medications").document(id.toString())
                    .set(medication.copy(id = id)).await()
            } catch (e: Exception) {
                // Background save failure
            }
        }
    }
}
