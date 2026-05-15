package com.example.shishu_sneh_healthcare.domain.repository

import com.example.shishu_sneh_healthcare.data.local.entity.BabyEntity
import kotlinx.coroutines.flow.Flow

interface BabyRepository {
    fun getBabiesForUser(userId: String): Flow<List<BabyEntity>>
    suspend fun getBabyById(id: Long): BabyEntity?
    suspend fun insertBaby(baby: BabyEntity): Long
    suspend fun updateBaby(baby: BabyEntity): Int
    suspend fun deleteBaby(baby: BabyEntity): Int
}
