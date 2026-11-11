package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.Water
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.ContrapesoEnvironment
import com.example.ui.components.OceanicContourBackground
import com.example.ui.theme.*

@Composable
fun ContrapesoScreen(
    currentActiveEnv: ContrapesoEnvironment,
    onBack: () -> Unit,
    onActivateEnvironment: (ContrapesoEnvironment) -> Unit
) {
    // FASE 4B.1: Persist preview selection with rememberSaveable across activity recreations
    var selectedEnv by rememberSaveable { mutableStateOf(currentActiveEnv) }
    var activationFeedback by remember { mutableStateOf<String?>(null) }

    // Atmospheric preview tint that gently modulates the background to preview the chosen environment
    val atmosphericPreviewTint = Color(selectedEnv.primaryColorHex)

    OceanicContourBackground(
        isDarkOcean = false,
        ambientTint = atmosphericPreviewTint,
        ambientIntensity = 0.12f
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(horizontal = 20.dp)
        ) {
            // Top App Bar
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
                        .testTag("contrapeso_back_button"),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Regresar al inicio",
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
                        text = "Contrapeso",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = OceanDeep
                    )
                    Text(
                        text = "Elige tu ambiente y transforma tu experiencia",
                        fontSize = 12.sp,
                        color = Color(0xFF1E3A56),
                        lineHeight = 16.sp
                    )
                }

                Spacer(modifier = Modifier.size(40.dp)) // balance layout
            }

            Spacer(modifier = Modifier.height(8.dp))

            // 2x2 Grid of 4 Ambientes wrapped in selectableGroup for TalkBack accessibility
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .selectableGroup(),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        // Calmar Card
                        AmbienteCard(
                            modifier = Modifier.weight(1f),
                            env = ContrapesoEnvironment.CALMAR,
                            isActive = currentActiveEnv == ContrapesoEnvironment.CALMAR,
                            isSelected = selectedEnv == ContrapesoEnvironment.CALMAR,
                            onClick = {
                                selectedEnv = ContrapesoEnvironment.CALMAR
                                activationFeedback = null
                            }
                        )

                        // Recuperar Card
                        AmbienteCard(
                            modifier = Modifier.weight(1f),
                            env = ContrapesoEnvironment.RECUPERAR,
                            isActive = currentActiveEnv == ContrapesoEnvironment.RECUPERAR,
                            isSelected = selectedEnv == ContrapesoEnvironment.RECUPERAR,
                            onClick = {
                                selectedEnv = ContrapesoEnvironment.RECUPERAR
                                activationFeedback = null
                            }
                        )
                    }
                }

                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        // Equilibrar Card
                        AmbienteCard(
                            modifier = Modifier.weight(1f),
                            env = ContrapesoEnvironment.EQUILIBRAR,
                            isActive = currentActiveEnv == ContrapesoEnvironment.EQUILIBRAR,
                            isSelected = selectedEnv == ContrapesoEnvironment.EQUILIBRAR,
                            onClick = {
                                selectedEnv = ContrapesoEnvironment.EQUILIBRAR
                                activationFeedback = null
                            }
                        )

                        // Concentrar Card
                        AmbienteCard(
                            modifier = Modifier.weight(1f),
                            env = ContrapesoEnvironment.CONCENTRAR,
                            isActive = currentActiveEnv == ContrapesoEnvironment.CONCENTRAR,
                            isSelected = selectedEnv == ContrapesoEnvironment.CONCENTRAR,
                            onClick = {
                                selectedEnv = ContrapesoEnvironment.CONCENTRAR
                                activationFeedback = null
                            }
                        )
                    }
                }

                // FASE 4B.1: Vista Previa Atmosférica Integrada
                item {
                    AtmosphericPreviewPanel(
                        selectedEnv = selectedEnv,
                        isActive = currentActiveEnv == selectedEnv
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Feedback de activación perceptible
            AnimatedVisibility(
                visible = activationFeedback != null,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                activationFeedback?.let { feedbackMessage ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color(0xE6E2F8EE))
                            .border(1.dp, Color(0x6610B981), RoundedCornerShape(16.dp))
                            .padding(horizontal = 16.dp, vertical = 10.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = Color(0xFF059669),
                                modifier = Modifier.size(18.dp)
                            )
                            Text(
                                text = feedbackMessage,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF065F46)
                            )
                        }
                    }
                }
            }

            // FASE 4B.1: Botón de Activación Claro y Diferenciado
            val isAlreadyActive = currentActiveEnv == selectedEnv
            Button(
                onClick = {
                    if (!isAlreadyActive) {
                        onActivateEnvironment(selectedEnv)
                        activationFeedback = "¡Ambiente ${selectedEnv.title} activado para tu experiencia en Aflote!"
                    } else {
                        activationFeedback = "El ambiente ${selectedEnv.title} ya se encuentra activo en tu día."
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .testTag("activar_ambiente_button"),
                shape = RoundedCornerShape(28.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isAlreadyActive) Color(0xFF0F3B63) else Color(0xFF0A2E4E),
                    contentColor = Color.White
                )
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (isAlreadyActive) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = Color(0xFF6EE7B7),
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = "Ambiente ${selectedEnv.title} Activo",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    } else {
                        Text(
                            text = "Activar ambiente ${selectedEnv.title}",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

/**
 * Tarjeta individual de ambiente con ilustración vectorial orgánica estratificada
 * y clara diferenciación de estados: "Activo" (badge permanente) vs "Previsualizado" (borde de foco).
 */
@Composable
private fun AmbienteCard(
    modifier: Modifier = Modifier,
    env: ContrapesoEnvironment,
    isActive: Boolean,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val borderColor = when {
        isSelected -> Color(env.primaryColorHex)
        isActive -> Color(0xFF0A2E4E)
        else -> Color(0x66FFFFFF)
    }

    val borderWidth = when {
        isSelected -> 2.5.dp
        isActive -> 1.5.dp
        else -> 1.dp
    }

    Box(
        modifier = modifier
            .height(205.dp)
            .clip(RoundedCornerShape(26.dp))
            .background(Color(0xF2FFFFFF))
            .border(
                width = borderWidth,
                color = borderColor,
                shape = RoundedCornerShape(26.dp)
            )
            .clickable(onClick = onClick)
            .semantics {
                role = Role.RadioButton
                selected = isSelected
                stateDescription = when {
                    isActive && isSelected -> "Ambiente activo y previsualizado"
                    isActive -> "Ambiente activo"
                    isSelected -> "Ambiente previsualizado"
                    else -> "No seleccionado"
                }
            }
            .testTag("ambiente_card_${env.name.lowercase()}")
            .padding(8.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Ilustración vectorial estratificada con curvas orgánicas y orbe
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(98.dp)
                    .clip(RoundedCornerShape(20.dp))
            ) {
                // Dibujo vectorial nativo en Canvas
                VectorAmbienteCanvas(
                    env = env,
                    modifier = Modifier.fillMaxSize()
                )

                // Insignia permanente "ACTIVO": Nunca desaparece de la tarjeta activa aunque el usuario explore otra
                if (isActive) {
                    Box(
                        modifier = Modifier
                            .padding(8.dp)
                            .align(Alignment.TopEnd)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFF0A2E4E))
                            .border(1.dp, Color(0x66FFFFFF), RoundedCornerShape(12.dp))
                            .padding(horizontal = 7.dp, vertical = 3.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(3.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Activo en Aflote",
                                tint = Color(0xFF6EE7B7),
                                modifier = Modifier.size(11.dp)
                            )
                            Text(
                                text = "ACTIVO",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                letterSpacing = 0.5.sp
                            )
                        }
                    }
                } else if (isSelected) {
                    // Indicador sutil de previsualización en la tarjeta
                    Box(
                        modifier = Modifier
                            .padding(8.dp)
                            .align(Alignment.TopEnd)
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0xCC0EA9CE))
                            .padding(horizontal = 6.dp, vertical = 2.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "EXPLORANDO",
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Textos informativos con alto contraste (≥ 5.5:1)
            Column(modifier = Modifier.padding(horizontal = 6.dp)) {
                Text(
                    text = env.title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = OceanDeep
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = env.subtitle,
                    fontSize = 11.sp,
                    color = Color(0xFF1E3A56),
                    fontWeight = FontWeight.Medium,
                    lineHeight = 14.sp
                )
            }
        }
    }
}

/**
 * Dibuja la composición batimétrica/oceánica de 3 estratos de ondas fluidas y orbes
 * directamente en Compose con Canvas y curvas Bézier, eliminando los rectángulos planos.
 */
@Composable
private fun VectorAmbienteCanvas(
    env: ContrapesoEnvironment,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height

        when (env) {
            ContrapesoEnvironment.CALMAR -> {
                // 1. Fondo base azul cian profundo
                drawRect(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color(0xFF0B6396), Color(0xFF073859))
                    )
                )

                // 2. Estrato 1: Oleaje cerúleo profundo
                val path1 = Path().apply {
                    moveTo(0f, h * 0.40f)
                    cubicTo(w * 0.35f, h * 0.28f, w * 0.65f, h * 0.52f, w, h * 0.38f)
                    lineTo(w, h)
                    lineTo(0f, h)
                    close()
                }
                drawPath(path1, color = Color(0xFF1D95D3).copy(alpha = 0.65f))

                // 3. Estrato 2: Oleaje intermedio turquesa
                val path2 = Path().apply {
                    moveTo(0f, h * 0.58f)
                    cubicTo(w * 0.30f, h * 0.70f, w * 0.70f, h * 0.46f, w, h * 0.62f)
                    lineTo(w, h)
                    lineTo(0f, h)
                    close()
                }
                drawPath(path2, color = Color(0xFF70C2E8).copy(alpha = 0.75f))

                // 4. Estrato 3: Cresta frontal luminosa
                val path3 = Path().apply {
                    moveTo(0f, h * 0.78f)
                    cubicTo(w * 0.45f, h * 0.68f, w * 0.75f, h * 0.85f, w, h * 0.76f)
                    lineTo(w, h)
                    lineTo(0f, h)
                    close()
                }
                drawPath(path3, color = Color(0xFFB3DFEE).copy(alpha = 0.90f))

                // Cresta brillante sutil
                val crestPath = Path().apply {
                    moveTo(0f, h * 0.58f)
                    cubicTo(w * 0.30f, h * 0.70f, w * 0.70f, h * 0.46f, w, h * 0.62f)
                }
                drawPath(crestPath, color = Color.White.copy(alpha = 0.45f), style = Stroke(width = 1.5f))
            }

            ContrapesoEnvironment.RECUPERAR -> {
                // 1. Fondo base de amanecer cálido
                drawRect(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color(0xFF8A3B0F), Color(0xFF4A1E06))
                    )
                )

                // Orbe solar luminoso de amanecer en la esquina superior derecha
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color(0xFFFFF7D6),
                            Color(0xFFFFC043).copy(alpha = 0.65f),
                            Color.Transparent
                        ),
                        center = Offset(w * 0.82f, h * 0.28f),
                        radius = 32.dp.toPx()
                    ),
                    center = Offset(w * 0.82f, h * 0.28f),
                    radius = 32.dp.toPx()
                )
                drawCircle(
                    color = Color(0xFFFFFBEA),
                    radius = 11.dp.toPx(),
                    center = Offset(w * 0.82f, h * 0.28f)
                )

                // 2. Estrato 1: Marea ámbar cálida
                val path1 = Path().apply {
                    moveTo(0f, h * 0.44f)
                    cubicTo(w * 0.35f, h * 0.34f, w * 0.65f, h * 0.54f, w, h * 0.40f)
                    lineTo(w, h)
                    lineTo(0f, h)
                    close()
                }
                drawPath(path1, color = Color(0xFFE26D24).copy(alpha = 0.70f))

                // 3. Estrato 2: Marea coral
                val path2 = Path().apply {
                    moveTo(0f, h * 0.60f)
                    cubicTo(w * 0.30f, h * 0.72f, w * 0.70f, h * 0.48f, w, h * 0.64f)
                    lineTo(w, h)
                    lineTo(0f, h)
                    close()
                }
                drawPath(path2, color = Color(0xFFF7B14A).copy(alpha = 0.80f))

                // 4. Estrato 3: Cresta melocotón frontal
                val path3 = Path().apply {
                    moveTo(0f, h * 0.80f)
                    cubicTo(w * 0.40f, h * 0.70f, w * 0.75f, h * 0.86f, w, h * 0.78f)
                    lineTo(w, h)
                    lineTo(0f, h)
                    close()
                }
                drawPath(path3, color = Color(0xFFFFD8A8).copy(alpha = 0.90f))
            }

            ContrapesoEnvironment.EQUILIBRAR -> {
                // 1. Fondo base jade profundo
                drawRect(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color(0xFF095A47), Color(0xFF042F24))
                    )
                )

                // Anillo de equilibrio armónico sutil
                drawCircle(
                    color = Color.White.copy(alpha = 0.22f),
                    radius = 16.dp.toPx(),
                    center = Offset(w * 0.82f, h * 0.28f),
                    style = Stroke(width = 2.dp.toPx())
                )
                drawCircle(
                    color = Color(0xFF6EE7B7).copy(alpha = 0.35f),
                    radius = 8.dp.toPx(),
                    center = Offset(w * 0.82f, h * 0.28f)
                )

                // 2. Estrato 1: Corrientes esmeralda profundas
                val path1 = Path().apply {
                    moveTo(0f, h * 0.42f)
                    cubicTo(w * 0.35f, h * 0.32f, w * 0.65f, h * 0.50f, w, h * 0.38f)
                    lineTo(w, h)
                    lineTo(0f, h)
                    close()
                }
                drawPath(path1, color = Color(0xFF059669).copy(alpha = 0.70f))

                // 3. Estrato 2: Flujo verde mar
                val path2 = Path().apply {
                    moveTo(0f, h * 0.60f)
                    cubicTo(w * 0.30f, h * 0.70f, w * 0.70f, h * 0.48f, w, h * 0.62f)
                    lineTo(w, h)
                    lineTo(0f, h)
                    close()
                }
                drawPath(path2, color = Color(0xFF10B981).copy(alpha = 0.80f))

                // 4. Estrato 3: Cresta esmeralda armónica
                val path3 = Path().apply {
                    moveTo(0f, h * 0.78f)
                    cubicTo(w * 0.40f, h * 0.68f, w * 0.75f, h * 0.84f, w, h * 0.76f)
                    lineTo(w, h)
                    lineTo(0f, h)
                    close()
                }
                drawPath(path3, color = Color(0xFF6EE7B7).copy(alpha = 0.90f))
            }

            ContrapesoEnvironment.CONCENTRAR -> {
                // 1. Fondo base crepuscular índigo
                drawRect(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color(0xFF33206B), Color(0xFF150B33))
                    )
                )

                // Orbe lunar sereno con aura estelar
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color(0xFFEDE9FE),
                            Color(0xFFA5B4FC).copy(alpha = 0.50f),
                            Color.Transparent
                        ),
                        center = Offset(w * 0.82f, h * 0.28f),
                        radius = 28.dp.toPx()
                    ),
                    center = Offset(w * 0.82f, h * 0.28f),
                    radius = 28.dp.toPx()
                )
                drawCircle(
                    color = Color(0xFFF5F3FF),
                    radius = 10.dp.toPx(),
                    center = Offset(w * 0.82f, h * 0.28f)
                )

                // 2. Estrato 1: Marea nocturna violeta
                val path1 = Path().apply {
                    moveTo(0f, h * 0.42f)
                    cubicTo(w * 0.35f, h * 0.30f, w * 0.65f, h * 0.52f, w, h * 0.38f)
                    lineTo(w, h)
                    lineTo(0f, h)
                    close()
                }
                drawPath(path1, color = Color(0xFF6366F1).copy(alpha = 0.70f))

                // 3. Estrato 2: Flujo azul crepuscular
                val path2 = Path().apply {
                    moveTo(0f, h * 0.60f)
                    cubicTo(w * 0.30f, h * 0.70f, w * 0.70f, h * 0.48f, w, h * 0.62f)
                    lineTo(w, h)
                    lineTo(0f, h)
                    close()
                }
                drawPath(path2, color = Color(0xFF8B5CF6).copy(alpha = 0.80f))

                // 4. Estrato 3: Cresta lavanda frontal
                val path3 = Path().apply {
                    moveTo(0f, h * 0.78f)
                    cubicTo(w * 0.40f, h * 0.68f, w * 0.75f, h * 0.84f, w, h * 0.76f)
                    lineTo(w, h)
                    lineTo(0f, h)
                    close()
                }
                drawPath(path3, color = Color(0xFFA5B4FC).copy(alpha = 0.90f))
            }
        }
    }
}

