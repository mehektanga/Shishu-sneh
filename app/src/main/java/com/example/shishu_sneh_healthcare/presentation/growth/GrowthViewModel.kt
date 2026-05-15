package com.example.shishu_sneh_healthcare.presentation.growth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shishu_sneh_healthcare.data.local.entity.GrowthEntryEntity
import com.example.shishu_sneh_healthcare.domain.repository.GrowthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GrowthViewModel @Inject constructor(
    private val repository: GrowthRepository
) : ViewModel() {

    private val _growthEntries = MutableStateFlow<List<GrowthEntryEntity>>(emptyList())
    val growthEntries: StateFlow<List<GrowthEntryEntity>> = _growthEntries.asStateFlow()

    fun loadGrowthEntries(babyId: Long) {
        viewModelScope.launch {
            repository.getGrowthEntries(babyId).collectLatest {
                _growthEntries.value = it
            }
        }
    }

    fun addGrowthEntry(entry: GrowthEntryEntity) {
        viewModelScope.launch {
            repository.insertGrowthEntry(entry)
        }
    }
}
