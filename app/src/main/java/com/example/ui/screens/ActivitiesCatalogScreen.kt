package com.example.ui.screens

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
import com.example.model.ActivityCategory
import com.example.model.ActivityItem
import com.example.model.NeedFilter
import com.example.ui.components.OceanicContourBackground
import com.example.ui.theme.*

@Composable
fun ActivitiesCatalogScreen(
    activities: List<ActivityItem>,
    onBack: () -> Unit,
    onSelectActivity: (ActivityItem) -> Unit
) {
    var selectedNeed by remember { mutableStateOf(NeedFilter.ALL) }
    var selectedCategory by remember { mutableStateOf<ActivityCategory?>(null) }

    val filteredActivities = activities.filter { item ->
        val matchesNeed = when (selectedNeed) {
            NeedFilter.ALL -> true
            NeedFilter.UNSURE -> true
            else -> item.relatedNeeds.contains(selectedNeed)
        }
        val matchesCategory = selectedCategory == null || item.category == selectedCategory
        matchesNeed && matchesCategory
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
                        .testTag("catalog_back_button"),
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
                        text = "Actividades",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = OceanDeep
                    )
                    Text(
                        text = "Explora y aprende a tu ritmo",
                        fontSize = 12.sp,
                        color = Color(0xFF4A6882)
                    )
                }

                Spacer(modifier = Modifier.size(40.dp))
            }

            // Filtros por Necesidad (Section 9)
            Text(
                text = "¿Qué necesitas ahora?",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = OceanDeep,
                modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
            )

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(bottom = 8.dp)
            ) {
                items(NeedFilter.values()) { need ->
                    val isSelected = need == selectedNeed
                    FilterChip(
                        selected = isSelected,
                        onClick = { selectedNeed = need },
                        label = { Text(need.displayName, fontSize = 12.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = OceanCyan,
                            selectedLabelColor = Color.White,
                            containerColor = Color(0xD9FFFFFF),
                            labelColor = OceanDeep
                        ),
                        border = FilterChipDefaults.filterChipBorder(
                            borderColor = if (isSelected) OceanCyan else Color(0x66FFFFFF),
                            enabled = true,
                            selected = isSelected
                        )
                    )
                }
            }

            // Explicación empática del filtro (Regla 9: "Podría ayudarte ahora")
            if (selectedNeed != NeedFilter.ALL) {
                Text(
                    text = "Podría ayudarte ahora según tu momento:",
                    fontSize = 12.sp,
                    color = OceanCyan,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            // List of Activities
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(top = 4.dp, bottom = 20.dp)
            ) {
                items(filteredActivities) { activity ->
                    ActivityCard(activity = activity, onClick = { onSelectActivity(activity) })
                }

                if (filteredActivities.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 40.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "No se encontraron actividades con este filtro.\nPuedes explorar todas las opciones disponibles.",
                                fontSize = 13.sp,
                                color = Color(0xFF6B8CA8),
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ActivityCard(
    activity: ActivityItem,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(Color(0xD9FFFFFF))
            .border(1.dp, Color(0x66FFFFFF), RoundedCornerShape(24.dp))
            .clickable(onClick = onClick)
            .testTag("activity_card_${activity.id}")
            .padding(16.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Category Tag
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFE2F0F8))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = activity.category.displayName,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = OceanCyan
                    )
                }

                // Duration tag
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Timer,
                        contentDescription = null,
                        tint = Color(0xFF6B8CA8),
                        modifier = Modifier.size(14.dp)
                    )
                    Text(
                        text = "${activity.durationMinutes} min",
                        fontSize = 12.sp,
                        color = Color(0xFF6B8CA8),
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = activity.title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = OceanDeep
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = activity.shortDescription,
                fontSize = 13.sp,
                color = Color(0xFF557793),
                lineHeight = 17.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = activity.exerciseType,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = OceanCyan
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "Comenzar",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = OceanDeep
                    )
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        tint = OceanDeep,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }
        }
    }
}
