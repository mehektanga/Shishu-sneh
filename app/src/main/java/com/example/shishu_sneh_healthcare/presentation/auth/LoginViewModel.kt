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

    private val _phoneNumber = MutableStateFlow("")
    val phoneNumber: StateFlow<String> = _phoneNumber.asStateFlow()

    private val _otp = MutableStateFlow("")
    val otp: StateFlow<String> = _otp.asStateFlow()

    private val _isOtpSent = MutableStateFlow(false)
    val isOtpSent: StateFlow<Boolean> = _isOtpSent.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private var verificationId: String? = null

    fun onPhoneNumberChange(newValue: String) {
        _phoneNumber.value = newValue
    }

    fun onOtpChange(newValue: String) {
        _otp.value = newValue
    }

    fun sendOtp() {
        if (_phoneNumber.value.length < 10) {
            _error.value = "Enter a valid phone number"
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            authRepository.sendOtp(
                phoneNumber = _phoneNumber.value,
                onCodeSent = { id ->
                    verificationId = id
                    _isOtpSent.value = true
                    _isLoading.value = false
                },
                onVerificationFailed = { e ->
                    _error.value = e.message ?: "Failed to send OTP"
                    _isLoading.value = false
                }
            )
        }
    }

    fun verifyOtp(onSuccess: () -> Unit) {
        val id = verificationId ?: return
        
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            authRepository.verifyOtp(id, _otp.value).collectLatest { result ->
                if (result.isSuccess) {
                    onSuccess()
                } else {
                    _error.value = result.exceptionOrNull()?.message ?: "Invalid OTP"
                }
                _isLoading.value = false
            }
        }
    }

    fun resetOtpState() {
        _isOtpSent.value = false
        _otp.value = ""
        _error.value = null
    }
}
