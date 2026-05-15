package com.example.shishu_sneh_healthcare.presentation.milestone

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shishu_sneh_healthcare.data.local.entity.MilestoneEntity
import com.example.shishu_sneh_healthcare.domain.repository.MilestoneRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MilestoneViewModel @Inject constructor(
    private val repository: MilestoneRepository
) : ViewModel() {

    private val _milestones = MutableStateFlow<List<MilestoneEntity>>(emptyList())
    val milestones: StateFlow<List<MilestoneEntity>> = _milestones.asStateFlow()

    fun loadMilestones(babyId: Long) {
        viewModelScope.launch {
            repository.getMilestonesForBaby(babyId).collectLatest {
                _milestones.value = it
            }
        }
    }

    fun updateMilestone(milestone: MilestoneEntity) {
        viewModelScope.launch {
            repository.updateMilestone(milestone)
        }
    }
}
