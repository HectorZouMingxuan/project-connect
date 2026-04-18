package com.example.projectconnect.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.projectconnect.data.model.Project

@Composable
fun ProjectListScreen(
    projects: List<Project>,
    currentUserId: String,
    currentUsername: String,
    onSelectProject: (Project) -> Unit,
    onGoToCreateProject: () -> Unit,
    onGoToProfile: () -> Unit,
    onSwitchUser: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Student Projects",
            style = MaterialTheme.typography.headlineMedium
        )

        Text(text = "Current User: $currentUsername")

        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(onClick = onGoToCreateProject) {
                Text("Create Project")
            }

            Button(onClick = onGoToProfile) {
                Text("Profile")
            }

            Button(onClick = onSwitchUser) {
                Text("Switch User")
            }
        }

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(projects) { project ->
                val status = when {
                    project.ownerId == currentUserId -> "Owner"
                    currentUserId in project.memberIds -> "Joined"
                    project.memberIds.size >= project.teamSize -> "Full"
                    else -> "Open"
                }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onSelectProject(project) }
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = project.title,
                            style = MaterialTheme.typography.titleLarge
                        )
                        Text(text = "Owner: ${project.ownerName}")
                        Text(text = "Skills: ${project.requiredSkills.joinToString(", ")}")
                        Text(text = "Members: ${project.memberIds.size} / ${project.teamSize}")
                        Text(text = "Status: $status")
                    }
                }
            }
        }
    }
}