package com.example.projectconnect.data.repository

import com.example.projectconnect.data.model.ChatMessage
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query

class ChatRepository {

    private val db = FirebaseFirestore.getInstance()
    private val projectsCollection = db.collection("projects")

    fun listenToProjectMessages(
        projectId: String,
        onResult: (List<ChatMessage>) -> Unit,
        onError: (String) -> Unit
    ): ListenerRegistration {
        return projectsCollection
            .document(projectId)
            .collection("messages")
            .orderBy("createdAt", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshots, e ->
                if (e != null) {
                    onError(e.message ?: "Failed to listen to team chat.")
                    return@addSnapshotListener
                }

                val messages = snapshots?.documents?.mapNotNull { document ->
                    document.toObject(ChatMessage::class.java)?.copy(messageId = document.id)
                } ?: emptyList()

                onResult(messages)
            }
    }

    fun sendMessage(
        projectId: String,
        senderId: String,
        senderName: String,
        memberIds: List<String>,
        text: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        val trimmedText = text.trim()
        if (projectId.isBlank() || senderId.isBlank()) {
            onError("You must be in a project to send a message.")
            return
        }

        if (senderId !in memberIds) {
            onError("Only project members can send team chat messages.")
            return
        }

        if (trimmedText.isBlank()) {
            onError("Message cannot be empty.")
            return
        }

        val messagesCollection = projectsCollection
            .document(projectId)
            .collection("messages")
        val messageDocument = messagesCollection.document()
        val message = ChatMessage(
            messageId = messageDocument.id,
            projectId = projectId,
            senderId = senderId,
            senderName = senderName.ifBlank { "Student" },
            text = trimmedText,
            createdAt = System.currentTimeMillis()
        )

        messageDocument
            .set(message)
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener { e ->
                onError(e.message ?: "Failed to send message.")
            }
    }
}
