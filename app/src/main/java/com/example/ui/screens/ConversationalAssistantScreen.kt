package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.OceanicContourBackground
import com.example.ui.theme.*

data class ChatMessage(
    val id: String,
    val isUser: Boolean,
    val text: String,
    val isCrisisWarning: Boolean = false
)

@Composable
fun ConversationalAssistantScreen(
    onBack: () -> Unit
) {
    var inputText by remember { mutableStateOf("") }
    var messages by remember {
        mutableStateOf(
            listOf(
                ChatMessage(
                    id = "msg_1",
                    isUser = false,
                    text = "Hola, soy el asistente de bienestar de Aflote. Este es un espacio seguro para desahogarte y poner en palabras lo que sientes. Recuerda que no reemplazo a un profesional de salud mental."
                )
            )
        )
    }

    OceanicContourBackground(isDarkOcean = false) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(horizontal = 20.dp)
        ) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color(0xD9FFFFFF))
                        .clickable(onClick = onBack)
                        .testTag("ai_assistant_back_button"),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Regresar",
                        tint = OceanDeep,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Asistente IA",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = OceanDeep
                    )
                    Text(
                        text = "Habla, desahógate a tu ritmo",
                        fontSize = 12.sp,
                        color = Color(0xFF4A6882)
                    )
                }

                Spacer(modifier = Modifier.size(40.dp))
            }

            // Responsible Crisis Protocol Disclaimer (Section 18)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFFE5F3FA))
                    .border(1.dp, Color(0x661D95D3), RoundedCornerShape(16.dp))
                    .padding(12.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Info,
                        contentDescription = null,
                        tint = OceanCyan,
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        text = "Línea de Crisis y Apoyo Psicológico Nacional: 106 / Bienestar Universitario. Servicio confidencial 24/7.",
                        fontSize = 11.sp,
                        color = OceanDeep,
                        lineHeight = 15.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Chat Messages List
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(vertical = 10.dp)
            ) {
                items(messages) { msg ->
                    ChatBubble(msg = msg)
                }
            }

            // Chat Input Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = inputText,
                    onValueChange = { inputText = it },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("ai_chat_input"),
                    placeholder = { Text("Escribe cómo te sientes...", fontSize = 13.sp) },
                    shape = RoundedCornerShape(24.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color(0xD9FFFFFF),
                        unfocusedContainerColor = Color(0xD9FFFFFF),
                        focusedBorderColor = OceanCyan,
                        unfocusedBorderColor = Color(0x66FFFFFF)
                    ),
                    maxLines = 3
                )

                IconButton(
                    onClick = {
                        val trimmed = inputText.trim()
                        if (trimmed.isNotEmpty()) {
                            val userMsg = ChatMessage(
                                id = "msg_${System.currentTimeMillis()}",
                                isUser = true,
                                text = trimmed
                            )
                            // Section 18: Crisis check & responsible accompanying
                            val isCrisis = trimmed.contains("morir", ignoreCase = true) ||
                                    trimmed.contains("suicid", ignoreCase = true) ||
                                    trimmed.contains("no puedo más", ignoreCase = true)

                            val botResponse = if (isCrisis) {
                                ChatMessage(
                                    id = "msg_bot_${System.currentTimeMillis()}",
                                    isUser = false,
                                    text = "Siento mucho que estés atravesando por tanto dolor. En Aflote nos preocupamos por tu seguridad. Por favor, comunícate de inmediato con la Línea Nacional de Asistencia Psicológica marcando 106 o acude al Centro de Bienestar Universitario. No estás solo.",
                                    isCrisisWarning = true
                                )
                            } else {
                                ChatMessage(
                                    id = "msg_bot_${System.currentTimeMillis()}",
                                    isUser = false,
                                    text = "Te escucho con atención. Es completamente comprensible experimentar mareas emocionales como esta en medio de la vida universitaria. ¿Te ayudaría realizar una respiración guiada o prefieres seguir contándome?"
                                )
                            }
                            messages = messages + userMsg + botResponse
                            inputText = ""
                        }
                    },
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF0A2E4E))
                        .testTag("send_ai_message_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Send,
                        contentDescription = "Enviar",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun ChatBubble(msg: ChatMessage) {
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = if (msg.isUser) Alignment.CenterEnd else Alignment.CenterStart
    ) {
        Box(
            modifier = Modifier
                .widthIn(max = 280.dp)
                .clip(
                    RoundedCornerShape(
                        topStart = 18.dp,
                        topEnd = 18.dp,
                        bottomStart = if (msg.isUser) 18.dp else 4.dp,
                        bottomEnd = if (msg.isUser) 4.dp else 18.dp
                    )
                )
                .background(
                    when {
                        msg.isCrisisWarning -> Color(0xFFFEE2E2)
                        msg.isUser -> Color(0xFF0A2E4E)
                        else -> Color(0xD9FFFFFF)
                    }
                )
                .border(
                    1.dp,
                    if (msg.isCrisisWarning) Color(0xFFEF4444) else Color(0x40FFFFFF),
                    RoundedCornerShape(18.dp)
                )
                .padding(14.dp)
        ) {
            Text(
                text = msg.text,
                fontSize = 13.sp,
                color = when {
                    msg.isCrisisWarning -> Color(0xFF991B1B)
                    msg.isUser -> Color.White
                    else -> OceanDeep
                },
                lineHeight = 18.sp
            )
        }
    }
}
