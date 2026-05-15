package com.example.shishu_sneh_healthcare.presentation.vaccine

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shishu_sneh_healthcare.data.local.entity.VaccineEntity
import com.example.shishu_sneh_healthcare.domain.repository.VaccineRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VaccinationViewModel @Inject constructor(
    private val repository: VaccineRepository
) : ViewModel() {

    private val _vaccines = MutableStateFlow<List<VaccineEntity>>(emptyList())
    val vaccines: StateFlow<List<VaccineEntity>> = _vaccines.asStateFlow()

    fun loadVaccines(babyId: Long) {
        viewModelScope.launch {
            repository.getVaccinesForBaby(babyId).collectLatest {
                _vaccines.value = it
            }
        }
    }

    fun updateVaccineStatus(vaccine: VaccineEntity) {
        viewModelScope.launch {
            repository.updateVaccine(vaccine)
        }
    }
}
