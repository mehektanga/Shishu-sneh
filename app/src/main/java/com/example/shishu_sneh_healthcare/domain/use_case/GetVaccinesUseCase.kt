package com.example.shishu_sneh_healthcare.domain.use_case

import com.example.shishu_sneh_healthcare.data.local.entity.VaccineEntity
import com.example.shishu_sneh_healthcare.domain.repository.VaccineRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetVaccinesUseCase @Inject constructor(
    private val repository: VaccineRepository
) {
    operator fun invoke(babyId: Long): Flow<List<VaccineEntity>> {
        return repository.getVaccinesForBaby(babyId)
    }
}
