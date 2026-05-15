package com.example.shishu_sneh_healthcare.domain.repository

import com.example.shishu_sneh_healthcare.data.local.entity.MilestoneEntity
import kotlinx.coroutines.flow.Flow

interface MilestoneRepository {
    fun getMilestonesForBaby(babyId: Long): Flow<List<MilestoneEntity>>
    suspend fun insertMilestones(milestones: List<MilestoneEntity>)
    suspend fun updateMilestone(milestone: MilestoneEntity)
}
