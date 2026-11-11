package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.MedicalServices
import androidx.compose.material.icons.outlined.School
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
import com.example.model.UserRole
import com.example.ui.components.OceanicContourBackground
import com.example.ui.theme.*

@Composable
fun ProfileSelectionScreen(
    onSelectStudent: (name: String, lastName: String, email: String, career: String) -> Unit,
    onSelectPsychologist: (name: String, lastName: String, email: String, specialty: String) -> Unit
) {
    var selectedRole by remember { mutableStateOf<UserRole?>(null) }

    // Student fields
    var studentName by remember { mutableStateOf("Valentina") }
    var studentLastName by remember { mutableStateOf("Restrepo") }
    var studentEmail by remember { mutableStateOf("valentina.restrepo@universidad.edu.co") }
    var studentCareer by remember { mutableStateOf("Ingeniería de Diseño") }

    // Psychologist fields
    var psychName by remember { mutableStateOf("Sofía") }
    var psychLastName by remember { mutableStateOf("Mendoza") }
    var psychEmail by remember { mutableStateOf("sofia.mendoza@universidad.edu.co") }
    var psychSpecialty by remember { mutableStateOf("Psicología Clínica y Bienestar") }

    var formError by remember { mutableStateOf<String?>(null) }

    OceanicContourBackground(isDarkOcean = false) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(horizontal = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            // Logo & Header
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_aflote_logo),
                    contentDescription = "Aflote Logo",
                    modifier = Modifier.size(46.dp)
                )
                Column {
                    Text(
                        text = "Aflote",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = OceanDeep
                    )
                    Text(
                        text = "Bienestar y acompañamiento universitario",
                        fontSize = 12.sp,
                        color = Color(0xFF4A6882)
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            if (selectedRole == null) {
                // Role Picker
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "Selecciona tu perfil de ingreso",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = OceanDeep
                    )
                    Text(
                        text = "Cada experiencia está diseñada específicamente para su propósito de cuidado o atención profesional.",
                        fontSize = 13.sp,
                        color = Color(0xFF557793),
                        lineHeight = 18.sp
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Option 1: Soy Estudiante
                    RoleCard(
                        title = "Soy estudiante",
                        subtitle = "Explora herramientas de regulación, registra tus mareas emocionales y gestiona tu bienestar a tu ritmo.",
                        icon = Icons.Outlined.School,
                        testTag = "select_student_role_button",
                        onClick = { selectedRole = UserRole.STUDENT }
                    )

                    // Option 2: Soy Psicólogo
                    RoleCard(
                        title = "Soy psicólogo",
                        subtitle = "Gestiona citas, genera códigos de vinculación y consulta el seguimiento autorizado de tus estudiantes.",
                        icon = Icons.Outlined.MedicalServices,
                        testTag = "select_psychologist_role_button",
                        onClick = { selectedRole = UserRole.PSYCHOLOGIST }
                    )
                }
            } else if (selectedRole == UserRole.STUDENT) {
                // Student Form
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    item {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(onClick = { selectedRole = null }) {
                                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver", tint = OceanDeep)
                            }
                            Text(
                                text = "Ingreso como Estudiante",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = OceanDeep
                            )
                        }
                        Text(
                            text = "Completa tus datos locales para crear tu perfil de acompañamiento. No se solicitan contraseñas ni información clínica sensible.",
                            fontSize = 12.sp,
                            color = Color(0xFF557793),
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }

                    item {
                        OutlinedTextField(
                            value = studentName,
                            onValueChange = { studentName = it },
                            label = { Text("Nombre") },
                            modifier = Modifier.fillMaxWidth().testTag("student_name_input"),
                            shape = RoundedCornerShape(16.dp)
                        )
                    }

                    item {
                        OutlinedTextField(
                            value = studentLastName,
                            onValueChange = { studentLastName = it },
                            label = { Text("Apellido") },
                            modifier = Modifier.fillMaxWidth().testTag("student_lastname_input"),
                            shape = RoundedCornerShape(16.dp)
                        )
                    }

                    item {
                        OutlinedTextField(
                            value = studentEmail,
                            onValueChange = { studentEmail = it },
                            label = { Text("Correo institucional") },
                            modifier = Modifier.fillMaxWidth().testTag("student_email_input"),
                            shape = RoundedCornerShape(16.dp)
                        )
                    }

                    item {
                        OutlinedTextField(
                            value = studentCareer,
                            onValueChange = { studentCareer = it },
                            label = { Text("Programa o Carrera (Opcional)") },
                            modifier = Modifier.fillMaxWidth().testTag("student_career_input"),
                            shape = RoundedCornerShape(16.dp)
                        )
                    }

                    if (formError != null) {
                        item {
                            Text(formError ?: "", color = Color(0xFFD32F2F), fontSize = 12.sp)
                        }
                    }

                    item {
                        Button(
                            onClick = {
                                if (studentName.isBlank() || studentLastName.isBlank() || studentEmail.isBlank()) {
                                    formError = "Por favor completa nombre, apellido y correo."
                                } else {
                                    onSelectStudent(studentName, studentLastName, studentEmail, studentCareer)
                                }
                            },
                            modifier = Modifier.fillMaxWidth().height(52.dp).testTag("confirm_student_entry_button"),
                            shape = RoundedCornerShape(26.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0A2E4E))
                        ) {
                            Text("Entrar a Aflote", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                        }
                    }
                }
            } else if (selectedRole == UserRole.PSYCHOLOGIST) {
                // Psychologist Form
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    item {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(onClick = { selectedRole = null }) {
                                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver", tint = OceanDeep)
                            }
                            Text(
                                text = "Ingreso como Psicólogo",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = OceanDeep
                            )
                        }
                        Text(
                            text = "Acceso a la plataforma profesional de bienestar universitario. Identificación local de demostración.",
                            fontSize = 12.sp,
                            color = Color(0xFF557793),
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }

                    item {
                        OutlinedTextField(
                            value = psychName,
                            onValueChange = { psychName = it },
                            label = { Text("Nombre") },
                            modifier = Modifier.fillMaxWidth().testTag("psych_name_input"),
                            shape = RoundedCornerShape(16.dp)
                        )
                    }

                    item {
                        OutlinedTextField(
                            value = psychLastName,
                            onValueChange = { psychLastName = it },
                            label = { Text("Apellido") },
                            modifier = Modifier.fillMaxWidth().testTag("psych_lastname_input"),
                            shape = RoundedCornerShape(16.dp)
                        )
                    }

                    item {
                        OutlinedTextField(
                            value = psychEmail,
                            onValueChange = { psychEmail = it },
                            label = { Text("Correo profesional") },
                            modifier = Modifier.fillMaxWidth().testTag("psych_email_input"),
                            shape = RoundedCornerShape(16.dp)
                        )
                    }

                    item {
                        OutlinedTextField(
                            value = psychSpecialty,
                            onValueChange = { psychSpecialty = it },
                            label = { Text("Especialidad o Área (Opcional)") },
                            modifier = Modifier.fillMaxWidth().testTag("psych_specialty_input"),
                            shape = RoundedCornerShape(16.dp)
                        )
                    }

                    if (formError != null) {
                        item {
                            Text(formError ?: "", color = Color(0xFFD32F2F), fontSize = 12.sp)
                        }
                    }

                    item {
                        Button(
                            onClick = {
                                if (psychName.isBlank() || psychLastName.isBlank() || psychEmail.isBlank()) {
                                    formError = "Por favor completa nombre, apellido y correo profesional."
                                } else {
                                    onSelectPsychologist(psychName, psychLastName, psychEmail, psychSpecialty)
                                }
                            },
                            modifier = Modifier.fillMaxWidth().height(52.dp).testTag("confirm_psych_entry_button"),
                            shape = RoundedCornerShape(26.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0A2E4E))
                        ) {
                            Text("Entrar al Portal Profesional", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun RoleCard(
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    testTag: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(Color(0xD9FFFFFF))
            .border(1.dp, Color(0x66FFFFFF), RoundedCornerShape(24.dp))
            .clickable(onClick = onClick)
            .testTag(testTag)
            .padding(20.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFE2F0F8)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = OceanCyan,
                    modifier = Modifier.size(26.dp)
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = OceanDeep
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = subtitle,
                    fontSize = 12.sp,
                    color = Color(0xFF557793),
                    lineHeight = 16.sp
                )
            }
        }
    }
}
