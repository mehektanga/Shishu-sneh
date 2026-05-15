package com.example.shishu_sneh_healthcare.data.repository

import com.example.shishu_sneh_healthcare.data.local.dao.VaccineDao
import com.example.shishu_sneh_healthcare.data.local.entity.VaccineEntity
import com.example.shishu_sneh_healthcare.domain.repository.VaccineRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class VaccineRepositoryImpl @Inject constructor(
    private val vaccineDao: VaccineDao
) : VaccineRepository {
    override fun getVaccinesForBaby(babyId: Long): Flow<List<VaccineEntity>> =
        vaccineDao.getVaccinesForBaby(babyId)

    override suspend fun insertVaccines(vaccines: List<VaccineEntity>) {
        vaccineDao.insertVaccines(vaccines)
    }

    override suspend fun updateVaccine(vaccine: VaccineEntity) {
        vaccineDao.updateVaccine(vaccine)
    }

    override fun getOverdueVaccines(babyId: Long): Flow<List<VaccineEntity>> =
        vaccineDao.getOverdueVaccines(babyId)
}
