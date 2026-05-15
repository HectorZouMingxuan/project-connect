package com.example.projectconnect.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.projectconnect.data.model.UserProfile
import com.example.projectconnect.ui.component.PrimaryActionButton
import com.example.projectconnect.ui.component.PunkTitle
import com.example.projectconnect.ui.component.SecondaryActionButton

@Composable
fun EditProfileScreen(
    userProfile: UserProfile,
    onSave: (UserProfile) -> Unit,
    onBack: () -> Unit
) {
    var username by remember { mutableStateOf(userProfile.username) }
    var bio by remember { mutableStateOf(userProfile.bio) }
    var faculty by remember { mutableStateOf(userProfile.faculty) }
    var year by remember { mutableStateOf(userProfile.year) }
    var skillsText by remember { mutableStateOf(userProfile.skills.joinToString(", ")) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        PunkTitle(text = "Edit Profile")

        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = bio,
            onValueChange = { bio = it },
            label = { Text("Bio") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = faculty,
            onValueChange = { faculty = it },
            label = { Text("Faculty") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = year,
            onValueChange = { year = it },
            label = { Text("Year") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = skillsText,
            onValueChange = { skillsText = it },
            label = { Text("Skills (comma separated)") },
            modifier = Modifier.fillMaxWidth()
        )

        PrimaryActionButton(
            text = "Save",
            onClick = {
                val updatedProfile = userProfile.copy(
                    username = username,
                    bio = bio,
                    faculty = faculty,
                    year = year,
                    skills = skillsText
                        .split(",")
                        .map { it.trim() }
                        .filter { it.isNotEmpty() }
                )
                onSave(updatedProfile)
            }
        )

        SecondaryActionButton(
            text = "Cancel",
            onClick = onBack
        )
    }
}
