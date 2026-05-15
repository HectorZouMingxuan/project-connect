package com.example.projectconnect.data.repository

import com.example.projectconnect.data.model.UserProfile
import com.google.firebase.firestore.FirebaseFirestore

class UserRepository {

    private val db = FirebaseFirestore.getInstance()
    private val usersCollection = db.collection("users")

    fun getUserProfileByUid(
        uid: String,
        onResult: (UserProfile?) -> Unit,
        onError: (String) -> Unit
    ) {
        if (uid.isBlank()) {
            onError("No logged-in user found.")
            return
        }

        usersCollection.document(uid)
            .get()
            .addOnSuccessListener { document ->
                onResult(document.toObject(UserProfile::class.java)?.copy(userId = uid))
            }
            .addOnFailureListener { e ->
                onError(e.message ?: "Failed to load user profile.")
            }
    }

    fun createUserProfileAfterRegistration(
        uid: String,
        email: String,
        onSuccess: (UserProfile) -> Unit,
        onError: (String) -> Unit
    ) {
        val profile = UserProfile(
            userId = uid,
            email = email
        )

        usersCollection.document(uid)
            .set(profile)
            .addOnSuccessListener {
                onSuccess(profile)
            }
            .addOnFailureListener { e ->
                onError(e.message ?: "Failed to create user profile.")
            }
    }

    fun loadOrCreateUserProfile(
        defaultProfile: UserProfile,
        onResult: (UserProfile) -> Unit,
        onError: (String) -> Unit
    ) {
        val uid = defaultProfile.userId
        if (uid.isBlank()) {
            onError("No logged-in user found.")
            return
        }

        usersCollection.document(uid)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val profile = document.toObject(UserProfile::class.java)
                    onResult(profile?.copy(userId = uid) ?: defaultProfile)
                } else {
                    usersCollection.document(uid)
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
        if (profile.userId.isBlank()) {
            onError("No logged-in user found.")
            return
        }

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
