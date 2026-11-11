package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import com.example.model.ActivityItem
import com.example.model.ActivityStatus
import com.example.ui.components.OceanicContourBackground
import com.example.ui.theme.*
import kotlinx.coroutines.delay

@Composable
fun ActivityDetailScreen(
    activity: ActivityItem,
    onBack: () -> Unit,
    onCompleteActivity: (activityId: String, durationSeconds: Int, status: ActivityStatus, reflection: String, attemptId: String) -> Unit
) {
    val attemptId = remember { "att_${System.currentTimeMillis()}_${(1000..9999).random()}" }
    var isRunning by remember { mutableStateOf(false) }
    var secondsElapsed by remember { mutableIntStateOf(0) }
    var currentStepIndex by remember { mutableIntStateOf(0) }
    var isFinished by remember { mutableStateOf(false) }
    var reflectionText by remember { mutableStateOf("") }
    var isSubmitted by remember { mutableStateOf(false) }

    // Real working timer coroutine
    LaunchedEffect(isRunning) {
        while (isRunning) {
            delay(1000L)
            secondsElapsed++
        }
    }

    val totalSteps = activity.steps.size
    val minutes = secondsElapsed / 60
    val seconds = secondsElapsed % 60
    val timeFormatted = String.format("%02d:%02d", minutes, seconds)

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
                        .clickable(onClick = {
                            if (isRunning) {
                                isRunning = false
                                onCompleteActivity(activity.id, secondsElapsed, ActivityStatus.INTERRUPTED, "", attemptId)
                            }
                            onBack()
                        })
                        .testTag("activity_detail_back_button"),
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
                        text = activity.title,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = OceanDeep,
                        maxLines = 1
                    )
                    Text(
                        text = activity.category.displayName,
                        fontSize = 12.sp,
                        color = Color(0xFF4A6882)
                    )
                }

                Spacer(modifier = Modifier.size(40.dp))
            }

            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(bottom = 20.dp)
            ) {
                // Description card
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(24.dp))
                            .background(Color(0xD9FFFFFF))
                            .border(1.dp, Color(0x66FFFFFF), RoundedCornerShape(24.dp))
                            .padding(18.dp)
                    ) {
                        Column {
                            Text(
                                text = "Propósito de la práctica",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = OceanDeep
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = activity.fullDescription,
                                fontSize = 13.sp,
                                color = Color(0xFF557793),
                                lineHeight = 18.sp
                            )
                        }
                    }
                }

                // Timer & Controls card
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(24.dp))
                            .background(Color(0xD9FFFFFF))
                            .border(1.dp, Color(0x66FFFFFF), RoundedCornerShape(24.dp))
                            .padding(20.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "Tiempo de práctica",
                                fontSize = 12.sp,
                                color = Color(0xFF6B8CA8)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = timeFormatted,
                                fontSize = 42.sp,
                                fontWeight = FontWeight.Bold,
                                color = OceanDeep
                            )

                            Spacer(modifier = Modifier.height(14.dp))

                            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                                Button(
                                    onClick = { isRunning = !isRunning },
                                    shape = RoundedCornerShape(20.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = if (isRunning) Color(0xFFE26D24) else Color(0xFF0A2E4E),
                                        contentColor = Color.White
                                    ),
                                    modifier = Modifier.testTag("timer_toggle_button")
                                ) {
                                    Icon(
                                        imageVector = if (isRunning) Icons.Default.Pause else Icons.Default.PlayArrow,
                                        contentDescription = null,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(if (isRunning) "Pausar" else "Comenzar")
                                }

                                if (secondsElapsed > 0 && !isFinished) {
                                    OutlinedButton(
                                        onClick = {
                                            isRunning = false
                                            isFinished = true
                                        },
                                        shape = RoundedCornerShape(20.dp),
                                        modifier = Modifier.testTag("finish_activity_button")
                                    ) {
                                        Text("Finalizar sesión")
                                    }
                                }
                            }
                        }
                    }
                }

                // Steps guided view
                item {
                    Text(
                        text = "Instrucciones paso a paso",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = OceanDeep
                    )
                }

                activity.steps.forEachIndexed { index, step ->
                    item {
                        val isCurrent = index == currentStepIndex
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(20.dp))
                                .background(if (isCurrent) Color(0xFFE5F4FC) else Color(0xD9FFFFFF))
                                .border(
                                    1.dp,
                                    if (isCurrent) OceanCyan else Color(0x66FFFFFF),
                                    RoundedCornerShape(20.dp)
                                )
                                .clickable { currentStepIndex = index }
                                .padding(16.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.Top,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(28.dp)
                                        .clip(CircleShape)
                                        .background(if (isCurrent) OceanCyan else Color(0xFFD4E7F2)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "${index + 1}",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isCurrent) Color.White else OceanDeep
                                    )
                                }

                                Text(
                                    text = step,
                                    fontSize = 14.sp,
                                    color = OceanDeep,
                                    lineHeight = 19.sp,
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }
                }

                // Cierre Reflexivo (Section 11: Opcional, breve, privado)
                if (isFinished) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(24.dp))
                                .background(Color(0xD9FFFFFF))
                                .border(1.dp, Color(0x66FFFFFF), RoundedCornerShape(24.dp))
                                .testTag("cierre_reflexivo_card")
                                .padding(18.dp)
                        ) {
                            Column {
                                Text(
                                    text = "Cierre Reflexivo (Opcional & Privado)",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = OceanDeep
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = activity.reflectivePrompt,
                                    fontSize = 13.sp,
                                    color = Color(0xFF4A6882)
                                )
                                Spacer(modifier = Modifier.height(10.dp))

                                OutlinedTextField(
                                    value = reflectionText,
                                    onValueChange = { reflectionText = it },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(90.dp)
                                        .testTag("reflection_input"),
                                    placeholder = {
                                        Text(
                                            "Tus respuestas son estrictamente privadas...",
                                            fontSize = 12.sp,
                                            color = Color(0xFF7A9BB8)
                                        )
                                    },
                                    shape = RoundedCornerShape(16.dp),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedContainerColor = Color.White,
                                        unfocusedContainerColor = Color.White,
                                        focusedBorderColor = OceanCyan,
                                        unfocusedBorderColor = Color(0x66FFFFFF)
                                    )
                                )

                                Spacer(modifier = Modifier.height(14.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    OutlinedButton(
                                        onClick = {
                                            if (!isSubmitted) {
                                                onCompleteActivity(
                                                    activity.id,
                                                    secondsElapsed,
                                                    ActivityStatus.COMPLETED,
                                                    "",
                                                    attemptId
                                                )
                                                isSubmitted = true
                                            }
                                        },
                                        modifier = Modifier.weight(1f),
                                        shape = RoundedCornerShape(20.dp),
                                        enabled = !isSubmitted
                                    ) {
                                        Text("Omitir reflexión", fontSize = 12.sp)
                                    }

                                    Button(
                                        onClick = {
                                            if (!isSubmitted) {
                                                onCompleteActivity(
                                                    activity.id,
                                                    secondsElapsed,
                                                    ActivityStatus.COMPLETED,
                                                    reflectionText.trim(),
                                                    attemptId
                                                )
                                                isSubmitted = true
                                            }
                                        },
                                        modifier = Modifier.weight(1f),
                                        shape = RoundedCornerShape(20.dp),
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = Color(0xFF0A2E4E),
                                            contentColor = Color.White
                                        ),
                                        enabled = !isSubmitted
                                    ) {
                                        Text("Guardar en Bitácora", fontSize = 12.sp)
                                    }
                                }

                                if (isSubmitted) {
                                    Text(
                                        text = "¡Actividad guardada en tu bitácora personal!",
                                        fontSize = 12.sp,
                                        color = OceanCyan,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(top = 8.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
