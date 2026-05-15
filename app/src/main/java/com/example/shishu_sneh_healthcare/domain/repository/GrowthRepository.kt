package com.example.shishu_sneh_healthcare.domain.repository

import com.example.shishu_sneh_healthcare.data.local.entity.GrowthEntryEntity
import kotlinx.coroutines.flow.Flow

interface GrowthRepository {
    fun getGrowthEntries(babyId: Long): Flow<List<GrowthEntryEntity>>
    suspend fun insertGrowthEntry(entry: GrowthEntryEntity): Long
    suspend fun deleteGrowthEntry(entry: GrowthEntryEntity): Int
}
