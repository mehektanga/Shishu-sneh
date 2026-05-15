package com.example.shishu_sneh_healthcare.domain.repository

import com.example.shishu_sneh_healthcare.data.local.entity.VaccineEntity
import kotlinx.coroutines.flow.Flow

interface VaccineRepository {
    fun getVaccinesForBaby(babyId: Long): Flow<List<VaccineEntity>>
    suspend fun insertVaccines(vaccines: List<VaccineEntity>)
    suspend fun updateVaccine(vaccine: VaccineEntity)
    fun getOverdueVaccines(babyId: Long): Flow<List<VaccineEntity>>
}
