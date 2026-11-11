package com.example.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.RouteStation
import com.example.ui.components.OceanicContourBackground
import com.example.ui.theme.*

@Composable
fun RutasScreen(
    stations: List<RouteStation>,
    onBack: () -> Unit,
    onOpenStation: (RouteStation) -> Unit,
    onOpenGuide: () -> Unit
) {
    OceanicContourBackground(isDarkOcean = true) {
        Box(modifier = Modifier.fillMaxSize()) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
                    .padding(horizontal = 20.dp),
                contentPadding = PaddingValues(top = 16.dp, bottom = 120.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Top App Bar: Back button, Title & Subtitle, Book/Guide icon
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Circular translucent Back Button
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(Color(0x33FFFFFF))
                                .clickable(onClick = onBack)
                                .testTag("rutas_back_button"),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Regresar",
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "Rutas",
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = "Explora tu bienestar",
                                fontSize = 13.sp,
                                color = Color(0xCCB0D8EF)
                            )
                        }

                        // Book / Guide Icon button
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(Color(0x33FFFFFF))
                                .clickable(onClick = onOpenGuide)
                                .testTag("rutas_guide_button"),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.MenuBook,
                                contentDescription = "Guía de Rutas",
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }

                item { Spacer(modifier = Modifier.height(10.dp)) }

                // The 4 Stations rendered on the winding path
                item {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(22.dp)
                    ) {
                        stations.forEachIndexed { index, station ->
                            StationCardRow(
                                station = station,
                                index = index,
                                onClick = { onOpenStation(station) }
                            )
                        }
                    }
                }
            }

            // Bottom destination floating banner: "Tu destino actual: Equilibrio"
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 24.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(30.dp))
                        .background(Color(0xE6082236))
                        .border(1.dp, Color(0x3338E1E5), RoundedCornerShape(30.dp))
                        .clickable {
                            val current = stations.find { it.isCurrent } ?: stations.first()
                            onOpenStation(current)
                        }
                        .testTag("tu_destino_actual_capsule")
                        .padding(horizontal = 18.dp, vertical = 12.dp)
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                // Compass icon in glowing gold ring
                                Box(
                                    modifier = Modifier
                                        .size(38.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFF0F3657))
                                        .border(2.dp, Color(0xFFFFC043), CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Explore,
                                        contentDescription = null,
                                        tint = Color(0xFFFFC043),
                                        modifier = Modifier.size(20.dp)
                                    )
                                }

                                Column {
                                    Text(
                                        text = "Tu destino actual",
                                        fontSize = 11.sp,
                                        color = Color(0x99FFFFFF)
                                    )
                                    Text(
                                        text = stations.find { it.isCurrent }?.title ?: "Equilibrio",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                }
                            }

                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Progress line with glowing dots
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(4.dp)
                                .clip(RoundedCornerShape(2.dp))
                                .background(Color(0x3338E1E5))
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(0.65f)
                                    .fillMaxHeight()
                                    .background(Color(0xFFFFC043))
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun StationCardRow(
    station: RouteStation,
    index: Int,
    onClick: () -> Unit
) {
    val ringColor = when (station.id) {
        "st_descanso" -> Color(0xFF38E1E5)
        "st_equilibrio" -> Color(0xFFFFC043)
        "st_exploracion" -> Color(0xFF0EA9CE)
        "st_acompanamiento" -> Color(0xFFB57EDC)
        else -> Color(0xFF38E1E5)
    }

    val icon = when (station.iconType) {
        "lotus" -> Icons.Outlined.Spa
        "sun" -> Icons.Default.WbSunny
        "sparkle" -> Icons.Default.AutoAwesome
        "heart" -> Icons.Default.FavoriteBorder
        else -> Icons.Default.Circle
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("station_${station.id}"),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Glowing circular node matching the path in reference
        Box(
            modifier = Modifier
                .size(54.dp)
                .clip(CircleShape)
                .background(Color(0x400C2B47))
                .border(2.dp, ringColor, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = station.title,
                tint = ringColor,
                modifier = Modifier.size(26.dp)
            )
        }

        Spacer(modifier = Modifier.width(14.dp))

        // Station glass pill card
        Box(
            modifier = Modifier
                .weight(1f)
                .height(68.dp)
                .clip(RoundedCornerShape(34.dp))
                .background(
                    if (station.isCurrent) Color(0x331D95D3) else Color(0x240C2B47)
                )
                .border(
                    1.dp,
                    if (station.isCurrent) ringColor.copy(alpha = 0.6f) else Color(0x2638E1E5),
                    RoundedCornerShape(34.dp)
                )
                .clickable(onClick = onClick)
                .padding(horizontal = 20.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = station.title,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = station.subtitle,
                        fontSize = 12.sp,
                        color = Color(0xCCB3DCF2)
                    )
                }

                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = "Abrir estación",
                    tint = Color.White,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}
