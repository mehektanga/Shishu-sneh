package com.example.shishu_sneh_healthcare.data.repository

import com.example.shishu_sneh_healthcare.data.local.dao.GrowthDao
import com.example.shishu_sneh_healthcare.data.local.entity.GrowthEntryEntity
import com.example.shishu_sneh_healthcare.domain.repository.GrowthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GrowthRepositoryImpl @Inject constructor(
    private val growthDao: GrowthDao
) : GrowthRepository {
    override fun getGrowthEntries(babyId: Long): Flow<List<GrowthEntryEntity>> =
        growthDao.getGrowthEntries(babyId)

    override suspend fun insertGrowthEntry(entry: GrowthEntryEntity): Long =
        growthDao.insertGrowthEntry(entry)

    override suspend fun deleteGrowthEntry(entry: GrowthEntryEntity): Int =
        growthDao.deleteGrowthEntry(entry)
}
