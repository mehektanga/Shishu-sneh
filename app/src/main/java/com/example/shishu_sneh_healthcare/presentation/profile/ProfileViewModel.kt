package com.example.shishu_sneh_healthcare.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shishu_sneh_healthcare.data.local.entity.BabyEntity
import com.example.shishu_sneh_healthcare.domain.repository.BabyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: BabyRepository
) : ViewModel() {

    fun saveBabyDetails(baby: BabyEntity, onSuccess: () -> Unit) {
        viewModelScope.launch {
            repository.insertBaby(baby)
            onSuccess()
        }
    }
}
