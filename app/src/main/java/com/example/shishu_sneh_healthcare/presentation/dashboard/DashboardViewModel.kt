package com.example.shishu_sneh_healthcare.presentation.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shishu_sneh_healthcare.data.local.entity.BabyEntity
import com.example.shishu_sneh_healthcare.domain.use_case.GetBabiesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val getBabiesUseCase: GetBabiesUseCase
) : ViewModel() {

    private val _babies = MutableStateFlow<List<BabyEntity>>(emptyList())
    val babies: StateFlow<List<BabyEntity>> = _babies.asStateFlow()

    private val _selectedBaby = MutableStateFlow<BabyEntity?>(null)
    val selectedBaby: StateFlow<BabyEntity?> = _selectedBaby.asStateFlow()

    private val _currentTab = MutableStateFlow(0)
    val currentTab: StateFlow<Int> = _currentTab.asStateFlow()

    fun setCurrentTab(index: Int) {
        _currentTab.value = index
    }

    fun loadBabies(userId: String) {
        viewModelScope.launch {
            getBabiesUseCase(userId).collectLatest {
                _babies.value = it
                if (it.isNotEmpty() && _selectedBaby.value == null) {
                    _selectedBaby.value = it.first()
                }
            }
        }
    }

    fun selectBaby(baby: BabyEntity) {
        _selectedBaby.value = baby
    }
}
