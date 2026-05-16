package com.example.shishu_sneh_healthcare.data.repository

import android.util.Log
import com.example.shishu_sneh_healthcare.domain.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.userProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withTimeout
import javax.inject.Inject

private const val TAG = "AuthRepositoryImpl"
private const val QUICK_TIMEOUT = 1500L // 1.5 seconds for snappy UI

class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : AuthRepository {

    override fun isUserLoggedIn(): Boolean = firebaseAuth.currentUser != null

    override fun getCurrentUserId(): String? = firebaseAuth.currentUser?.uid

    override fun logout() {
        firebaseAuth.signOut()
    }

    override suspend fun loginWithEmail(email: String, password: String): Flow<Result<Unit>> = flow {
        try {
            firebaseAuth.signInWithEmailAndPassword(email, password).await()
            emit(Result.success(Unit))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    override suspend fun registerWithEmail(name: String, email: String, password: String): Flow<Result<Unit>> = flow {
        try {
            // 1. Authenticate immediately
            val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            val user = result.user
            val uid = user?.uid
            
            if (user != null && uid != null) {
                // 2. Background Profile Update (Fire-and-Forget)
                user.updateProfile(userProfileChangeRequest { displayName = name })

                // 3. Fire-and-Forget Firestore Write
                // We do NOT use .await() here so the app navigates INSTANTLY.
                val userData = mapOf(
                    "name" to name,
                    "email" to email,
                    "uid" to uid,
                    "createdAt" to System.currentTimeMillis()
                )
                firestore.collection("users").document(uid).set(userData)
                    .addOnFailureListener { e -> Log.e(TAG, "Background Firestore write failed: ${e.message}") }
            }
            
            // 4. Emit success immediately
            emit(Result.success(Unit))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    override suspend fun hasBabyProfile(): Result<Boolean> {
        return try {
            val uid = firebaseAuth.currentUser?.uid ?: return Result.failure(Exception("User not logged in"))
            
            // Short timeout to avoid hanging the UI
            val snapshot = withTimeout(QUICK_TIMEOUT) {
                firestore.collection("users").document(uid).collection("baby").limit(1).get().await()
            }
            Result.success(!snapshot.isEmpty)
        } catch (e: Exception) {
            Log.w(TAG, "Profile check timed out or failed, defaulting to 'No Profile': ${e.message}")
            Result.success(false)
        }
    }
}
