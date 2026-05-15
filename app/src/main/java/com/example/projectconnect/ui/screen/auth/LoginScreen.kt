package com.example.projectconnect.ui.screen.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.projectconnect.ui.component.PrimaryActionButton
import com.example.projectconnect.ui.component.PunkTitle
import com.example.projectconnect.ui.component.QuietActionButton
import com.example.projectconnect.ui.viewmodel.AuthViewModel

@Composable
fun LoginScreen(
    authViewModel: AuthViewModel,
    onGoToRegister: () -> Unit,
    onLoginSuccess: () -> Unit
) {
    LaunchedEffect(Unit) {
        authViewModel.clearLoginForm()
    }

    LaunchedEffect(authViewModel.isLoginSuccessful) {
        if (authViewModel.isLoginSuccessful) {
            onLoginSuccess()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        PunkTitle(
            text = "Project Connect",
            fontSizeSp = 40,
            shadowBlur = 12f,
            glitchOffsetDp = 1
        )

        Spacer(modifier = Modifier.height(8.dp))

        PunkTitle(text = "Login")

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = authViewModel.loginEmail,
            onValueChange = authViewModel::onLoginEmailChange,
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            enabled = !authViewModel.isLoading,
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = authViewModel.loginPassword,
            onValueChange = authViewModel::onLoginPasswordChange,
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth(),
            enabled = !authViewModel.isLoading,
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )

        authViewModel.errorMessage?.let { message ->
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = message,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        PrimaryActionButton(
            text = "Login",
            onClick = authViewModel::login,
            modifier = Modifier.fillMaxWidth(),
            enabled = !authViewModel.isLoading
        ) {
            if (authViewModel.isLoading) {
                CircularProgressIndicator()
            } else {
                Text("Login")
            }
        }

        QuietActionButton(
            text = "Create an account",
            onClick = onGoToRegister,
            enabled = !authViewModel.isLoading
        )
    }
}
