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
private const val FIRESTORE_TIMEOUT = 5000L // 5 seconds

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
        val startTime = System.currentTimeMillis()
        try {
            Log.d(TAG, "Starting login for $email")
            firebaseAuth.signInWithEmailAndPassword(email, password).await()
            Log.d(TAG, "Login successful in ${System.currentTimeMillis() - startTime}ms")
            emit(Result.success(Unit))
        } catch (e: Exception) {
            Log.e(TAG, "Login failed after ${System.currentTimeMillis() - startTime}ms: ${e.message}")
            emit(Result.failure(e))
        }
    }

    override suspend fun registerWithEmail(name: String, email: String, password: String): Flow<Result<Unit>> = flow {
        val totalStartTime = System.currentTimeMillis()
        try {
            // 1. Create Auth User - must be successful to proceed
            Log.d(TAG, "Creating Auth user for $email")
            val authStartTime = System.currentTimeMillis()
            val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            Log.d(TAG, "Auth user created in ${System.currentTimeMillis() - authStartTime}ms")
            
            // 2. Update User Profile (Auth)
            val profileStartTime = System.currentTimeMillis()
            val profileUpdates = userProfileChangeRequest {
                displayName = name
            }
            result.user?.updateProfile(profileUpdates)?.await()
            Log.d(TAG, "Auth profile updated in ${System.currentTimeMillis() - profileStartTime}ms")
            
            // 3. Create User Document in Firestore
            // We'll wrap this in a timeout and log it, but we can emit success even if Firestore is slow
            // as long as the user is authenticated, we can let them proceed to setup.
            val uid = result.user?.uid
            if (uid != null) {
                try {
                    Log.d(TAG, "Creating Firestore document for UID: $uid")
                    val firestoreStartTime = System.currentTimeMillis()
                    withTimeout(FIRESTORE_TIMEOUT) {
                        val userData = mapOf(
                            "name" to name,
                            "email" to email,
                            "uid" to uid,
                            "createdAt" to System.currentTimeMillis()
                        )
                        firestore.collection("users").document(uid).set(userData).await()
                    }
                    Log.d(TAG, "Firestore document created in ${System.currentTimeMillis() - firestoreStartTime}ms")
                } catch (e: Exception) {
                    Log.e(TAG, "Firestore user creation timed out or failed: ${e.message}")
                    // Non-fatal for registration flow; we proceed as user is created in Auth
                }
            }
            
            Log.d(TAG, "Total registration flow completed in ${System.currentTimeMillis() - totalStartTime}ms")
            emit(Result.success(Unit))
        } catch (e: Exception) {
            Log.e(TAG, "Registration failed after ${System.currentTimeMillis() - totalStartTime}ms: ${e.message}")
            emit(Result.failure(e))
        }
    }

    override suspend fun hasBabyProfile(): Result<Boolean> {
        val startTime = System.currentTimeMillis()
        return try {
            val uid = firebaseAuth.currentUser?.uid ?: return Result.failure(Exception("User not logged in"))
            Log.d(TAG, "Checking baby profile for UID: $uid")
            
            val snapshot = withTimeout(FIRESTORE_TIMEOUT) {
                firestore.collection("users").document(uid).collection("baby").get().await()
            }
            
            val exists = !snapshot.isEmpty
            Log.d(TAG, "Baby profile check completed in ${System.currentTimeMillis() - startTime}ms. Exists: $exists")
            Result.success(exists)
        } catch (e: Exception) {
            Log.e(TAG, "Baby profile check failed/timed out after ${System.currentTimeMillis() - startTime}ms: ${e.message}")
            Result.failure(e)
        }
    }
}
