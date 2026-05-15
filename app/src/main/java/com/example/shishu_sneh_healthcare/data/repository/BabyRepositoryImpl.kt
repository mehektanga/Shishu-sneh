package com.example.shishu_sneh_healthcare.data.repository

import com.example.shishu_sneh_healthcare.data.local.dao.BabyDao
import com.example.shishu_sneh_healthcare.data.local.entity.BabyEntity
import com.example.shishu_sneh_healthcare.domain.repository.BabyRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class BabyRepositoryImpl @Inject constructor(
    private val babyDao: BabyDao
) : BabyRepository {
    override fun getBabiesForUser(userId: String): Flow<List<BabyEntity>> =
        babyDao.getBabiesForUser(userId)

    override suspend fun getBabyById(id: Long): BabyEntity? =
        babyDao.getBabyById(id)

    override suspend fun insertBaby(baby: BabyEntity): Long =
        babyDao.insertBaby(baby)

    override suspend fun updateBaby(baby: BabyEntity): Int =
        babyDao.updateBaby(baby)

    override suspend fun deleteBaby(baby: BabyEntity): Int =
        babyDao.deleteBaby(baby)
}
