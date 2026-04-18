package com.example.projectconnect.data.model

data class UserProfile(
    val userId: String = "",
    val username: String = "",
    val bio: String = "",
    val faculty: String = "",
    val year: String = "",
    val skills: List<String> = emptyList()
)