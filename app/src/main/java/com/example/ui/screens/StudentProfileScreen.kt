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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.model.StudentPsychologistPairing
import com.example.ui.components.OceanicContourBackground
import com.example.ui.theme.*

@Composable
fun StudentProfileScreen(
    studentName: String,
    pairing: StudentPsychologistPairing,
    onBack: () -> Unit,
    onLogout: () -> Unit,
    onNavigateToAccompaniment: () -> Unit,
    onNavigateToPreConsultation: () -> Unit
) {
    var notificationsEnabled by remember { mutableStateOf(true) }
    var highContrastAmbient by remember { mutableStateOf(false) }

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
                        .testTag("profile_back_button"),
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
                        text = "Mi Perfil",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = OceanDeep
                    )
                    Text(
                        text = "Ajustes y preferencias personales",
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
                // Avatar and Identity Card
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
                            Box(
                                modifier = Modifier
                                    .size(72.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFE2F0F8))
                                    .border(2.5.dp, Color(0xFFFFC043), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.avatar_valentina),
                                    contentDescription = "Avatar de $studentName",
                                    modifier = Modifier
                                        .size(66.dp)
                                        .clip(CircleShape)
                                )
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            Text(
                                text = studentName,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = OceanDeep
                            )
                            Text(
                                text = "Estudiante • Bienestar Activo",
                                fontSize = 12.sp,
                                color = OceanCyan,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }

                // Pairing Status Card
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(24.dp))
                            .background(Color(0xD9FFFFFF))
                            .border(1.dp, Color(0x66FFFFFF), RoundedCornerShape(24.dp))
                            .clickable(onClick = onNavigateToAccompaniment)
                            .padding(18.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(
                                    text = "Estado de Acompañamiento",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = OceanDeep
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = if (pairing.isPaired) "Vinculado con ${pairing.psychologistName}" else "Sin profesional vinculado",
                                    fontSize = 12.sp,
                                    color = if (pairing.isPaired) Color(0xFF059669) else Color(0xFF6B8CA8)
                                )
                            }

                            TextButton(onClick = onNavigateToAccompaniment) {
                                Text(if (pairing.isPaired) "Gestionar" else "Vincular", color = OceanCyan)
                            }
                        }
                    }
                }

                // Pre-consultation Quick Access (Fase 4A)
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(24.dp))
                            .background(Color(0xD9FFFFFF))
                            .border(1.dp, Color(0x66FFFFFF), RoundedCornerShape(24.dp))
                            .clickable(onClick = onNavigateToPreConsultation)
                            .padding(18.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Ficha de Preconsulta",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = OceanDeep
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "Prepara tus motivos y expectativas antes de una cita.",
                                    fontSize = 12.sp,
                                    color = Color(0xFF557793)
                                )
                            }
                            TextButton(onClick = onNavigateToPreConsultation) {
                                Text("Editar", color = OceanCyan, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }

                // Logout session button (Fase 4A: Returns to Profile Selection)
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
                                text = "Sesión Activa",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = OceanDeep
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Sesión iniciada como Estudiante. Si deseas cambiar de perfil, cierra tu sesión actual para regresar a la pantalla de entrada.",
                                fontSize = 12.sp,
                                color = Color(0xFF6B8CA8),
                                lineHeight = 16.sp
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            OutlinedButton(
                                onClick = onLogout,
                                shape = RoundedCornerShape(20.dp),
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFD32F2F)),
                                modifier = Modifier.fillMaxWidth().testTag("student_logout_button")
                            ) {
                                Text("Cerrar Sesión")
                            }
                        }
                    }
                }

                // Preferences
                item {
                    Text(
                        text = "Preferencias",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = OceanDeep
                    )
                }

                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(24.dp))
                            .background(Color(0xD9FFFFFF))
                            .border(1.dp, Color(0x66FFFFFF), RoundedCornerShape(24.dp))
                            .padding(16.dp)
                    ) {
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Recordatorios de marea emocional",
                                    fontSize = 13.sp,
                                    color = OceanDeep
                                )
                                Switch(
                                    checked = notificationsEnabled,
                                    onCheckedChange = { notificationsEnabled = it },
                                    colors = SwitchDefaults.colors(
                                        checkedThumbColor = Color.White,
                                        checkedTrackColor = OceanCyan
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
