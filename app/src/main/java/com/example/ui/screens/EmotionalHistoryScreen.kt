package com.example.ui.screens

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
import com.example.model.ActivityLogEntry
import com.example.model.EmotionRecord
import com.example.model.TidalCategory
import com.example.ui.components.OceanicContourBackground
import com.example.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun EmotionalHistoryScreen(
    emotionRecords: List<EmotionRecord>,
    activityLogs: List<ActivityLogEntry>,
    onBack: () -> Unit
) {
    var selectedTab by remember { mutableIntStateOf(0) } // 0: Mareas Emocionales, 1: Bitácora de Actividades, 2: Progreso Semanal

    val dateFormat = remember { SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault()) }

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
                        .testTag("history_back_button"),
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
                        text = "Historial & Progreso",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = OceanDeep
                    )
                    Text(
                        text = "Tu recorrido personal y bitácora",
                        fontSize = 12.sp,
                        color = Color(0xFF4A6882)
                    )
                }

                Spacer(modifier = Modifier.size(40.dp))
            }

            // Tab Selector
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color(0xD9FFFFFF))
                    .padding(4.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                TabButton(
                    modifier = Modifier.weight(1f),
                    title = "Mareas",
                    isSelected = selectedTab == 0,
                    onClick = { selectedTab = 0 }
                )
                TabButton(
                    modifier = Modifier.weight(1f),
                    title = "Bitácora",
                    isSelected = selectedTab == 1,
                    onClick = { selectedTab = 1 }
                )
                TabButton(
                    modifier = Modifier.weight(1f),
                    title = "Progreso",
                    isSelected = selectedTab == 2,
                    onClick = { selectedTab = 2 }
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            when (selectedTab) {
                0 -> {
                    // Mareas Emocionales History
                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        contentPadding = PaddingValues(bottom = 20.dp)
                    ) {
                        items(emotionRecords) { record ->
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(22.dp))
                                    .background(Color(0xD9FFFFFF))
                                    .border(1.dp, Color(0x66FFFFFF), RoundedCornerShape(22.dp))
                                    .padding(16.dp)
                            ) {
                                Column {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                                        ) {
                                            Box(
                                                modifier = Modifier
                                                    .size(12.dp)
                                                    .clip(CircleShape)
                                                    .background(Color(record.category.colorHex))
                                            )
                                            Text(
                                                text = "Marea ${record.category.displayName}",
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = OceanDeep
                                            )
                                        }

                                        Text(
                                            text = dateFormat.format(Date(record.timestamp)),
                                            fontSize = 11.sp,
                                            color = Color(0xFF6B8CA8)
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(8.dp))

                                    // Emotions badges
                                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                        record.emotions.forEach { emo ->
                                            Box(
                                                modifier = Modifier
                                                    .clip(RoundedCornerShape(12.dp))
                                                    .background(Color(0xFFE2F0F8))
                                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                                            ) {
                                                Text(
                                                    text = emo,
                                                    fontSize = 11.sp,
                                                    color = OceanCyan,
                                                    fontWeight = FontWeight.Medium
                                                )
                                            }
                                        }
                                        // Intensity
                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(12.dp))
                                                .background(Color(0xFFFFF2D6))
                                                .padding(horizontal = 8.dp, vertical = 4.dp)
                                        ) {
                                            Text(
                                                text = "Intensidad: ${record.intensity}/5",
                                                fontSize = 11.sp,
                                                color = Color(0xFFB45309),
                                                fontWeight = FontWeight.SemiBold
                                            )
                                        }
                                    }

                                    if (record.contextNotes.isNotEmpty()) {
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Text(
                                            text = record.contextNotes,
                                            fontSize = 12.sp,
                                            color = Color(0xFF557793),
                                            lineHeight = 16.sp
                                        )
                                    }
                                }
                            }
                        }

                        if (emotionRecords.isEmpty()) {
                            item {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 40.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "Aún no tienes registros de marea.\nComienza con 'Registrar mi estado' en Inicio.",
                                        fontSize = 13.sp,
                                        color = Color(0xFF6B8CA8),
                                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                    )
                                }
                            }
                        }
                    }
                }

                1 -> {
                    // Bitácora de Actividades (Section 12)
                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        contentPadding = PaddingValues(bottom = 20.dp)
                    ) {
                        items(activityLogs) { log ->
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(22.dp))
                                    .background(Color(0xD9FFFFFF))
                                    .border(1.dp, Color(0x66FFFFFF), RoundedCornerShape(22.dp))
                                    .padding(16.dp)
                            ) {
                                Column {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(
                                            text = log.title,
                                            fontSize = 15.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = OceanDeep
                                        )
                                        Text(
                                            text = dateFormat.format(Date(log.timestamp)),
                                            fontSize = 11.sp,
                                            color = Color(0xFF6B8CA8)
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(4.dp))
                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = log.categoryName,
                                            fontSize = 12.sp,
                                            color = OceanCyan,
                                            fontWeight = FontWeight.Medium
                                        )
                                        Text(
                                            text = "•",
                                            fontSize = 12.sp,
                                            color = Color(0xFF8BA6BE)
                                        )
                                        Text(
                                            text = "Duración: ${log.durationSeconds / 60} min ${log.durationSeconds % 60} s",
                                            fontSize = 12.sp,
                                            color = Color(0xFF557793)
                                        )
                                    }

                                    if (log.privateReflection.isNotEmpty()) {
                                        Spacer(modifier = Modifier.height(10.dp))
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clip(RoundedCornerShape(14.dp))
                                                .background(Color(0xFFEAF5FC))
                                                .padding(12.dp)
                                        ) {
                                            Column {
                                                Row(
                                                    verticalAlignment = Alignment.CenterVertically,
                                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                                ) {
                                                    Icon(
                                                        imageVector = Icons.Outlined.Lock,
                                                        contentDescription = null,
                                                        tint = Color(0xFF4A7496),
                                                        modifier = Modifier.size(14.dp)
                                                    )
                                                    Text(
                                                        text = "Reflexión Personal Privada",
                                                        fontSize = 11.sp,
                                                        fontWeight = FontWeight.Bold,
                                                        color = Color(0xFF4A7496)
                                                    )
                                                }
                                                Spacer(modifier = Modifier.height(4.dp))
                                                Text(
                                                    text = log.privateReflection,
                                                    fontSize = 12.sp,
                                                    color = OceanDeep,
                                                    lineHeight = 16.sp
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        if (activityLogs.isEmpty()) {
                            item {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 40.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "Tu bitácora está limpia.\nAl realizar actividades y cierres reflexivos, aparecerán aquí.",
                                        fontSize = 13.sp,
                                        color = Color(0xFF6B8CA8),
                                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                    )
                                }
                            }
                        }
                    }
                }

                2 -> {
                    // Progreso Semanal (Section 5.5: Sin racha ni presión competitiva, acotado estrictamente a 7 días)
                    val sevenDaysAgo = remember { System.currentTimeMillis() - 7 * 86400000L }
                    val weeklyEmotions = remember(emotionRecords) {
                        emotionRecords.filter { it.timestamp >= sevenDaysAgo }
                    }
                    val weeklyActivities = remember(activityLogs) {
                        activityLogs.filter { it.timestamp >= sevenDaysAgo && it.status == com.example.model.ActivityStatus.COMPLETED }
                    }

                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(14.dp),
                        contentPadding = PaddingValues(bottom = 20.dp)
                    ) {
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
                                        text = "Tu espacio de observación semanal",
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = OceanDeep
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "Un recorrido para observar cómo fluyen tus momentos en los últimos 7 días, sin calificaciones ni exigencias.",
                                        fontSize = 12.sp,
                                        color = Color(0xFF557793),
                                        lineHeight = 16.sp
                                    )

                                    Spacer(modifier = Modifier.height(16.dp))

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        MetricItem(
                                            title = "Mareas registradas",
                                            value = weeklyEmotions.size.toString()
                                        )
                                        MetricItem(
                                            title = "Actividades hechas",
                                            value = weeklyActivities.size.toString()
                                        )
                                        MetricItem(
                                            title = "Tiempo invertido",
                                            value = "${weeklyActivities.sumOf { it.durationSeconds } / 60} min"
                                        )
                                    }
                                }
                            }
                        }

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
                                        text = "Distribución de tus mareas",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = OceanDeep
                                    )
                                    Spacer(modifier = Modifier.height(10.dp))

                                    TidalCategory.values().forEach { cat ->
                                        val count = emotionRecords.count { it.category == cat }
                                        val pct = if (emotionRecords.isNotEmpty()) count.toFloat() / emotionRecords.size else 0f
                                        Column(modifier = Modifier.padding(vertical = 4.dp)) {
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceBetween
                                            ) {
                                                Text(
                                                    text = cat.displayName,
                                                    fontSize = 12.sp,
                                                    color = OceanDeep
                                                )
                                                Text(
                                                    text = "$count (${(pct * 100).toInt()}%)",
                                                    fontSize = 12.sp,
                                                    color = Color(0xFF6B8CA8)
                                                )
                                            }
                                            Spacer(modifier = Modifier.height(4.dp))
                                            LinearProgressIndicator(
                                                progress = { pct },
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .height(6.dp)
                                                    .clip(RoundedCornerShape(3.dp)),
                                                color = Color(cat.colorHex),
                                                trackColor = Color(0xFFE2F0F8)
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
    }
}

@Composable
private fun MetricItem(title: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = OceanCyan
        )
        Text(
            text = title,
            fontSize = 11.sp,
            color = Color(0xFF6B8CA8)
        )
    }
}

@Composable
private fun TabButton(
    modifier: Modifier = Modifier,
    title: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(if (isSelected) OceanCyan else Color.Transparent)
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = title,
            fontSize = 12.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            color = if (isSelected) Color.White else OceanDeep
        )
    }
}
