package com.example.projectconnect.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.projectconnect.data.model.UserProfile

@Composable
fun ProfileScreen(
    userProfile: UserProfile,
    onGoToEditProfile: () -> Unit,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Profile",
            style = MaterialTheme.typography.headlineMedium
        )

        Card {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text("Username: ${userProfile.username}")
                Text("Bio: ${userProfile.bio}")
                Text("Faculty: ${userProfile.faculty}")
                Text("Year: ${userProfile.year}")
                Text("Skills: ${userProfile.skills.joinToString(", ")}")
            }
        }

        Button(onClick = onGoToEditProfile) {
            Text("Edit Profile")
        }

        Button(onClick = onBack) {
            Text("Back To Projects")
        }
    }
}