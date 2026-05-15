package com.example.shishu_sneh_healthcare.data.repository

import com.example.shishu_sneh_healthcare.data.local.dao.MilestoneDao
import com.example.shishu_sneh_healthcare.data.local.entity.MilestoneEntity
import com.example.shishu_sneh_healthcare.domain.repository.MilestoneRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MilestoneRepositoryImpl @Inject constructor(
    private val milestoneDao: MilestoneDao
) : MilestoneRepository {
    override fun getMilestonesForBaby(babyId: Long): Flow<List<MilestoneEntity>> =
        milestoneDao.getMilestonesForBaby(babyId)

    override suspend fun insertMilestones(milestones: List<MilestoneEntity>) {
        milestoneDao.insertMilestones(milestones)
    }

    override suspend fun updateMilestone(milestone: MilestoneEntity) {
        milestoneDao.updateMilestone(milestone)
    }
}
