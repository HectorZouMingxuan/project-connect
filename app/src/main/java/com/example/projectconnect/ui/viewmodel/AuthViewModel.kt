package com.example.projectconnect.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.projectconnect.data.repository.AuthRepository
import com.example.projectconnect.data.repository.UserRepository

class AuthViewModel(
    private val authRepository: AuthRepository = AuthRepository(),
    private val userRepository: UserRepository = UserRepository()
) : ViewModel() {

    var loginEmail by mutableStateOf("")
        private set

    var loginPassword by mutableStateOf("")
        private set

    var registerName by mutableStateOf("")
        private set

    var registerEmail by mutableStateOf("")
        private set

    var registerPassword by mutableStateOf("")
        private set

    var registerConfirmPassword by mutableStateOf("")
        private set

    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    var isLoginSuccessful by mutableStateOf(false)
        private set

    var isRegistrationSuccessful by mutableStateOf(false)
        private set

    fun onLoginEmailChange(value: String) {
        loginEmail = value
        clearAuthResult()
    }

    fun onLoginPasswordChange(value: String) {
        loginPassword = value
        clearAuthResult()
    }

    fun onRegisterNameChange(value: String) {
        registerName = value
        clearAuthResult()
    }

    fun onRegisterEmailChange(value: String) {
        registerEmail = value
        clearAuthResult()
    }

    fun onRegisterPasswordChange(value: String) {
        registerPassword = value
        clearAuthResult()
    }

    fun onRegisterConfirmPasswordChange(value: String) {
        registerConfirmPassword = value
        clearAuthResult()
    }

    fun login() {
        val email = loginEmail.trim()
        val password = loginPassword

        if (email.isBlank() || password.isBlank()) {
            errorMessage = "Email and password are required."
            return
        }

        isLoading = true
        errorMessage = null
        isLoginSuccessful = false

        authRepository.login(
            email = email,
            password = password,
            onSuccess = {
                isLoading = false
                isLoginSuccessful = true
            },
            onError = { message ->
                isLoading = false
                errorMessage = message
            }
        )
    }

    fun register() {
        val name = registerName.trim()
        val email = registerEmail.trim()
        val password = registerPassword
        val confirmPassword = registerConfirmPassword

        if (name.isBlank() || email.isBlank() || password.isBlank() || confirmPassword.isBlank()) {
            errorMessage = "Name, email, and password are required."
            return
        }

        if (password != confirmPassword) {
            errorMessage = "Passwords do not match."
            return
        }

        isLoading = true
        errorMessage = null
        isRegistrationSuccessful = false

        authRepository.register(
            email = email,
            password = password,
            onSuccess = { user ->
                userRepository.createUserProfileAfterRegistration(
                    uid = user.uid,
                    email = email,
                    onSuccess = { profile ->
                        userRepository.saveUserProfile(
                            profile = profile.copy(username = name),
                            onSuccess = {
                                isLoading = false
                                isRegistrationSuccessful = true
                            },
                            onError = { message ->
                                isLoading = false
                                errorMessage = message
                            }
                        )
                    },
                    onError = { message ->
                        isLoading = false
                        errorMessage = message
                    }
                )
            },
            onError = { message ->
                isLoading = false
                errorMessage = message
            }
        )
    }

    fun clearAuthResult() {
        errorMessage = null
        isLoginSuccessful = false
        isRegistrationSuccessful = false
    }

    fun clearLoginForm() {
        loginEmail = ""
        loginPassword = ""
        clearAuthResult()
    }

    fun clearRegisterForm() {
        registerName = ""
        registerEmail = ""
        registerPassword = ""
        registerConfirmPassword = ""
        clearAuthResult()
    }

    fun clearAuthForms() {
        clearLoginForm()
        clearRegisterForm()
    }
}
