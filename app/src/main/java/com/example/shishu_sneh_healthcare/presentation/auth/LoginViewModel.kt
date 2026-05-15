package com.example.shishu_sneh_healthcare.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shishu_sneh_healthcare.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email.asStateFlow()

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    fun onEmailChange(newValue: String) {
        _email.value = newValue
    }

    fun onPasswordChange(newValue: String) {
        _password.value = newValue
    }

    fun loginUser(onSuccess: (Boolean) -> Unit) {
        if (_email.value.isBlank() || _password.value.isBlank()) {
            _error.value = "Email and password cannot be empty"
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            authRepository.loginWithEmail(_email.value, _password.value).collectLatest { result ->
                if (result.isSuccess) {
                    val profileResult = authRepository.hasBabyProfile()
                    onSuccess(profileResult.getOrDefault(false))
                } else {
                    _error.value = result.exceptionOrNull()?.message ?: "Login failed"
                }
                _isLoading.value = false
            }
        }
    }
}
