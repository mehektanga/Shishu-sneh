package com.example.shishu_sneh_healthcare.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.shishu_sneh_healthcare.domain.repository.VaccineRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.first

@HiltWorker
class VaccineReminderWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val vaccineRepository: VaccineRepository
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        val babyId = inputData.getLong("babyId", -1L)
        if (babyId == -1L) return Result.failure()

        val vaccines = vaccineRepository.getOverdueVaccines(babyId).first()
        if (vaccines.isNotEmpty()) {
            // Notification logic would go here
            // For now, we'll just log or return success
        }

        return Result.success()
    }
}
