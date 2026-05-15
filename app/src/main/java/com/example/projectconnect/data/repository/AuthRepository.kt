package com.example.projectconnect.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseUser

class AuthRepository {

    private val auth = FirebaseAuth.getInstance()

    fun getCurrentUser(): FirebaseUser? {
        return auth.currentUser
    }

    fun getCurrentUserId(): String? {
        return auth.currentUser?.uid
    }

    fun getCurrentUserEmail(): String {
        return auth.currentUser?.email.orEmpty()
    }

    fun register(
        email: String,
        password: String,
        onSuccess: (FirebaseUser) -> Unit,
        onError: (String) -> Unit
    ) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener { result ->
                val user = result.user
                if (user != null) {
                    onSuccess(user)
                } else {
                    onError("Registration succeeded but no user was returned.")
                }
            }
            .addOnFailureListener { e ->
                onError(e.toAuthMessage("Failed to register."))
            }
    }

    fun login(
        email: String,
        password: String,
        onSuccess: (FirebaseUser) -> Unit,
        onError: (String) -> Unit
    ) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener { result ->
                val user = result.user
                if (user != null) {
                    onSuccess(user)
                } else {
                    onError("Login succeeded but no user was returned.")
                }
            }
            .addOnFailureListener { e ->
                onError(e.toAuthMessage("Failed to log in."))
            }
    }

    fun logout() {
        auth.signOut()
    }

    private fun Exception.toAuthMessage(defaultMessage: String): String {
        val message = message.orEmpty()
        val errorCode = (this as? FirebaseAuthException)?.errorCode.orEmpty()

        return when {
            errorCode == "ERROR_CONFIGURATION_NOT_FOUND" ||
                message.contains("CONFIGURATION_NOT_FOUND", ignoreCase = true) ->
                "Firebase Authentication is not enabled for this project. Enable Email/Password sign-in in Firebase Console, then rebuild and reinstall the app."

            else -> message.ifBlank { defaultMessage }
        }
    }
}
