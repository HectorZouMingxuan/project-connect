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
import com.example.projectconnect.ui.component.PrimaryActionButton
import com.example.projectconnect.ui.component.PunkTitle
import com.example.projectconnect.ui.component.SecondaryActionButton

@Composable
fun CreateProjectScreen(
    onCreateProject: (
        title: String,
        description: String,
        requiredSkills: List<String>,
        teamSize: Int
    ) -> Unit,
    onBack: () -> Unit
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var skillsText by remember { mutableStateOf("") }
    var teamSizeText by remember { mutableStateOf("") }

    val parsedTeamSize = teamSizeText.toIntOrNull() ?: 0
    val isValid = title.isNotBlank() &&
            description.isNotBlank() &&
            skillsText.isNotBlank() &&
            parsedTeamSize > 0

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        PunkTitle(text = "Create Project")

        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Project Title") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Description") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = skillsText,
            onValueChange = { skillsText = it },
            label = { Text("Required Skills (comma separated)") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = teamSizeText,
            onValueChange = { teamSizeText = it },
            label = { Text("Team Size") },
            modifier = Modifier.fillMaxWidth()
        )

        PrimaryActionButton(
            text = "Publish Project",
            onClick = {
                onCreateProject(
                    title.trim(),
                    description.trim(),
                    skillsText
                        .split(",")
                        .map { it.trim() }
                        .filter { it.isNotEmpty() },
                    parsedTeamSize
                )
            },
            enabled = isValid
        )

        SecondaryActionButton(
            text = "Cancel",
            onClick = onBack
        )
    }
}
