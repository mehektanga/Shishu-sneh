package com.example.shishu_sneh_healthcare.presentation.medication

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shishu_sneh_healthcare.data.local.entity.MedicationEntity
import com.example.shishu_sneh_healthcare.domain.repository.HealthRecordRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MedicationViewModel @Inject constructor(
    private val repository: HealthRecordRepository
) : ViewModel() {

    private val _medications = MutableStateFlow<List<MedicationEntity>>(emptyList())
    val medications: StateFlow<List<MedicationEntity>> = _medications.asStateFlow()

    fun loadMedications(babyId: Long) {
        viewModelScope.launch {
            repository.getMedications(babyId).collectLatest {
                _medications.value = it
            }
        }
    }

    fun addMedication(medication: MedicationEntity) {
        viewModelScope.launch {
            repository.insertMedication(medication)
        }
    }
}
