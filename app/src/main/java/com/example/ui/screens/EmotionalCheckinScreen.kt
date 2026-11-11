package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
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
import com.example.model.TidalCategory
import com.example.ui.components.OceanicContourBackground
import com.example.ui.theme.*

@Composable
fun EmotionalCheckinScreen(
    onBack: () -> Unit,
    onSaveRecord: (category: TidalCategory, emotions: List<String>, intensity: Int, context: String) -> Unit
) {
    var selectedCategory by remember { mutableStateOf(TidalCategory.TRANQUILA) }
    var selectedEmotions by remember { mutableStateOf<Set<String>>(emptySet()) }
    var intensity by remember { mutableIntStateOf(3) }
    var contextText by remember { mutableStateOf("") }
    var validationError by remember { mutableStateOf<String?>(null) }
    var isSubmitted by remember { mutableStateOf(false) }

    val emotionsForCategory = when (selectedCategory) {
        TidalCategory.TRANQUILA -> listOf("Paz", "Alivio", "Gratitud", "Serenidad", "Confianza")
        TidalCategory.MOVIDA -> listOf("Inquietud", "Expectativa", "Curiosidad", "Agitación", "Duda")
        TidalCategory.ALTA -> listOf("Euforia", "Entusiasmo", "Impulso", "Sobrecarga", "Urgencia")
        TidalCategory.TORMENTA -> listOf("Agobio", "Tristeza", "Frustración", "Cansancio", "Vulnerabilidad")
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
                        .testTag("checkin_back_button"),
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
                        text = "Registro Emocional",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = OceanDeep
                    )
                    Text(
                        text = "¿Cómo está tu marea hoy?",
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
                // 1. Selector de Mareas (4 categorías de marea)
                item {
                    Text(
                        text = "1. Selecciona el estado de tu marea",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = OceanDeep
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        TidalCategory.values().forEach { cat ->
                            val isSelected = cat == selectedCategory
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(if (isSelected) Color(cat.colorHex) else Color(0xD9FFFFFF))
                                    .border(
                                        1.dp,
                                        if (isSelected) Color(cat.colorHex) else Color(0x66FFFFFF),
                                        RoundedCornerShape(16.dp)
                                    )
                                    .clickable {
                                        selectedCategory = cat
                                        selectedEmotions = emptySet()
                                    }
                                    .testTag("marea_${cat.name.lowercase()}")
                                    .padding(vertical = 10.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = cat.displayName,
                                    fontSize = 12.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isSelected) Color.White else OceanDeep
                                )
                            }
                        }
                    }

                    Text(
                        text = selectedCategory.description,
                        fontSize = 12.sp,
                        color = Color(0xFF5A7994),
                        modifier = Modifier.padding(top = 6.dp, start = 4.dp)
                    )
                }

                // 2. Selección de Emociones (Validación: al menos una seleccionada)
                item {
                    Text(
                        text = "2. Identifica tus emociones específicas (mínimo 1)*",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = OceanDeep
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        emotionsForCategory.take(3).forEach { emo ->
                            val isSelected = selectedEmotions.contains(emo)
                            FilterChip(
                                selected = isSelected,
                                onClick = {
                                    selectedEmotions = if (isSelected) {
                                        selectedEmotions - emo
                                    } else {
                                        selectedEmotions + emo
                                    }
                                    validationError = null
                                },
                                label = { Text(emo, fontSize = 12.sp) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = OceanCyan,
                                    selectedLabelColor = Color.White
                                )
                            )
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        emotionsForCategory.drop(3).forEach { emo ->
                            val isSelected = selectedEmotions.contains(emo)
                            FilterChip(
                                selected = isSelected,
                                onClick = {
                                    selectedEmotions = if (isSelected) {
                                        selectedEmotions - emo
                                    } else {
                                        selectedEmotions + emo
                                    }
                                    validationError = null
                                },
                                label = { Text(emo, fontSize = 12.sp) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = OceanCyan,
                                    selectedLabelColor = Color.White
                                )
                            )
                        }
                    }
                }

                // 3. Nivel de Intensidad (1 a 5)
                item {
                    Text(
                        text = "3. Intensidad percibida (1 a 5)",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = OceanDeep
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        (1..5).forEach { level ->
                            val isSelected = level == intensity
                            Box(
                                modifier = Modifier
                                    .size(46.dp)
                                    .clip(CircleShape)
                                    .background(if (isSelected) OceanCyan else Color(0xD9FFFFFF))
                                    .border(
                                        1.dp,
                                        if (isSelected) OceanCyan else Color(0x66FFFFFF),
                                        CircleShape
                                    )
                                    .clickable { intensity = level }
                                    .testTag("intensity_$level"),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = level.toString(),
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelected) Color.White else OceanDeep
                                )
                            }
                        }
                    }
                }

                // 4. Notas de Contexto (Opcional)
                item {
                    Text(
                        text = "4. Notas de contexto (Opcional)",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = OceanDeep
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = contextText,
                        onValueChange = { contextText = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp)
                            .testTag("context_notes_input"),
                        placeholder = {
                            Text(
                                "Cuéntate qué ha estado ocurriendo hoy...",
                                fontSize = 13.sp,
                                color = Color(0xFF7A9BB8)
                            )
                        },
                        shape = RoundedCornerShape(18.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color(0xD9FFFFFF),
                            unfocusedContainerColor = Color(0xD9FFFFFF),
                            focusedBorderColor = OceanCyan,
                            unfocusedBorderColor = Color(0x66FFFFFF)
                        )
                    )
                }

                if (validationError != null) {
                    item {
                        Text(
                            text = validationError ?: "",
                            fontSize = 12.sp,
                            color = Color(0xFFD32F2F),
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            // Confirm Button
            Button(
                onClick = {
                    if (selectedEmotions.isEmpty()) {
                        validationError = "Por favor selecciona al menos una emoción para guardar tu registro."
                        return@Button
                    }
                    onSaveRecord(
                        selectedCategory,
                        selectedEmotions.toList(),
                        intensity,
                        contextText.trim()
                    )
                    isSubmitted = true
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .testTag("guardar_registro_emocional_button"),
                shape = RoundedCornerShape(28.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF0A2E4E),
                    contentColor = Color.White
                )
            ) {
                Text(
                    text = if (isSubmitted) "¡Registro Guardado!" else "Confirmar registro emocional",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
