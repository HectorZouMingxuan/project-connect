package com.example.projectconnect.data.model

data class ChatMessage(
    val messageId: String = "",
    val projectId: String = "",
    val senderId: String = "",
    val senderName: String = "",
    val text: String = "",
    val createdAt: Long = 0L
)
