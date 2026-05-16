package com.example.shishu_sneh_healthcare.presentation.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shishu_sneh_healthcare.data.local.entity.BabyEntity
import com.example.shishu_sneh_healthcare.data.local.entity.MedicationEntity
import com.example.shishu_sneh_healthcare.data.local.entity.VaccineEntity
import com.example.shishu_sneh_healthcare.domain.repository.AuthRepository
import com.example.shishu_sneh_healthcare.domain.repository.HealthRecordRepository
import com.example.shishu_sneh_healthcare.domain.repository.VaccineRepository
import com.example.shishu_sneh_healthcare.domain.repository.MilestoneRepository
import com.example.shishu_sneh_healthcare.domain.use_case.GetBabiesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val getBabiesUseCase: GetBabiesUseCase,
    private val authRepository: AuthRepository,
    private val healthRecordRepository: HealthRecordRepository,
    private val vaccineRepository: VaccineRepository,
    private val milestoneRepository: MilestoneRepository
) : ViewModel() {

    private val _babies = MutableStateFlow<List<BabyEntity>>(emptyList())
    val babies: StateFlow<List<BabyEntity>> = _babies.asStateFlow()

    private val _selectedBaby = MutableStateFlow<BabyEntity?>(null)
    val selectedBaby: StateFlow<BabyEntity?> = _selectedBaby.asStateFlow()

    private val _currentTab = MutableStateFlow(0)
    val currentTab: StateFlow<Int> = _currentTab.asStateFlow()

    private val _upcomingVaccines = MutableStateFlow<List<VaccineEntity>>(emptyList())
    val upcomingVaccines: StateFlow<List<VaccineEntity>> = _upcomingVaccines.asStateFlow()

    private val _activeMedications = MutableStateFlow<List<MedicationEntity>>(emptyList())
    val activeMedications: StateFlow<List<MedicationEntity>> = _activeMedications.asStateFlow()

    private val _milestoneProgress = MutableStateFlow(0f)
    val milestoneProgress: StateFlow<Float> = _milestoneProgress.asStateFlow()

    private var dataLoadJob: Job? = null

    fun setCurrentTab(index: Int) {
        _currentTab.value = index
    }

    fun loadBabies() {
        val userId = authRepository.getCurrentUserId() ?: return
        viewModelScope.launch {
            getBabiesUseCase(userId).distinctUntilChanged().collectLatest { babyList ->
                _babies.value = babyList
                if (babyList.isNotEmpty() && _selectedBaby.value == null) {
                    val baby = babyList.first()
                    _selectedBaby.value = baby
                    startDashboardSync(baby.id)
                }
            }
        }
    }

    private fun startDashboardSync(babyId: Long) {
        dataLoadJob?.cancel()
        dataLoadJob = viewModelScope.launch {
            // Combine local data streams into one to reduce thread switching
            combine(
                vaccineRepository.getVaccinesForBaby(babyId),
                healthRecordRepository.getMedications(babyId),
                milestoneRepository.getMilestonesForBaby(babyId)
            ) { vaccines, meds, milestones ->
                Triple(vaccines, meds, milestones)
            }.collectLatest { (vaccines, meds, milestones) ->
                _upcomingVaccines.value = vaccines.filter { it.status != "Done" }
                    .sortedBy { it.scheduledDate }
                    .take(3)
                _activeMedications.value = meds.take(3)
                if (milestones.isNotEmpty()) {
                    _milestoneProgress.value = milestones.count { it.status == "Yes" }.toFloat() / milestones.size
                }
            }
        }
    }

    fun selectBaby(baby: BabyEntity) {
        _selectedBaby.value = baby
        startDashboardSync(baby.id)
    }
}
