package com.example.shishu_sneh_healthcare.presentation.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shishu_sneh_healthcare.data.preferences.PreferenceManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val preferenceManager: PreferenceManager
) : ViewModel() {

    fun completeOnboarding() {
        viewModelScope.launch {
            preferenceManager.setOnboardingCompleted(true)
        }
    }
}
