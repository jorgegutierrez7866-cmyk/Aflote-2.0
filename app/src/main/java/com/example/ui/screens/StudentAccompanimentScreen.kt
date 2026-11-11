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
import com.example.model.Appointment
import com.example.model.StudentPsychologistPairing
import com.example.ui.components.OceanicContourBackground
import com.example.ui.theme.*

@Composable
fun StudentAccompanimentScreen(
    pairing: StudentPsychologistPairing,
    availableAppointments: List<Appointment>,
    onBack: () -> Unit,
    onPairWithCode: (String) -> Result<String>,
    onRevokePairing: () -> Unit,
    onUpdatePermissions: (shareEmotions: Boolean, shareWeeklyProgress: Boolean) -> Unit,
    onBookAppointment: (String) -> Unit,
    onCancelAppointment: (String) -> Unit,
    onNavigateToPreConsultation: () -> Unit
) {
    var pairingCodeInput by remember { mutableStateOf("") }
    var pairingError by remember { mutableStateOf<String?>(null) }
    var pairingSuccess by remember { mutableStateOf<String?>(null) }
    var showRevokeConfirmation by remember { mutableStateOf(false) }

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
                        .testTag("accompaniment_back_button"),
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
                        text = "Acompañamiento",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = OceanDeep
                    )
                    Text(
                        text = "Tu proceso psicológico autorizado",
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
                if (!pairing.isPaired) {
                    // Not Paired: 6-Digit Pairing Flow
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(24.dp))
                                .background(Color(0xD9FFFFFF))
                                .border(1.dp, Color(0x66FFFFFF), RoundedCornerShape(24.dp))
                                .padding(20.dp)
                        ) {
                            Column {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Outlined.Key,
                                        contentDescription = null,
                                        tint = OceanCyan,
                                        modifier = Modifier.size(24.dp)
                                    )
                                    Text(
                                        text = "Vincular con tu Psicólogo",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = OceanDeep
                                    )
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                Text(
                                    text = "Ingresa el código único de 6 dígitos que te facilitó tu profesional de bienestar universitario.",
                                    fontSize = 13.sp,
                                    color = Color(0xFF557793),
                                    lineHeight = 17.sp
                                )

                                Spacer(modifier = Modifier.height(14.dp))

                                OutlinedTextField(
                                    value = pairingCodeInput,
                                    onValueChange = {
                                        if (it.length <= 6) {
                                            pairingCodeInput = it
                                            pairingError = null
                                            pairingSuccess = null
                                        }
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .testTag("pairing_code_input"),
                                    placeholder = { Text("Ej: 729401", fontSize = 14.sp) },
                                    singleLine = true,
                                    shape = RoundedCornerShape(16.dp),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedContainerColor = Color.White,
                                        unfocusedContainerColor = Color.White,
                                        focusedBorderColor = OceanCyan,
                                        unfocusedBorderColor = Color(0x66FFFFFF)
                                    )
                                )

                                if (pairingError != null) {
                                    Text(
                                        text = pairingError ?: "",
                                        fontSize = 12.sp,
                                        color = Color(0xFFD32F2F),
                                        modifier = Modifier.padding(top = 6.dp)
                                    )
                                }

                                if (pairingSuccess != null) {
                                    Text(
                                        text = pairingSuccess ?: "",
                                        fontSize = 12.sp,
                                        color = Color(0xFF10B981),
                                        fontWeight = FontWeight.SemiBold,
                                        modifier = Modifier.padding(top = 6.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.height(14.dp))

                                Button(
                                    onClick = {
                                        val result = onPairWithCode(pairingCodeInput)
                                        result.onSuccess {
                                            pairingSuccess = it
                                            pairingError = null
                                        }.onFailure {
                                            pairingError = it.message
                                            pairingSuccess = null
                                        }
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(48.dp)
                                        .testTag("submit_pairing_code_button"),
                                    shape = RoundedCornerShape(24.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color(0xFF0A2E4E),
                                        contentColor = Color.White
                                    )
                                ) {
                                    Text("Validar y Vincular", fontWeight = FontWeight.SemiBold)
                                }
                            }
                        }
                    }
                } else {
                    // Paired State
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
                                                .size(42.dp)
                                                .clip(CircleShape)
                                                .background(Color(0xFFE2F0F8)),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.MedicalServices,
                                                contentDescription = null,
                                                tint = OceanCyan,
                                                modifier = Modifier.size(24.dp)
                                            )
                                        }

                                        Column {
                                            Text(
                                                text = pairing.psychologistName ?: "Profesional",
                                                fontSize = 15.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = OceanDeep
                                            )
                                            Text(
                                                text = pairing.psychologistTitle ?: "Acompañante de Bienestar",
                                                fontSize = 12.sp,
                                                color = Color(0xFF557793)
                                            )
                                        }
                                    }

                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(12.dp))
                                            .background(Color(0xFFD1FAE5))
                                            .padding(horizontal = 8.dp, vertical = 4.dp)
                                    ) {
                                        Text(
                                            text = "Vinculado",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF065F46)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(14.dp))

                                // Privacy & Permissions Controls (Section 15: Inviolable rules)
                                Text(
                                    text = "Autorización de datos compartidos",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = OceanDeep
                                )
                                Spacer(modifier = Modifier.height(6.dp))

                                PermissionToggleRow(
                                    title = "Compartir registros de marea",
                                    subtitle = "Permite ver estados generales para la sesión",
                                    isChecked = pairing.permissionsShareEmotions,
                                    onCheckedChange = { checked ->
                                        onUpdatePermissions(checked, pairing.permissionsShareWeeklyProgress)
                                    }
                                )

                                PermissionToggleRow(
                                    title = "Compartir progreso semanal",
                                    subtitle = "Frecuencia y actividades realizadas",
                                    isChecked = pairing.permissionsShareWeeklyProgress,
                                    onCheckedChange = { checked ->
                                        onUpdatePermissions(pairing.permissionsShareEmotions, checked)
                                    }
                                )

                                // Inviolable rule item
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(14.dp))
                                        .background(Color(0xFFF3F7FA))
                                        .padding(10.dp)
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Lock,
                                            contentDescription = null,
                                            tint = Color(0xFF0EA9CE),
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Text(
                                            text = "Tus reflexiones íntimas y notas privadas NUNCA son compartidas.",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = OceanDeep
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(10.dp))

                                // Pre-consultation Card (Fase 4A)
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(16.dp))
                                        .background(Color(0xFFEBF5FA))
                                        .border(1.dp, Color(0x661D95D3), RoundedCornerShape(16.dp))
                                        .padding(12.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(
                                                text = "Información de Preconsulta",
                                                fontSize = 13.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = OceanDeep
                                            )
                                            Text(
                                                text = "Prepara tus expectativas y motivos antes de tu sesión.",
                                                fontSize = 11.sp,
                                                color = Color(0xFF557793)
                                            )
                                        }

                                        TextButton(
                                            onClick = onNavigateToPreConsultation,
                                            modifier = Modifier.testTag("open_preconsultation_button")
                                        ) {
                                            Text("Gestionar", color = OceanCyan, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(10.dp))

                                OutlinedButton(
                                    onClick = { showRevokeConfirmation = true },
                                    shape = RoundedCornerShape(20.dp),
                                    colors = ButtonDefaults.outlinedButtonColors(
                                        contentColor = Color(0xFFD32F2F)
                                    ),
                                    modifier = Modifier.align(Alignment.End)
                                ) {
                                    Text("Revocar vinculación", fontSize = 12.sp)
                                }
                            }
                        }
                    }

                    // Section 17: Sistema de Citas
                    item {
                        Text(
                            text = "Citas y Disponibilidad",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = OceanDeep
                        )
                    }

                    items(availableAppointments) { appt ->
                        val isMyBooking = appt.isBooked && appt.studentId == "val_1"
                        val isTakenByOther = appt.isBooked && appt.studentId != "val_1"

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(20.dp))
                                .background(Color(0xD9FFFFFF))
                                .border(
                                    1.dp,
                                    if (isMyBooking) OceanCyan else Color(0x66FFFFFF),
                                    RoundedCornerShape(20.dp)
                                )
                                .padding(16.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column {
                                    Text(
                                        text = appt.dateLabel,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = OceanDeep
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = appt.timeLabel,
                                        fontSize = 12.sp,
                                        color = Color(0xFF557793)
                                    )
                                }

                                when {
                                    isMyBooking -> {
                                        Button(
                                            onClick = { onCancelAppointment(appt.id) },
                                            shape = RoundedCornerShape(16.dp),
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = Color(0xFFFDE8E8),
                                                contentColor = Color(0xFF9B1C1C)
                                            ),
                                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                                        ) {
                                            Text("Cancelar Cita", fontSize = 12.sp)
                                        }
                                    }
                                    isTakenByOther -> {
                                        Text(
                                            text = "Horario reservado",
                                            fontSize = 12.sp,
                                            color = Color(0xFF9E9E9E),
                                            fontWeight = FontWeight.Medium
                                        )
                                    }
                                    else -> {
                                        Button(
                                            onClick = { onBookAppointment(appt.id) },
                                            shape = RoundedCornerShape(16.dp),
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = Color(0xFF0A2E4E),
                                                contentColor = Color.White
                                            ),
                                            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp)
                                        ) {
                                            Text("Reservar", fontSize = 12.sp)
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

    if (showRevokeConfirmation) {
        AlertDialog(
            onDismissRequest = { showRevokeConfirmation = false },
            title = { Text("Revocar vinculación") },
            text = { Text("¿Deseas desvincularte de este psicólogo? No podrá acceder a tus estados autorizados hasta que ingreses un nuevo código.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        onRevokePairing()
                        showRevokeConfirmation = false
                    }
                ) {
                    Text("Confirmar revocación", color = Color(0xFFD32F2F))
                }
            },
            dismissButton = {
                TextButton(onClick = { showRevokeConfirmation = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}

@Composable
private fun PermissionToggleRow(
    title: String,
    subtitle: String,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = OceanDeep
            )
            Text(
                text = subtitle,
                fontSize = 11.sp,
                color = Color(0xFF6B8CA8)
            )
        }

        Switch(
            checked = isChecked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = OceanCyan
            )
        )
    }
}
