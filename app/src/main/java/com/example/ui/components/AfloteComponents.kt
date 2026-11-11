package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

/**
 * Renders the organic topographic ocean contour lines seen in the reference backgrounds.
 */
@Composable
fun OceanicContourBackground(
    modifier: Modifier = Modifier,
    isDarkOcean: Boolean = false,
    ambientTint: Color? = null,
    ambientIntensity: Float = 0.08f,
    content: @Composable BoxScope.() -> Unit
) {
    val bgBrush = if (isDarkOcean) {
        Brush.verticalGradient(
            colors = listOf(
                Color(0xFF07243F),
                Color(0xFF04192E),
                Color(0xFF020E1C)
            )
        )
    } else {
        Brush.verticalGradient(
            colors = listOf(
                Color(0xFFE9F5FB),
                Color(0xFFD4ECF7),
                Color(0xFFB3DFEE),
                Color(0xFF0E6B9E),
                Color(0xFF072D4D)
            ),
            startY = 0f,
            endY = 1600f
        )
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(bgBrush)
    ) {
        // Subtle ambient atmospheric tint overlay (Fase 4B.1)
        if (ambientTint != null && ambientTint != Color.Unspecified) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                ambientTint.copy(alpha = ambientIntensity * 1.1f),
                                ambientTint.copy(alpha = ambientIntensity * 0.4f),
                                Color.Transparent,
                                ambientTint.copy(alpha = ambientIntensity * 0.7f)
                            )
                        )
                    )
            )
        }

        // Draw decorative topographic curved lines like the reference
        Canvas(modifier = Modifier.fillMaxSize()) {
            val baseStroke = if (isDarkOcean) Color(0x1A38E1E5) else Color(0x2E1D95D3)
            val strokeColor = if (ambientTint != null && ambientTint != Color.Unspecified) {
                ambientTint.copy(alpha = if (isDarkOcean) 0.22f else 0.32f)
            } else {
                baseStroke
            }
            val w = size.width
            val h = size.height

            // Wave curve 1
            val path1 = Path().apply {
                moveTo(0f, h * 0.22f)
                cubicTo(w * 0.3f, h * 0.18f, w * 0.7f, h * 0.28f, w, h * 0.20f)
            }
            drawPath(path1, color = strokeColor, style = Stroke(width = 1.5f))

            // Wave curve 2
            val path2 = Path().apply {
                moveTo(0f, h * 0.28f)
                cubicTo(w * 0.4f, h * 0.34f, w * 0.75f, h * 0.22f, w, h * 0.32f)
            }
            drawPath(path2, color = strokeColor, style = Stroke(width = 1.5f))

            // Wave curve 3
            val path3 = Path().apply {
                moveTo(0f, h * 0.38f)
                cubicTo(w * 0.35f, h * 0.42f, w * 0.65f, h * 0.32f, w, h * 0.45f)
            }
            drawPath(path3, color = strokeColor, style = Stroke(width = 1.5f))

            // Bottom wave curve
            val path4 = Path().apply {
                moveTo(0f, h * 0.72f)
                cubicTo(w * 0.4f, h * 0.68f, w * 0.7f, h * 0.78f, w, h * 0.70f)
            }
            drawPath(path4, color = strokeColor, style = Stroke(width = 1.5f))
        }

        content()
    }
}

/**
 * Standard Glassmorphic Card matching the reference
 */
@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color(0xCCFFFFFF),
    borderColor: Color = Color(0x66FFFFFF),
    cornerRadius: Int = 24,
    onClick: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    val cardModifier = if (onClick != null) {
        modifier
            .clip(RoundedCornerShape(cornerRadius.dp))
            .background(backgroundColor)
            .border(1.dp, borderColor, RoundedCornerShape(cornerRadius.dp))
            .clickable(onClick = onClick)
    } else {
        modifier
            .clip(RoundedCornerShape(cornerRadius.dp))
            .background(backgroundColor)
            .border(1.dp, borderColor, RoundedCornerShape(cornerRadius.dp))
    }

    Column(
        modifier = cardModifier.padding(16.dp),
        content = content
    )
}

/**
 * Glow indicator pill or dot
 */
@Composable
fun GlowDot(
    modifier: Modifier = Modifier,
    color: Color = Color(0xFF38E1E5),
    sizeDp: Int = 8
) {
    Box(
        modifier = modifier
            .size(sizeDp.dp)
            .clip(CircleShape)
            .background(color)
    )
}

/**
 * Rounded Action Button with oceanic primary styling
 */
@Composable
fun AfloteButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    backgroundColor: Color = Color(0xFF0A2E4E),
    contentColor: Color = Color.White
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        shape = RoundedCornerShape(28.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor,
            contentColor = contentColor,
            disabledContainerColor = Color(0xFFB0C4DE),
            disabledContentColor = Color.White
        ),
        modifier = modifier
            .fillMaxWidth()
            .height(54.dp)
    ) {
        Text(
            text = text,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}
