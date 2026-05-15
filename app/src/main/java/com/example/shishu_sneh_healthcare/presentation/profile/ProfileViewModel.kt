package com.example.shishu_sneh_healthcare.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shishu_sneh_healthcare.data.local.entity.BabyEntity
import com.example.shishu_sneh_healthcare.domain.repository.AuthRepository
import com.example.shishu_sneh_healthcare.domain.repository.BabyRepository
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: BabyRepository,
    private val authRepository: AuthRepository,
    private val firestore: FirebaseFirestore
) : ViewModel() {

    fun saveBabyDetails(
        name: String,
        dob: Long,
        gender: String,
        motherName: String,
        bloodGroup: String,
        onSuccess: () -> Unit
    ) {
        val userId = authRepository.getCurrentUserId() ?: return

        val baby = BabyEntity(
            name = name,
            dob = dob,
            gender = gender,
            bloodGroup = bloodGroup,
            birthWeight = 0.0,
            birthHeight = 0.0,
            photoUri = null,
            motherName = motherName,
            pediatrician = null,
            hospital = null,
            userId = userId
        )

        viewModelScope.launch {
            // 1. Save to Room (Local)
            val id = repository.insertBaby(baby)
            
            // 2. Save to Firestore (Remote) under users/{uid}/baby/{babyId}
            try {
                firestore.collection("users").document(userId)
                    .collection("baby").document(id.toString()).set(baby.copy(id = id)).await()
            } catch (e: Exception) {
                // Background save failed, but local is done
            }

            onSuccess()
        }
    }
}
