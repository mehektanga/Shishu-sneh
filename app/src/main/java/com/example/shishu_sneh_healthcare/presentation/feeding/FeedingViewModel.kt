package com.example.shishu_sneh_healthcare.presentation.feeding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shishu_sneh_healthcare.data.local.entity.FeedingLogEntity
import com.example.shishu_sneh_healthcare.domain.repository.FeedingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FeedingViewModel @Inject constructor(
    private val repository: FeedingRepository
) : ViewModel() {

    private val _feedingLogs = MutableStateFlow<List<FeedingLogEntity>>(emptyList())
    val feedingLogs: StateFlow<List<FeedingLogEntity>> = _feedingLogs.asStateFlow()

    fun loadFeedingLogs(babyId: Long) {
        viewModelScope.launch {
            repository.getFeedingLogs(babyId).collectLatest {
                _feedingLogs.value = it
            }
        }
    }

    fun addFeedingLog(log: FeedingLogEntity) {
        viewModelScope.launch {
            repository.insertFeedingLog(log)
        }
    }
}
