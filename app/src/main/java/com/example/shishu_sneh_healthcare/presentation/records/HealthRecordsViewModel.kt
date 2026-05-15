package com.example.shishu_sneh_healthcare.presentation.records

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shishu_sneh_healthcare.data.local.entity.HealthRecordEntity
import com.example.shishu_sneh_healthcare.domain.repository.HealthRecordRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HealthRecordsViewModel @Inject constructor(
    private val repository: HealthRecordRepository
) : ViewModel() {

    private val _records = MutableStateFlow<List<HealthRecordEntity>>(emptyList())
    val records: StateFlow<List<HealthRecordEntity>> = _records.asStateFlow()

    fun loadRecords(babyId: Long) {
        viewModelScope.launch {
            repository.getHealthRecords(babyId).collectLatest {
                _records.value = it
            }
        }
    }

    fun addRecord(record: HealthRecordEntity) {
        viewModelScope.launch {
            repository.insertHealthRecord(record)
        }
    }
}
