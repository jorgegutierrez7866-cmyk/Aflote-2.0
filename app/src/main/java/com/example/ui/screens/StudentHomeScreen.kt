package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.model.ContrapesoEnvironment
import com.example.model.EmotionRecord
import com.example.ui.components.OceanicContourBackground
import com.example.ui.theme.*

@Composable
fun StudentHomeScreen(
    studentName: String,
    currentRouteStation: String,
    activeEnvironment: ContrapesoEnvironment,
    emotionRecords: List<EmotionRecord>,
    onNavigateToCheckin: () -> Unit,
    onNavigateToRutas: () -> Unit,
    onNavigateToContrapeso: () -> Unit,
    onNavigateToActivities: () -> Unit,
    onNavigateToAI: () -> Unit,
    onNavigateToAccompaniment: () -> Unit,
    onNavigateToHistory: () -> Unit
) {
    val activeAmbientTint = Color(activeEnvironment.primaryColorHex)
    OceanicContourBackground(
        isDarkOcean = false,
        ambientTint = activeAmbientTint,
        ambientIntensity = 0.05f
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(top = 16.dp, bottom = 100.dp)
        ) {
            // Header: Aflote Logo + Avatar
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_aflote_logo),
                            contentDescription = "Aflote Logo",
                            modifier = Modifier.size(42.dp)
                        )
                        Text(
                            text = "Aflote",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            color = OceanDeep
                        )
                    }

                    // Avatar with amber notification glow ring
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFE2F0F8))
                            .border(2.dp, Color(0xFFFFC043), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.avatar_valentina),
                            contentDescription = "Avatar de Valentina",
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                        )
                    }
                }
            }

            // Greeting + "¿Cómo te sientes hoy?"
            item {
                Column(modifier = Modifier.padding(top = 8.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Hola, $studentName",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = OceanDeep
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "〜",
                            fontSize = 24.sp,
                            color = OceanCyan,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Text(
                        text = "¿Cómo te sientes hoy?",
                        fontSize = 16.sp,
                        color = Color(0xFF4A6882),
                        fontWeight = FontWeight.Normal,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }
            }

            // Registrar mi estado pill button
            item {
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(24.dp))
                        .background(Color(0xD9FFFFFF))
                        .border(1.dp, Color(0x66FFFFFF), RoundedCornerShape(24.dp))
                        .clickable(onClick = onNavigateToCheckin)
                        .testTag("registrar_mi_estado_button")
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.SentimentSatisfied,
                        contentDescription = null,
                        tint = OceanCyan,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = "Registrar mi estado",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = OceanDeep
                    )
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        tint = OceanCyan,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            // Tu ruta actual banner card
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(28.dp))
                        .background(
                            Brush.horizontalGradient(
                                colors = listOf(
                                    Color(0xE6FFFFFF),
                                    Color(0xB3E0F2FA)
                                )
                            )
                        )
                        .border(1.dp, Color(0x80FFFFFF), RoundedCornerShape(28.dp))
                        .clickable(onClick = onNavigateToRutas)
                        .testTag("banner_ruta_actual")
                        .padding(horizontal = 20.dp, vertical = 14.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            // Gold glowing compass circle
                            Box(
                                modifier = Modifier
                                    .size(46.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFF0C2B47)),
                                contentAlignment = Alignment.Center
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(38.dp)
                                        .clip(CircleShape)
                                        .border(2.dp, Color(0xFFFFC043), CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Explore,
                                        contentDescription = "Rutas",
                                        tint = Color(0xFFFFC043),
                                        modifier = Modifier.size(22.dp)
                                    )
                                }
                            }

                            Column {
                                Text(
                                    text = "Tu ruta actual:",
                                    fontSize = 12.sp,
                                    color = Color(0xFF5A7994)
                                )
                                Text(
                                    text = currentRouteStation,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = OceanDeep
                                )
                            }
                        }

                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = "Ir a Rutas",
                            tint = Color(0xFF6B8CA8),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            // Section: Tus herramientas
            item {
                Text(
                    text = "Tus herramientas",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            // 2x2 Tools Grid
            item {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    // Row 1: Contrapeso & Actividades
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        ToolCard(
                            modifier = Modifier.weight(1f),
                            icon = Icons.Outlined.Spa,
                            title = "Contrapeso",
                            subtitle = "Ambiente ${activeEnvironment.title}",
                            testTag = "tool_contrapeso",
                            onClick = onNavigateToContrapeso
                        )
                        ToolCard(
                            modifier = Modifier.weight(1f),
                            icon = Icons.Outlined.AllInclusive,
                            title = "Actividades",
                            subtitle = "Explora y aprende",
                            testTag = "tool_actividades",
                            onClick = onNavigateToActivities
                        )
                    }

                    // Row 2: Asistente IA & Acompañamiento
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        ToolCard(
                            modifier = Modifier.weight(1f),
                            icon = Icons.Outlined.ChatBubbleOutline,
                            title = "Asistente IA",
                            subtitle = "Habla, desahógate",
                            testTag = "tool_asistente_ia",
                            onClick = onNavigateToAI
                        )
                        ToolCard(
                            modifier = Modifier.weight(1f),
                            icon = Icons.Outlined.FavoriteBorder,
                            title = "Acompañamiento",
                            subtitle = "Conecta con un profesional",
                            testTag = "tool_acompanamiento",
                            onClick = onNavigateToAccompaniment
                        )
                    }
                }
            }

            // Tu progreso semanal card
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(24.dp))
                        .background(Color(0xD9FFFFFF))
                        .border(1.dp, Color(0x66FFFFFF), RoundedCornerShape(24.dp))
                        .clickable(onClick = onNavigateToHistory)
                        .testTag("card_progreso_semanal")
                        .padding(horizontal = 18.dp, vertical = 14.dp)
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
                                Icon(
                                    imageVector = Icons.Default.BarChart,
                                    contentDescription = null,
                                    tint = OceanCyan,
                                    modifier = Modifier.size(20.dp)
                                )
                                Text(
                                    text = "Tu progreso semanal",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = OceanDeep
                                )
                            }

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text(
                                    text = "Ver detalles",
                                    fontSize = 12.sp,
                                    color = OceanCyan,
                                    fontWeight = FontWeight.Medium
                                )
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                    contentDescription = null,
                                    tint = OceanCyan,
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))
                        // Real emotional moments summary
                        Text(
                            text = "${emotionRecords.size} momentos registrados esta semana • Marea predominante: ${emotionRecords.firstOrNull()?.category?.displayName ?: "En calma"}",
                            fontSize = 12.sp,
                            color = Color(0xFF5A7994)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ToolCard(
    modifier: Modifier = Modifier,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String,
    testTag: String,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .height(125.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(Color(0xD9FFFFFF))
            .border(1.dp, Color(0x80FFFFFF), RoundedCornerShape(24.dp))
            .clickable(onClick = onClick)
            .testTag(testTag)
            .padding(14.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = OceanDeep,
                modifier = Modifier.size(24.dp)
            )

            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = title,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = OceanDeep
                    )
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        tint = OceanCyan,
                        modifier = Modifier.size(14.dp)
                    )
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = subtitle,
                    fontSize = 11.sp,
                    color = Color(0xFF557793),
                    lineHeight = 14.sp,
                    maxLines = 2
                )
            }
        }
    }
}
