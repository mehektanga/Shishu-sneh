package com.example.shishu_sneh_healthcare.presentation.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shishu_sneh_healthcare.data.preferences.PreferenceManager
import com.example.shishu_sneh_healthcare.presentation.navigation.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val preferenceManager: PreferenceManager
) : ViewModel() {

    private val _startDestination = MutableStateFlow<String?>(null)
    val startDestination: StateFlow<String?> = _startDestination.asStateFlow()

    init {
        checkOnboardingState()
    }

    private fun checkOnboardingState() {
        viewModelScope.launch {
            val isOnboardingCompleted = preferenceManager.isOnboardingCompleted.first()
            if (isOnboardingCompleted) {
                _startDestination.value = Screen.Login.route
            } else {
                _startDestination.value = Screen.Onboarding.route
            }
        }
    }
}
