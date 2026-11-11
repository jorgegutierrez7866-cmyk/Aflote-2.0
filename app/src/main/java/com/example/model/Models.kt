package com.example.model

enum class UserRole {
    STUDENT,
    PSYCHOLOGIST
}

enum class ContrapesoEnvironment(
    val title: String,
    val subtitle: String,
    val description: String,
    val primaryColorHex: Long,
    val secondaryColorHex: Long,
    val bgGradientHex: Long
) {
    CALMAR(
        title = "Calmar",
        subtitle = "Reduce la tensión y respira profundo",
        description = "Oleaje suave y frecuencias bajas que reducen los picos de cortisol y desaceleran el ritmo cardíaco.",
        primaryColorHex = 0xFF1D95D3,
        secondaryColorHex = 0xFF0EA9CE,
        bgGradientHex = 0xFF0B3354
    ),
    RECUPERAR(
        title = "Recuperar",
        subtitle = "Descansa y recarga tu energía",
        description = "Tonos cálidos y ritmos orgánicos que renuevan la vitalidad cuando sientes agotamiento.",
        primaryColorHex = 0xFFF59E0B,
        secondaryColorHex = 0xFFE26D24,
        bgGradientHex = 0xFF4A250D
    ),
    EQUILIBRAR(
        title = "Equilibrar",
        subtitle = "Encuentra estabilidad en medio del caos",
        description = "Armonías esmeralda y respiraciones estructuradas para restablecer tu centro.",
        primaryColorHex = 0xFF10B981,
        secondaryColorHex = 0xFF059669,
        bgGradientHex = 0xFF0A362B
    ),
    CONCENTRAR(
        title = "Concentrar",
        subtitle = "Enfócate en lo importante",
        description = "Ondas nocturnas y frecuencias de concentración para aislar distracciones y enfocar el estudio.",
        primaryColorHex = 0xFF8B5CF6,
        secondaryColorHex = 0xFF6366F1,
        bgGradientHex = 0xFF241C48
    )
}

enum class TidalCategory(val displayName: String, val description: String, val colorHex: Long) {
    TRANQUILA("Tranquila", "Aguas en calma, serenidad y fluidez", 0xFF0EA9CE),
    MOVIDA("Movida", "Oleaje activo, inquietud o movimiento", 0xFF1D95D3),
    ALTA("Alta", "Marea creciente, alta intensidad o energía", 0xFFF59E0B),
    TORMENTA("Tormenta", "Aguas turbulentas, sobrecarga o agobio", 0xFFE11D48)
}

data class EmotionRecord(
    val id: String,
    val studentId: String,
    val timestamp: Long,
    val category: TidalCategory,
    val emotions: List<String>,
    val intensity: Int, // 1 to 5
    val contextNotes: String = "",
    val sharedWithPsychologist: Boolean = false
)

/**
 * Minimal clinical data transferred to the psychologist portal.
 * STRICT PRIVACY: Omits context notes, student ID, and any private reflection.
 */
data class AuthorizedEmotionRecord(
    val id: String,
    val timestamp: Long,
    val category: TidalCategory,
    val emotions: List<String>,
    val intensity: Int
)

data class PairingCode(
    val code: String,
    val psychologistId: String,
    val psychologistName: String,
    val psychologistTitle: String,
    val createdAtMillis: Long,
    val expiresAtMillis: Long,
    val isUsed: Boolean = false,
    val isRevoked: Boolean = false
)

/**
 * Real aggregated KPIs for the psychologist portal.
 * Derived 100% from real active pairings, appointments, and authorized activities.
 */
data class PsychologistKpiSummary(
    val activeLinkedStudentsCount: Int,
    val bookedAppointmentsCount: Int,
    val availableSlotsCount: Int,
    val authorizedActivitiesCount: Int
)

/**
 * Pre-consultation information prepared by the student.
 * STRICT PRIVACY: Private by default. Shared only if explicitly authorized.
 * Excludes private reflections and intimate step responses.
 */
data class PreConsultationData(
    val studentId: String = "val_1",
    val reasonForConsultation: String = "",
    val currentSituation: String = "",
    val expectations: String = "",
    val schedulePreferences: String = "",
    val academicContextNotes: String = "",
    val isSharedWithPsychologist: Boolean = false,
    val lastUpdatedMillis: Long = System.currentTimeMillis()
)

/**
 * An authorized student record as visible to the psychologist.
 * STRICT PRIVACY: Private notes, reflections, and full context are completely excluded.
 */
data class LinkedStudent(
    val studentId: String,
    val displayName: String,
    val pairedAtMillis: Long,
    val permissionsShareEmotions: Boolean,
    val permissionsShareWeeklyProgress: Boolean
)

enum class ActivityCategory(val displayName: String) {
    RESPIRACION("Respiración y regulación"),
    PSICOLOGIA("Psicología y autoconocimiento"),
    ESCRITURA("Escritura y expresión"),
    DESCANSO("Descanso y sueño"),
    GRUPAL("Actividades grupales"),
    EXPLORACION("Exploración y creatividad"),
    CONCENTRACION("Concentración y equilibrio")
}

enum class NeedFilter(val displayName: String) {
    ALL("Todas"),
    CALM("Quiero calmarme"),
    ENERGY("Necesito recuperar energía"),
    FOCUS("Quiero concentrarme"),
    BALANCE("Necesito equilibrarme"),
    EXPRESS("Quiero expresarme"),
    REST("Necesito descansar"),
    UNSURE("No sé qué necesito")
}

enum class ActivityStatus {
    AVAILABLE,
    IN_PROGRESS,
    COMPLETED,
    INTERRUPTED
}

data class ActivityItem(
    val id: String,
    val title: String,
    val shortDescription: String,
    val fullDescription: String,
    val category: ActivityCategory,
    val relatedNeeds: List<NeedFilter>,
    val durationMinutes: Int,
    val exerciseType: String,
    val steps: List<String>,
    val reflectivePrompt: String,
    val currentStatus: ActivityStatus = ActivityStatus.AVAILABLE
)

data class ActivityLogEntry(
    val id: String,
    val activityId: String,
    val title: String,
    val categoryName: String,
    val timestamp: Long,
    val durationSeconds: Int,
    val status: ActivityStatus,
    val privateReflection: String = "",
    val sharedWithPsychologist: Boolean = false // Never true automatically
)

data class RouteStation(
    val id: String,
    val title: String,
    val subtitle: String,
    val iconType: String,
    val isCompleted: Boolean = false,
    val isCurrent: Boolean = false,
    val progress: Float = 0f
)

data class PsychologistProfile(
    val id: String,
    val name: String,
    val title: String,
    val licenseNumber: String,
    val pairingCode: String,
    val bio: String
)

data class Appointment(
    val id: String,
    val psychologistId: String,
    val studentId: String?,
    val studentName: String?,
    val dateTimeMillis: Long,
    val timeLabel: String,
    val dateLabel: String,
    val isBooked: Boolean
)

data class StudentPsychologistPairing(
    val isPaired: Boolean,
    val psychologistName: String? = null,
    val psychologistTitle: String? = null,
    val pairingCode: String? = null,
    val permissionsShareEmotions: Boolean = true,
    val permissionsShareWeeklyProgress: Boolean = true,
    val permissionsSharePrivateNotes: Boolean = false // STRICT RULE: always false, user cannot leak private notes
)

data class WeeklyStats(
    val activitiesCompleted: Int,
    val emotionalMomentsLogged: Int,
    val minutesInvested: Int,
    val dailyActivityDays: List<Boolean> // Sun to Sat
)
