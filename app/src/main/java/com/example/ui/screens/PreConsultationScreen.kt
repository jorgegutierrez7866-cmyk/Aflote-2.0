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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Security
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
import com.example.model.PreConsultationData
import com.example.ui.components.OceanicContourBackground
import com.example.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun PreConsultationScreen(
    currentData: PreConsultationData,
    psychologistName: String?,
    isPaired: Boolean,
    onBack: () -> Unit,
    onSavePreConsultation: (
        reason: String,
        situation: String,
        expectations: String,
        schedulePrefs: String,
        academicContext: String,
        isShared: Boolean
    ) -> Unit
) {
    var reason by remember { mutableStateOf(currentData.reasonForConsultation) }
    var situation by remember { mutableStateOf(currentData.currentSituation) }
    var expectations by remember { mutableStateOf(currentData.expectations) }
    var schedulePrefs by remember { mutableStateOf(currentData.schedulePreferences) }
    var academicContext by remember { mutableStateOf(currentData.academicContextNotes) }
    var isShared by remember { mutableStateOf(currentData.isSharedWithPsychologist) }

    var saveNotification by remember { mutableStateOf(false) }

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
                        .testTag("preconsultation_back_button"),
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
                        text = "Información de Preconsulta",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = OceanDeep
                    )
                    Text(
                        text = "Prepara tu sesión con anticipación",
                        fontSize = 12.sp,
                        color = Color(0xFF4A6882)
                    )
                }

                Spacer(modifier = Modifier.size(40.dp))
            }

            // Privacy Guarantee Notice
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFFEAF5FC))
                    .border(1.dp, Color(0x661D95D3), RoundedCornerShape(16.dp))
                    .padding(12.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Lock,
                        contentDescription = null,
                        tint = OceanCyan,
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        text = "Privada por defecto. Solo se comparte con tu profesional si activas la autorización inferior.",
                        fontSize = 11.sp,
                        color = OceanDeep,
                        lineHeight = 15.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(14.dp),
                contentPadding = PaddingValues(bottom = 20.dp)
            ) {
                item {
                    Text(
                        text = "¿Por qué buscas acompañamiento?",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = OceanDeep
                    )
                    OutlinedTextField(
                        value = reason,
                        onValueChange = { reason = it },
                        modifier = Modifier.fillMaxWidth().testTag("preconsult_reason_input"),
                        placeholder = { Text("Ej: Manejo de sobrecarga académica o ansiedad...") },
                        shape = RoundedCornerShape(14.dp),
                        maxLines = 3
                    )
                }

                item {
                    Text(
                        text = "Situación actual que deseas comentar",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = OceanDeep
                    )
                    OutlinedTextField(
                        value = situation,
                        onValueChange = { situation = it },
                        modifier = Modifier.fillMaxWidth().testTag("preconsult_situation_input"),
                        placeholder = { Text("Describe brevemente lo que has estado experimentando...") },
                        shape = RoundedCornerShape(14.dp),
                        maxLines = 4
                    )
                }

                item {
                    Text(
                        text = "Expectativas de la consulta",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = OceanDeep
                    )
                    OutlinedTextField(
                        value = expectations,
                        onValueChange = { expectations = it },
                        modifier = Modifier.fillMaxWidth().testTag("preconsult_expectations_input"),
                        placeholder = { Text("¿Qué esperas lograr o llevarte de la sesión?") },
                        shape = RoundedCornerShape(14.dp),
                        maxLines = 2
                    )
                }

                item {
                    Text(
                        text = "Preferencias de horario o disponibilidad",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = OceanDeep
                    )
                    OutlinedTextField(
                        value = schedulePrefs,
                        onValueChange = { schedulePrefs = it },
                        modifier = Modifier.fillMaxWidth().testTag("preconsult_schedule_input"),
                        placeholder = { Text("Ej: Tardes de lunes y miércoles...") },
                        shape = RoundedCornerShape(14.dp),
                        singleLine = true
                    )
                }

                item {
                    Text(
                        text = "Contexto académico o personal adicional",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = OceanDeep
                    )
                    OutlinedTextField(
                        value = academicContext,
                        onValueChange = { academicContext = it },
                        modifier = Modifier.fillMaxWidth().testTag("preconsult_context_input"),
                        placeholder = { Text("Carrera, semestre o situaciones que sumen contexto...") },
                        shape = RoundedCornerShape(14.dp),
                        singleLine = true
                    )
                }

                // Share Toggle Card
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(20.dp))
                            .background(Color(0xD9FFFFFF))
                            .border(1.dp, Color(0x66FFFFFF), RoundedCornerShape(20.dp))
                            .padding(16.dp)
                    ) {
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "Compartir con profesional vinculado",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = OceanDeep
                                    )
                                    Text(
                                        text = if (isPaired && psychologistName != null) {
                                            "Autoriza la lectura previa a ${psychologistName}"
                                        } else {
                                            "Requiere vinculación activa para compartir"
                                        },
                                        fontSize = 11.sp,
                                        color = Color(0xFF6B8CA8)
                                    )
                                }

                                Switch(
                                    checked = isShared && isPaired,
                                    onCheckedChange = { isShared = it },
                                    enabled = isPaired,
                                    modifier = Modifier.testTag("preconsult_share_switch"),
                                    colors = SwitchDefaults.colors(
                                        checkedThumbColor = Color.White,
                                        checkedTrackColor = OceanCyan
                                    )
                                )
                            }

                            Spacer(modifier = Modifier.height(6.dp))

                            Text(
                                text = "Última actualización: ${dateFormat.format(Date(currentData.lastUpdatedMillis))}",
                                fontSize = 10.sp,
                                color = Color(0xFF8BA6BE)
                            )
                        }
                    }
                }
            }

            Button(
                onClick = {
                    onSavePreConsultation(reason, situation, expectations, schedulePrefs, academicContext, isShared)
                    saveNotification = true
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("save_preconsultation_button"),
                shape = RoundedCornerShape(26.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0A2E4E))
            ) {
                Text("Guardar Información", fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
            }

            AnimatedVisibility(visible = saveNotification) {
                Text(
                    text = "Información de preconsulta guardada correctamente.",
                    fontSize = 12.sp,
                    color = OceanCyan,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 6.dp).align(Alignment.CenterHorizontally)
                )
            }

            Spacer(modifier = Modifier.height(14.dp))
        }
    }
}