/**
 * Panel interactivo de Vista Previa Atmosférica.
 * Integra la descripción, el propósito y el micro-ritmo somático dentro de un solo contenedor vítreo.
 */
@Composable
private fun AtmosphericPreviewPanel(
    selectedEnv: ContrapesoEnvironment,
    isActive: Boolean
) {
    // Micro-ritmo visual animado (respiración suave de marea)
    val infiniteTransition = rememberInfiniteTransition(label = "pulse_transition")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_scale"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(Color(0xF5FFFFFF))
            .border(1.dp, Color(0x66FFFFFF), RoundedCornerShape(24.dp))
            .testTag("contrapeso_preview_banner")
            .padding(18.dp)
    ) {
        Column {
            // Header del panel de vista previa
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Icono con micro-pulso respiratorio
                    Box(
                        modifier = Modifier
                            .size((38 * pulseScale).dp)
                            .clip(CircleShape)
                            .background(Color(selectedEnv.primaryColorHex)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Water,
                            contentDescription = "Ritmo de marea",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Column {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = "Vista previa",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = OceanCyan
                            )
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(
                                        if (isActive) Color(0x2610B981) else Color(0x260EA9CE)
                                    )
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = if (isActive) "En uso activo" else "Listo para activar",
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = if (isActive) Color(0xFF059669) else Color(0xFF0284C7)
                                )
                            }
                        }
                        Text(
                            text = "Ambiente ${selectedEnv.title}",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = OceanDeep
                        )
                    }
                }

                Icon(
                    imageVector = Icons.Outlined.Visibility,
                    contentDescription = "Modo previsualización",
                    tint = OceanDeep,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Propósito del ambiente (comunicación clara de cómo ayuda sin promesas clínicas)
            Text(
                text = "Propósito del ambiente:",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = OceanDeep
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = selectedEnv.description,
                fontSize = 12.sp,
                color = Color(0xFF1E3A56),
                lineHeight = 16.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Ritmo y sensación somática asociada
            val ritmoDesc = when (selectedEnv) {
                ContrapesoEnvironment.CALMAR -> "Oleaje lento • Inhalar 4s, sostener 7s, soltar 8s para aliviar tensión."
                ContrapesoEnvironment.RECUPERAR -> "Activación solar • Inhalar 4s, exhalar 4s con energía y vitalidad."
                ContrapesoEnvironment.EQUILIBRAR -> "Armonía marina • Respiración 4x4 simétrica para regresar al centro."
                ContrapesoEnvironment.CONCENTRAR -> "Inmersión profunda • Ritmo fluido y continuo para aislar distracciones."
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(selectedEnv.primaryColorHex).copy(alpha = 0.08f))
                    .padding(horizontal = 10.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .clip(CircleShape)
                        .background(Color(selectedEnv.primaryColorHex))
                )
                Text(
                    text = ritmoDesc,
                    fontSize = 11.sp,
                    color = Color(0xFF1E3A56),
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}
