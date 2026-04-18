package com.example.projectconnect.data.repository

import com.example.projectconnect.data.model.UserProfile
import com.google.firebase.firestore.FirebaseFirestore

class UserRepository {

    private val db = FirebaseFirestore.getInstance()
    private val usersCollection = db.collection("users")

    fun loadOrCreateUserProfile(
        defaultProfile: UserProfile,
        onResult: (UserProfile) -> Unit,
        onError: (String) -> Unit
    ) {
        usersCollection.document(defaultProfile.userId)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val profile = document.toObject(UserProfile::class.java)
                    onResult(profile ?: defaultProfile)
                } else {
                    usersCollection.document(defaultProfile.userId)
                        .set(defaultProfile)
                        .addOnSuccessListener {
                            onResult(defaultProfile)
                        }
                        .addOnFailureListener { e ->
                            onError(e.message ?: "Failed to create user profile.")
                        }
                }
            }
            .addOnFailureListener { e ->
                onError(e.message ?: "Failed to load user profile.")
            }
    }

    fun saveUserProfile(
        profile: UserProfile,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        usersCollection.document(profile.userId)
            .set(profile)
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener { e ->
                onError(e.message ?: "Failed to save user profile.")
            }
    }
}