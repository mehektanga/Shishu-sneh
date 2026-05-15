package com.example.shishu_sneh_healthcare.domain.repository

import com.example.shishu_sneh_healthcare.data.local.entity.HealthRecordEntity
import com.example.shishu_sneh_healthcare.data.local.entity.MedicationEntity
import kotlinx.coroutines.flow.Flow

interface HealthRecordRepository {
    fun getHealthRecords(babyId: Long): Flow<List<HealthRecordEntity>>
    suspend fun insertHealthRecord(record: HealthRecordEntity)
    fun getMedications(babyId: Long): Flow<List<MedicationEntity>>
    suspend fun insertMedication(medication: MedicationEntity)
}
