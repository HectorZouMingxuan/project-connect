package com.example.projectconnect.data.model

data class Project(
    val projectId: String = "",
    val title: String = "",
    val description: String = "",
    val ownerId: String = "",
    val ownerName: String = "",
    val requiredSkills: List<String> = emptyList(),
    val teamSize: Int = 1,
    val memberIds: List<String> = emptyList()
)