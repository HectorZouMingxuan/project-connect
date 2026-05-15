package com.example.projectconnect.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.projectconnect.data.model.ChatMessage
import com.example.projectconnect.data.model.Project
import com.example.projectconnect.ui.component.PrimaryActionButton
import com.example.projectconnect.ui.component.PunkTitle
import com.example.projectconnect.ui.component.SecondaryActionButton
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun TeamChatScreen(
    project: Project,
    messages: List<ChatMessage>,
    currentUserId: String,
    onSendMessage: (String, () -> Unit) -> Unit,
    onBack: () -> Unit
) {
    var messageText by remember {
        mutableStateOf("")
    }
    val listState = rememberLazyListState()

    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.lastIndex)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .imePadding()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        PunkTitle(text = "Team Chat")
        Text(
            text = project.title,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            state = listState,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            if (messages.isEmpty()) {
                item {
                    Text(
                        text = "No messages yet. Start the team chat.",
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            items(messages) { message ->
                ChatMessageBubble(
                    message = message,
                    isMine = message.senderId == currentUserId
                )
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = messageText,
                onValueChange = { messageText = it },
                label = { Text("Message") },
                modifier = Modifier.weight(1f),
                maxLines = 4
            )

            PrimaryActionButton(
                text = "Send",
                onClick = {
                    onSendMessage(messageText) {
                        messageText = ""
                    }
                },
                enabled = messageText.isNotBlank()
            )
        }

        SecondaryActionButton(
            text = "Back",
            onClick = onBack,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun ChatMessageBubble(
    message: ChatMessage,
    isMine: Boolean
) {
    val bubbleColor = if (isMine) {
        MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.58f)
    } else {
        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.72f)
    }
    val alignment = if (isMine) Alignment.CenterEnd else Alignment.CenterStart
    val timeText = remember(message.createdAt) {
        formatMessageTime(message.createdAt)
    }

    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = alignment
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(0.82f)
                .background(
                    color = bubbleColor,
                    shape = RoundedCornerShape(18.dp)
                )
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = if (isMine) "You" else message.senderName,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = timeText,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Text(
                text = message.text,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

private fun formatMessageTime(createdAt: Long): String {
    if (createdAt <= 0L) {
        return ""
    }

    return SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(createdAt))
}
