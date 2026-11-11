package com.example.ui.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
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
import com.example.data.AfloteRepository
import com.example.model.ActivityItem
import com.example.model.UserRole
import com.example.ui.screens.*
import com.example.ui.theme.OceanCyan
import com.example.ui.theme.OceanDeep
import com.example.ui.viewmodel.AfloteViewModel

enum class AfloteDestination {
    HOME,
    RUTAS,
    CONTRAPESO,
    CHECKIN,
    ACTIVITIES_CATALOG,
    ACTIVITY_DETAIL,
    HISTORY,
    ACCOMPANIMENT,
    AI_ASSISTANT,
    PROFILE,
    PRE_CONSULTATION,
    PSYCHOLOGIST_PORTAL
}

enum class BottomBarTab(val title: String, val icon: androidx.compose.ui.graphics.vector.ImageVector, val destination: AfloteDestination) {
    INICIO("Inicio", Icons.Default.Home, AfloteDestination.HOME),
    RUTAS("Rutas", Icons.Default.Explore, AfloteDestination.RUTAS),
    HISTORIAL("Historial", Icons.Default.AccessTime, AfloteDestination.HISTORY),
    PERFIL("Perfil", Icons.Default.Person, AfloteDestination.PROFILE)
}

@Composable
fun AfloteApp(
    viewModel: AfloteViewModel = remember { AfloteViewModel() }
) {
    val currentRole by viewModel.currentRole.collectAsState()
    val studentName by viewModel.studentName.collectAsState()
    val activeEnvironment by viewModel.activeEnvironment.collectAsState()
    val emotionRecords by viewModel.emotionRecords.collectAsState()
    val activities by viewModel.activities.collectAsState()
    val activityLogs by viewModel.activityLogs.collectAsState()
    val pairing by viewModel.pairing.collectAsState()
    val appointments by viewModel.appointments.collectAsState()
    val routeStations by viewModel.routeStations.collectAsState()
    val psychologistActiveCode by viewModel.psychologistActiveCode.collectAsState()
    val preConsultation by viewModel.preConsultation.collectAsState()
    val psychologistProfile by viewModel.psychologistProfile.collectAsState()

    var currentScreen by remember { mutableStateOf(AfloteDestination.HOME) }
    var selectedActivity by remember { mutableStateOf<ActivityItem?>(null) }

    // Intercept back button when not on HOME
    BackHandler(enabled = currentRole != null && currentScreen != AfloteDestination.HOME) {
        when (currentScreen) {
            AfloteDestination.ACTIVITY_DETAIL -> currentScreen = AfloteDestination.ACTIVITIES_CATALOG
            AfloteDestination.PRE_CONSULTATION -> currentScreen = AfloteDestination.ACCOMPANIMENT
            else -> currentScreen = AfloteDestination.HOME
        }
    }

    // FASE 4A: Initial entry by profiles (null role = ProfileSelectionScreen)
    if (currentRole == null) {
        ProfileSelectionScreen(
            onSelectStudent = { name, lastName, email, career ->
                viewModel.registerStudentProfile(name, lastName, email, career)
                currentScreen = AfloteDestination.HOME
            },
            onSelectPsychologist = { name, lastName, email, specialty ->
                viewModel.registerPsychologistProfile(name, lastName, email, specialty)
            }
        )
        return
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color.Transparent,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        bottomBar = {
            // Reference Bottom Navigation Bar: Only show for Student on primary tabs
            if (currentRole == UserRole.STUDENT &&
                (currentScreen == AfloteDestination.HOME ||
                        currentScreen == AfloteDestination.RUTAS ||
                        currentScreen == AfloteDestination.HISTORY ||
                        currentScreen == AfloteDestination.PROFILE)
            ) {
                AfloteBottomNavigationBar(
                    currentDestination = currentScreen,
                    onSelectTab = { destination ->
                        currentScreen = destination
                    }
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentRole) {
                UserRole.PSYCHOLOGIST -> {
                    val activeCodes by viewModel.pairingCodes.collectAsState()
                    val authorizedPreConsultation = viewModel.getAuthorizedPreConsultation()
                    val psychTitle = psychologistProfile["name"]?.let { "Dra. $it ${psychologistProfile["lastName"] ?: ""}".trim() } ?: "Dra. Sofía Mendoza"

                    // BLOCK 1 & FASE 4 / 4A: Supply ONLY authorized clinical data, authorized preconsultation, and real KPIs
                    PsychologistDashboardScreen(
                        psychologistName = psychTitle,
                        activePairingCodes = activeCodes,
                        linkedStudents = viewModel.getLinkedStudents(),
                        kpiSummary = viewModel.getPsychologistKpis(),
                        authorizedEmotions = viewModel.getAuthorizedClinicalEmotions(),
                        authorizedPreConsultation = authorizedPreConsultation,
                        appointments = appointments,
                        onGenerateNewCode = { viewModel.generateNewPairingCode() },
                        onRevokeCode = { code -> viewModel.revokePairingCode(code) },
                        onAddSlot = { date, time -> viewModel.addPsychologistSlot(date, time) },
                        onCancelAppointment = { id -> viewModel.cancelAppointment(id) },
                        onLogout = { viewModel.logout() }
                    )
                }
                UserRole.STUDENT -> {
                    when (currentScreen) {
                        AfloteDestination.HOME -> {
                            StudentHomeScreen(
                                studentName = studentName,
                                currentRouteStation = routeStations.find { it.isCurrent }?.title ?: "Equilibrio",
                                activeEnvironment = activeEnvironment,
                                emotionRecords = emotionRecords,
                                onNavigateToCheckin = { currentScreen = AfloteDestination.CHECKIN },
                                onNavigateToRutas = { currentScreen = AfloteDestination.RUTAS },
                                onNavigateToContrapeso = { currentScreen = AfloteDestination.CONTRAPESO },
                                onNavigateToActivities = { currentScreen = AfloteDestination.ACTIVITIES_CATALOG },
                                onNavigateToAI = { currentScreen = AfloteDestination.AI_ASSISTANT },
                                onNavigateToAccompaniment = { currentScreen = AfloteDestination.ACCOMPANIMENT },
                                onNavigateToHistory = { currentScreen = AfloteDestination.HISTORY }
                            )
                        }
                        AfloteDestination.RUTAS -> {
                            RutasScreen(
                                stations = routeStations,
                                onBack = { currentScreen = AfloteDestination.HOME },
                                onOpenStation = { station ->
                                    when (station.id) {
                                        "st_descanso" -> currentScreen = AfloteDestination.ACTIVITIES_CATALOG
                                        "st_equilibrio" -> currentScreen = AfloteDestination.CONTRAPESO
                                        "st_exploracion" -> currentScreen = AfloteDestination.ACTIVITIES_CATALOG
                                        "st_acompanamiento" -> currentScreen = AfloteDestination.ACCOMPANIMENT
                                    }
                                },
                                onOpenGuide = { currentScreen = AfloteDestination.ACTIVITIES_CATALOG }
                            )
                        }
                        AfloteDestination.CONTRAPESO -> {
                            ContrapesoScreen(
                                currentActiveEnv = activeEnvironment,
                                onBack = { currentScreen = AfloteDestination.HOME },
                                onActivateEnvironment = { env -> viewModel.activateEnvironment(env) }
                            )
                        }
                        AfloteDestination.CHECKIN -> {
                            EmotionalCheckinScreen(
                                onBack = { currentScreen = AfloteDestination.HOME },
                                onSaveRecord = { category, emotions, intensity, context ->
                                    viewModel.addEmotionRecord(category, emotions, intensity, context)
                                    currentScreen = AfloteDestination.HOME
                                }
                            )
                        }
                        AfloteDestination.ACTIVITIES_CATALOG -> {
                            ActivitiesCatalogScreen(
                                activities = activities,
                                onBack = { currentScreen = AfloteDestination.HOME },
                                onSelectActivity = { act ->
                                    selectedActivity = act
                                    currentScreen = AfloteDestination.ACTIVITY_DETAIL
                                }
                            )
                        }
                        AfloteDestination.ACTIVITY_DETAIL -> {
                            selectedActivity?.let { act ->
                                ActivityDetailScreen(
                                    activity = act,
                                    onBack = { currentScreen = AfloteDestination.ACTIVITIES_CATALOG },
                                    onCompleteActivity = { actId, duration, status, reflection, attemptId ->
                                        viewModel.logActivityCompletion(actId, duration, status, reflection, attemptId)
                                        currentScreen = AfloteDestination.ACTIVITIES_CATALOG
                                    }
                                )
                            }
                        }
                        AfloteDestination.HISTORY -> {
                            EmotionalHistoryScreen(
                                emotionRecords = emotionRecords,
                                activityLogs = activityLogs,
                                onBack = { currentScreen = AfloteDestination.HOME }
                            )
                        }
                        AfloteDestination.ACCOMPANIMENT -> {
                            StudentAccompanimentScreen(
                                pairing = pairing,
                                availableAppointments = appointments,
                                onBack = { currentScreen = AfloteDestination.HOME },
                                onPairWithCode = { code -> viewModel.pairWithPsychologist(code) },
                                onRevokePairing = { viewModel.revokePairing() },
                                onUpdatePermissions = { shareEmo, shareProg ->
                                    viewModel.updatePermissions(shareEmo, shareProg)
                                },
                                onBookAppointment = { id -> viewModel.bookAppointment(id) },
                                onCancelAppointment = { id -> viewModel.cancelAppointment(id) },
                                onNavigateToPreConsultation = { currentScreen = AfloteDestination.PRE_CONSULTATION }
                            )
                        }
                        AfloteDestination.PRE_CONSULTATION -> {
                            PreConsultationScreen(
                                currentData = preConsultation,
                                psychologistName = pairing.psychologistName,
                                isPaired = pairing.isPaired,
                                onBack = { currentScreen = AfloteDestination.ACCOMPANIMENT },
                                onSavePreConsultation = { reason, situation, expectations, schedulePrefs, academicContext, isShared ->
                                    viewModel.updatePreConsultation(
                                        reason,
                                        situation,
                                        expectations,
                                        schedulePrefs,
                                        academicContext,
                                        isShared
                                    )
                                    currentScreen = AfloteDestination.ACCOMPANIMENT
                                }
                            )
                        }
                        AfloteDestination.AI_ASSISTANT -> {
                            ConversationalAssistantScreen(
                                onBack = { currentScreen = AfloteDestination.HOME }
                            )
                        }
                        AfloteDestination.PROFILE -> {
                            StudentProfileScreen(
                                studentName = studentName,
                                pairing = pairing,
                                onBack = { currentScreen = AfloteDestination.HOME },
                                onLogout = { viewModel.logout() },
                                onNavigateToAccompaniment = { currentScreen = AfloteDestination.ACCOMPANIMENT },
                                onNavigateToPreConsultation = { currentScreen = AfloteDestination.PRE_CONSULTATION }
                            )
                        }
                        AfloteDestination.PSYCHOLOGIST_PORTAL -> {
                            // Routed at role level
                        }
                    }
                }
                null -> {
                    // Handled above
                }
            }
        }
    }
}

/**
 * Bottom Navigation Bar reproducing the 4 tabs from the reference: Inicio, Rutas, Historial, Perfil
 */
@Composable
private fun AfloteBottomNavigationBar(
    currentDestination: AfloteDestination,
    onSelectTab: (AfloteDestination) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .windowInsetsPadding(WindowInsets.navigationBars)
            .padding(horizontal = 24.dp, vertical = 10.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .clip(RoundedCornerShape(32.dp))
                .background(Color(0xF0FFFFFF))
                .border(1.dp, Color(0x66FFFFFF), RoundedCornerShape(32.dp))
                .padding(horizontal = 12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                BottomBarTab.values().forEach { tab ->
                    val isSelected = currentDestination == tab.destination
                    Column(
                        modifier = Modifier
                            .clip(CircleShape)
                            .clickable { onSelectTab(tab.destination) }
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                            .testTag("nav_tab_${tab.title.lowercase()}"),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = tab.icon,
                            contentDescription = tab.title,
                            tint = if (isSelected) OceanCyan else Color(0xFF6B8CA8),
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = tab.title,
                            fontSize = 11.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSelected) OceanCyan else Color(0xFF6B8CA8)
                        )
                    }
                }
            }
        }
    }
}
