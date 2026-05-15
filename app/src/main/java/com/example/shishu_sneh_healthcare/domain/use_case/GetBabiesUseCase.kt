package com.example.shishu_sneh_healthcare.domain.use_case

import com.example.shishu_sneh_healthcare.data.local.entity.BabyEntity
import com.example.shishu_sneh_healthcare.domain.repository.BabyRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetBabiesUseCase @Inject constructor(
    private val repository: BabyRepository
) {
    operator fun invoke(userId: String): Flow<List<BabyEntity>> {
        return repository.getBabiesForUser(userId)
    }
}
