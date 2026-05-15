package com.example.projectconnect.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.projectconnect.data.model.UserProfile
import com.example.projectconnect.ui.component.DangerActionButton
import com.example.projectconnect.ui.component.PrimaryActionButton
import com.example.projectconnect.ui.component.PunkTitle
import com.example.projectconnect.ui.component.SecondaryActionButton

@Composable
fun ProfileScreen(
    userProfile: UserProfile,
    onGoToEditProfile: () -> Unit,
    onLogout: () -> Unit,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        PunkTitle(text = "Profile")

        Card {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text("Username: ${userProfile.username}")
                if (userProfile.email.isNotBlank()) {
                    Text("Email: ${userProfile.email}")
                }
                Text("Bio: ${userProfile.bio}")
                Text("Faculty: ${userProfile.faculty}")
                Text("Year: ${userProfile.year}")
                Text("Skills: ${userProfile.skills.joinToString(", ")}")
            }
        }

        PrimaryActionButton(
            text = "Edit Profile",
            onClick = onGoToEditProfile
        )

        DangerActionButton(
            text = "Logout",
            onClick = onLogout
        )

        SecondaryActionButton(
            text = "Back To Projects",
            onClick = onBack
        )
    }
}
