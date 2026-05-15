package com.example.shishu_sneh_healthcare.presentation.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shishu_sneh_healthcare.data.local.entity.BabyEntity
import com.example.shishu_sneh_healthcare.data.local.entity.MedicationEntity
import com.example.shishu_sneh_healthcare.data.local.entity.VaccineEntity
import com.example.shishu_sneh_healthcare.data.local.entity.MilestoneEntity
import com.example.shishu_sneh_healthcare.domain.repository.AuthRepository
import com.example.shishu_sneh_healthcare.domain.repository.BabyRepository
import com.example.shishu_sneh_healthcare.domain.repository.HealthRecordRepository
import com.example.shishu_sneh_healthcare.domain.repository.VaccineRepository
import com.example.shishu_sneh_healthcare.domain.repository.MilestoneRepository
import com.example.shishu_sneh_healthcare.domain.use_case.GetBabiesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
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

    // Dashboard Summaries
    private val _upcomingVaccines = MutableStateFlow<List<VaccineEntity>>(emptyList())
    val upcomingVaccines: StateFlow<List<VaccineEntity>> = _upcomingVaccines.asStateFlow()

    private val _activeMedications = MutableStateFlow<List<MedicationEntity>>(emptyList())
    val activeMedications: StateFlow<List<MedicationEntity>> = _activeMedications.asStateFlow()

    private val _milestoneProgress = MutableStateFlow(0f)
    val milestoneProgress: StateFlow<Float> = _milestoneProgress.asStateFlow()

    fun setCurrentTab(index: Int) {
        _currentTab.value = index
    }

    fun loadBabies() {
        val userId = authRepository.getCurrentUserId() ?: return
        viewModelScope.launch {
            getBabiesUseCase(userId).collectLatest { babyList ->
                _babies.value = babyList
                if (babyList.isNotEmpty() && _selectedBaby.value == null) {
                    val baby = babyList.first()
                    _selectedBaby.value = baby
                    loadDashboardData(baby.id)
                }
            }
        }
    }

    private fun loadDashboardData(babyId: Long) {
        viewModelScope.launch {
            // Load Vaccines
            vaccineRepository.getVaccinesForBaby(babyId).collectLatest { list ->
                _upcomingVaccines.value = list.filter { it.status != "Done" }
                    .sortedBy { it.scheduledDate }
                    .take(3)
            }
        }
        viewModelScope.launch {
            // Load Medications
            healthRecordRepository.getMedications(babyId).collectLatest { list ->
                _activeMedications.value = list.take(3)
            }
        }
        viewModelScope.launch {
            // Load Milestones
            milestoneRepository.getMilestonesForBaby(babyId).collectLatest { list ->
                if (list.isNotEmpty()) {
                    _milestoneProgress.value = list.count { it.status == "Yes" }.toFloat() / list.size
                }
            }
        }
    }

    fun selectBaby(baby: BabyEntity) {
        _selectedBaby.value = baby
        loadDashboardData(baby.id)
    }
}
