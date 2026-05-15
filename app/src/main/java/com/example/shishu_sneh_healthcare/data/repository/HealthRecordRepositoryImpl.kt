package com.example.shishu_sneh_healthcare.data.repository

import com.example.shishu_sneh_healthcare.data.local.dao.HealthRecordDao
import com.example.shishu_sneh_healthcare.data.local.dao.MedicationDao
import com.example.shishu_sneh_healthcare.data.local.entity.HealthRecordEntity
import com.example.shishu_sneh_healthcare.data.local.entity.MedicationEntity
import com.example.shishu_sneh_healthcare.domain.repository.HealthRecordRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class HealthRecordRepositoryImpl @Inject constructor(
    private val healthRecordDao: HealthRecordDao,
    private val medicationDao: MedicationDao
) : HealthRecordRepository {
    override fun getHealthRecords(babyId: Long): Flow<List<HealthRecordEntity>> =
        healthRecordDao.getHealthRecords(babyId)

    override suspend fun insertHealthRecord(record: HealthRecordEntity) {
        healthRecordDao.insertHealthRecord(record)
    }

    override fun getMedications(babyId: Long): Flow<List<MedicationEntity>> =
        medicationDao.getMedications(babyId)

    override suspend fun insertMedication(medication: MedicationEntity) {
        medicationDao.insertMedication(medication)
    }
}
