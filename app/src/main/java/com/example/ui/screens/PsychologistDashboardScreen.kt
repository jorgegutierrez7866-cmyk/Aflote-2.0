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
import androidx.compose.material.icons.automirrored.filled.ArrowForward
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
import com.example.model.*
import com.example.ui.components.OceanicContourBackground
import com.example.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun PsychologistDashboardScreen(
    psychologistName: String,
    activePairingCodes: List<PairingCode>,
    linkedStudents: List<LinkedStudent>,
    kpiSummary: PsychologistKpiSummary,
    authorizedEmotions: List<AuthorizedEmotionRecord>,
    authorizedPreConsultation: PreConsultationData?,
    appointments: List<Appointment>,
    onGenerateNewCode: () -> Unit,
    onRevokeCode: (String) -> Unit,
    onAddSlot: (dateLabel: String, timeLabel: String) -> Unit,
    onCancelAppointment: (String) -> Unit,
    onLogout: () -> Unit
) {
    var showNewSlotDialog by remember { mutableStateOf(false) }
    var newSlotDate by remember { mutableStateOf("Martes 17 de Noviembre") }
    var newSlotTime by remember { mutableStateOf("04:00 PM - 04:45 PM") }
    var showRevokeCodeDialog by remember { mutableStateOf<String?>(null) }

    val dateFormat = remember { SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault()) }

    // Only display appointments assigned to this psychologist
    val myAppointments = appointments.filter { it.psychologistId == "psy_sofia" }

    OceanicContourBackground(isDarkOcean = true) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(horizontal = 20.dp)
        ) {
            // Header: Psychologist Title & Logout session button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Portal Profesional",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = psychologistName,
                        fontSize = 13.sp,
                        color = Color(0xCCB3DCF2)
                    )
                }

                // Logout button returning to initial profile selection
                OutlinedButton(
                    onClick = onLogout,
                    shape = RoundedCornerShape(18.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color(0xFFFFB3B3)
                    ),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                    modifier = Modifier.testTag("psychologist_logout_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Logout,
                        contentDescription = "Cerrar Sesión",
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Cerrar Sesión", fontSize = 11.sp)
                }
            }

            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(bottom = 24.dp)
            ) {
                // Section 2: Indicadores KPI Reales (Calculados al 100% de datos en vivo)
                item {
                    Text(
                        text = "Resumen de Acompañamiento",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        KpiMetricCard(
                            modifier = Modifier.weight(1f),
                            title = "Estudiantes",
                            value = kpiSummary.activeLinkedStudentsCount.toString(),
                            subtitle = "Vinculados"
                        )
                        KpiMetricCard(
                            modifier = Modifier.weight(1f),
                            title = "Citas",
                            value = kpiSummary.bookedAppointmentsCount.toString(),
                            subtitle = "Agendadas"
                        )
                        KpiMetricCard(
                            modifier = Modifier.weight(1f),
                            title = "Horarios",
                            value = kpiSummary.availableSlotsCount.toString(),
                            subtitle = "Disponibles"
                        )
                    }
                }

                // Section 4: Códigos de Vinculación con Revocación y Expiración
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(24.dp))
                            .background(Color(0xE60A2B47))
                            .border(1.dp, Color(0x3338E1E5), RoundedCornerShape(24.dp))
                            .padding(18.dp)
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
                                        imageVector = Icons.Outlined.Key,
                                        contentDescription = null,
                                        tint = Color(0xFFFFC043),
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Text(
                                        text = "Códigos de Vinculación",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                }

                                TextButton(onClick = onGenerateNewCode) {
                                    Icon(Icons.Default.Add, contentDescription = null, tint = Color(0xFF38E1E5), modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Generar", fontSize = 12.sp, color = Color(0xFF38E1E5))
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            activePairingCodes.filter { !it.isRevoked && System.currentTimeMillis() <= it.expiresAtMillis }.take(2).forEach { codeEntry ->
                                val hoursLeft = ((codeEntry.expiresAtMillis - System.currentTimeMillis()) / 3600000L).coerceAtLeast(0)
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp)
                                        .clip(RoundedCornerShape(14.dp))
                                        .background(Color(0xFF04192E))
                                        .border(1.dp, Color(0x3338E1E5), RoundedCornerShape(14.dp))
                                        .padding(horizontal = 14.dp, vertical = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column {
                                        Text(
                                            text = codeEntry.code,
                                            fontSize = 20.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFFFFC043),
                                            letterSpacing = 4.sp
                                        )
                                        Text(
                                            text = "Vence en ${hoursLeft}h • Uso individual",
                                            fontSize = 10.sp,
                                            color = Color(0x99B3DCF2)
                                        )
                                    }

                                    OutlinedButton(
                                        onClick = { showRevokeCodeDialog = codeEntry.code },
                                        shape = RoundedCornerShape(12.dp),
                                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFE26D24)),
                                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 2.dp)
                                    ) {
                                        Text("Revocar", fontSize = 11.sp)
                                    }
                                }
                            }
                        }
                    }
                }

                // Section 3: Gestión de Estudiantes Vinculados
                item {
                    Text(
                        text = "Estudiantes Vinculados (${linkedStudents.size})",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                if (linkedStudents.isNotEmpty()) {
                    items(linkedStudents) { student ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(22.dp))
                                .background(Color(0xE60A2B47))
                                .border(1.dp, Color(0x3338E1E5), RoundedCornerShape(22.dp))
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
                                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(40.dp)
                                                .clip(CircleShape)
                                                .background(Color(0xFF0EA9CE)),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = student.displayName.take(1),
                                                fontSize = 18.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color.White
                                            )
                                        }

                                        Column {
                                            Text(
                                                text = student.displayName,
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color.White
                                            )
                                            Text(
                                                text = "Acompañamiento autorizado activo",
                                                fontSize = 11.sp,
                                                color = Color(0xFF10B981)
                                            )
                                        }
                                    }

                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(10.dp))
                                            .background(Color(0x3310B981))
                                            .padding(horizontal = 8.dp, vertical = 4.dp)
                                    ) {
                                        Text(
                                            text = "Vinculado",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF6EE7B7)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(10.dp))

                                // Permisos de datos
                                Text(
                                    text = "Permisos autorizados:",
                                    fontSize = 12.sp,
                                    color = Color(0xCCB3DCF2)
                                )
                                Text(
                                    text = "• Mareas: ${if (student.permissionsShareEmotions) "Autorizado" else "Revocado"}\n• Progreso semanal: ${if (student.permissionsShareWeeklyProgress) "Autorizado" else "Revocado"}\n• Notas privadas: Denegado por defecto",
                                    fontSize = 11.sp,
                                    color = Color(0xFF38E1E5),
                                    lineHeight = 15.sp,
                                    modifier = Modifier.padding(top = 2.dp)
                                )

                                Spacer(modifier = Modifier.height(12.dp))

                                // FASE 4A: Pre-consultation card (Strict Student-controlled Privacy)
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(16.dp))
                                        .background(Color(0xFF04192E))
                                        .border(1.dp, Color(0x3338E1E5), RoundedCornerShape(16.dp))
                                        .padding(12.dp)
                                ) {
                                    Column {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Outlined.Assignment,
                                                    contentDescription = null,
                                                    tint = if (authorizedPreConsultation != null) Color(0xFF38E1E5) else Color(0x88B3DCF2),
                                                    modifier = Modifier.size(16.dp)
                                                )
                                                Text(
                                                    text = "Ficha de Preconsulta",
                                                    fontSize = 12.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = Color.White
                                                )
                                            }

                                            Box(
                                                modifier = Modifier
                                                    .clip(RoundedCornerShape(6.dp))
                                                    .background(
                                                        if (authorizedPreConsultation != null) Color(0x3310B981) else Color(0x336B8CA8)
                                                    )
                                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                                            ) {
                                                Text(
                                                    text = if (authorizedPreConsultation != null) "Autorizada" else "Privada",
                                                    fontSize = 10.sp,
                                                    fontWeight = FontWeight.SemiBold,
                                                    color = if (authorizedPreConsultation != null) Color(0xFF6EE7B7) else Color(0xFFB3DCF2)
                                                )
                                            }
                                        }

                                        Spacer(modifier = Modifier.height(6.dp))

                                        if (authorizedPreConsultation != null) {
                                            Text(
                                                text = "Motivo de consulta:",
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color(0xFFFFC043)
                                            )
                                            Text(
                                                text = authorizedPreConsultation.reasonForConsultation,
                                                fontSize = 11.sp,
                                                color = Color.White,
                                                lineHeight = 15.sp
                                            )

                                            Spacer(modifier = Modifier.height(4.dp))
                                            Text(
                                                text = "Situación actual:",
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color(0xCCB3DCF2)
                                            )
                                            Text(
                                                text = authorizedPreConsultation.currentSituation,
                                                fontSize = 11.sp,
                                                color = Color(0xFFE2F0F8),
                                                lineHeight = 15.sp
                                            )

                                            Spacer(modifier = Modifier.height(4.dp))
                                            Text(
                                                text = "Expectativas:",
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color(0xCCB3DCF2)
                                            )
                                            Text(
                                                text = authorizedPreConsultation.expectations,
                                                fontSize = 11.sp,
                                                color = Color(0xFFE2F0F8),
                                                lineHeight = 15.sp
                                            )

                                            if (authorizedPreConsultation.schedulePreferences.isNotBlank()) {
                                                Spacer(modifier = Modifier.height(4.dp))
                                                Text(
                                                    text = "Horarios preferidos: ${authorizedPreConsultation.schedulePreferences}",
                                                    fontSize = 11.sp,
                                                    color = Color(0xFF38E1E5)
                                                )
                                            }

                                            if (authorizedPreConsultation.academicContextNotes.isNotBlank()) {
                                                Spacer(modifier = Modifier.height(4.dp))
                                                Text(
                                                    text = "Contexto académico: ${authorizedPreConsultation.academicContextNotes}",
                                                    fontSize = 11.sp,
                                                    color = Color(0xCCB3DCF2)
                                                )
                                            }
                                        } else {
                                            Text(
                                                text = "La estudiante no ha autorizado compartir su ficha de preconsulta con el profesional. La información permanece privada en su dispositivo.",
                                                fontSize = 11.sp,
                                                color = Color(0x99B3DCF2),
                                                lineHeight = 15.sp
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // Section 1: Seguimiento Emocional Clínico Autorizado
                    item {
                        Text(
                            text = "Registros de Marea Autorizados",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }

                    if (authorizedEmotions.isEmpty()) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(18.dp))
                                    .background(Color(0x330A2B47))
                                    .padding(18.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "No hay registros autorizados disponibles o la estudiante ha revocado el permiso.",
                                    fontSize = 12.sp,
                                    color = Color(0x99B3DCF2),
                                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                )
                            }
                        }
                    } else {
                        items(authorizedEmotions) { record ->
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(18.dp))
                                    .background(Color(0x400A2B47))
                                    .border(1.dp, Color(0x2638E1E5), RoundedCornerShape(18.dp))
                                    .padding(14.dp)
                            ) {
                                Column {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(
                                            text = "Marea ${record.category.displayName} (Intensidad ${record.intensity}/5)",
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White
                                        )
                                        Text(
                                            text = dateFormat.format(Date(record.timestamp)),
                                            fontSize = 11.sp,
                                            color = Color(0x99B3DCF2)
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "Emociones autorizadas: ${record.emotions.joinToString(", ")}",
                                        fontSize = 12.sp,
                                        color = Color(0xFF38E1E5)
                                    )
                                }
                            }
                        }
                    }
                } else {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(20.dp))
                                .background(Color(0x260A2B47))
                                .padding(24.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "No hay estudiantes vinculados actualmente.\nProporciona un código de 6 dígitos para iniciar un proceso.",
                                fontSize = 13.sp,
                                color = Color(0x99B3DCF2),
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                        }
                    }
                }

                // Section 5: Citas y Disponibilidad
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Agenda y Disponibilidad (${myAppointments.size})",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )

                        TextButton(onClick = { showNewSlotDialog = true }) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = null,
                                tint = Color(0xFF38E1E5),
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Abrir Horario", fontSize = 12.sp, color = Color(0xFF38E1E5))
                        }
                    }
                }

                items(myAppointments) { appt ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(18.dp))
                            .background(Color(0xE60A2B47))
                            .border(1.dp, Color(0x3338E1E5), RoundedCornerShape(18.dp))
                            .padding(14.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(
                                    text = appt.dateLabel,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Text(
                                    text = appt.timeLabel,
                                    fontSize = 12.sp,
                                    color = Color(0xCCB3DCF2)
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = if (appt.isBooked) "Reservado por: ${appt.studentName}" else "Disponible para reservar",
                                    fontSize = 11.sp,
                                    color = if (appt.isBooked) Color(0xFFFFC043) else Color(0xFF10B981),
                                    fontWeight = FontWeight.SemiBold
                                )
                            }

                            if (appt.isBooked) {
                                OutlinedButton(
                                    onClick = { onCancelAppointment(appt.id) },
                                    shape = RoundedCornerShape(14.dp),
                                    colors = ButtonDefaults.outlinedButtonColors(
                                        contentColor = Color(0xFFE26D24)
                                    ),
                                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                                ) {
                                    Text("Liberar", fontSize = 11.sp)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (showRevokeCodeDialog != null) {
        val codeToRevoke = showRevokeCodeDialog ?: ""
        AlertDialog(
            onDismissRequest = { showRevokeCodeDialog = null },
            title = { Text("Revocar código de vinculación") },
            text = { Text("¿Deseas revocar el código $codeToRevoke? Ningún estudiante podrá utilizarlo para vincularse.") },
            confirmButton = {
                Button(
                    onClick = {
                        onRevokeCode(codeToRevoke)
                        showRevokeCodeDialog = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE26D24))
                ) {
                    Text("Revocar Código")
                }
            },
            dismissButton = {
                TextButton(onClick = { showRevokeCodeDialog = null }) {
                    Text("Cancelar")
                }
            }
        )
    }

    if (showNewSlotDialog) {
        AlertDialog(
            onDismissRequest = { showNewSlotDialog = false },
            title = { Text("Abrir nuevo horario de cita") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = newSlotDate,
                        onValueChange = { newSlotDate = it },
                        label = { Text("Fecha") }
                    )
                    OutlinedTextField(
                        value = newSlotTime,
                        onValueChange = { newSlotTime = it },
                        label = { Text("Horario") }
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        onAddSlot(newSlotDate, newSlotTime)
                        showNewSlotDialog = false
                    }
                ) {
                    Text("Guardar Horario")
                }
            },
            dismissButton = {
                TextButton(onClick = { showNewSlotDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}

@Composable
private fun KpiMetricCard(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    subtitle: String
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(18.dp))
            .background(Color(0xE60A2B47))
            .border(1.dp, Color(0x3338E1E5), RoundedCornerShape(18.dp))
            .padding(12.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = value,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFFFC043)
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = title,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.White
            )
            Text(
                text = subtitle,
                fontSize = 10.sp,
                color = Color(0x99B3DCF2)
            )
        }
    }
}
