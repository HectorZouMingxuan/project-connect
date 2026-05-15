package com.example.projectconnect.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.projectconnect.data.model.Project
import com.example.projectconnect.ui.component.DangerActionButton
import com.example.projectconnect.ui.component.PrimaryActionButton
import com.example.projectconnect.ui.component.PunkTitle
import com.example.projectconnect.ui.component.SecondaryActionButton

@Composable
fun ProjectDetailScreen(
    project: Project,
    currentUserId: String,
    onJoinProject: () -> Unit,
    onQuitProject: () -> Unit,
    onOpenTeamChat: () -> Unit,
    onDeleteProject: () -> Unit,
    onBack: () -> Unit
) {
    val isOwner = project.ownerId == currentUserId
    val alreadyJoined = currentUserId in project.memberIds
    val isFull = project.memberIds.size >= project.teamSize

    val buttonText = when {
        isOwner -> "Your Project"
        alreadyJoined -> "Joined"
        isFull -> "Project Full"
        else -> "Join Project"
    }

    val canJoin = !isOwner && !alreadyJoined && !isFull

    var showDeleteDialog by remember {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        PunkTitle(text = "Project Detail")

        Card(
            modifier = Modifier.fillMaxWidth()
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
                Text(text = "Description: ${project.description}")
                Text(text = "Required Skills: ${project.requiredSkills.joinToString(", ")}")
                Text(text = "Members: ${project.memberIds.size} / ${project.teamSize}")
            }
        }

        PrimaryActionButton(
            text = buttonText,
            onClick = onJoinProject,
            enabled = canJoin
        )

        if (alreadyJoined && !isOwner) {
            DangerActionButton(
                text = "Quit Project",
                onClick = onQuitProject
            )
        }

        if (alreadyJoined) {
            SecondaryActionButton(
                text = "Team Chat",
                onClick = onOpenTeamChat
            )
        }

        if (isOwner) {
            DangerActionButton(
                text = "Delete Project",
                onClick = {
                    showDeleteDialog = true
                }
            )
        }

        SecondaryActionButton(
            text = "Back",
            onClick = onBack
        )
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = {
                showDeleteDialog = false
            },
            title = {
                Text("Delete Project")
            },
            text = {
                Text("Are you sure you want to delete this project?")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDeleteDialog = false
                        onDeleteProject()
                    }
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showDeleteDialog = false
                    }
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}
