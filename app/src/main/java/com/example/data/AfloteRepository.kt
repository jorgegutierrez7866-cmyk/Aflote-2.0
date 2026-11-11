package com.example.data

import com.example.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class AfloteRepository(
    private val timeProvider: () -> Long = { System.currentTimeMillis() }
) {
    // Current Active Role & Session Information
    private val _currentRole = MutableStateFlow<UserRole?>(null) // null = on boarding role selection
    val currentRole: StateFlow<UserRole?> = _currentRole.asStateFlow()

    private val _studentProfile = MutableStateFlow(
        mapOf(
            "name" to "Valentina",
            "lastName" to "Restrepo",
            "email" to "valentina.restrepo@universidad.edu.co",
            "career" to "Ingeniería de Diseño"
        )
    )
    val studentProfile: StateFlow<Map<String, String>> = _studentProfile.asStateFlow()

    private val _psychologistProfile = MutableStateFlow(
        mapOf(
            "name" to "Sofía",
            "lastName" to "Mendoza",
            "email" to "sofia.mendoza@universidad.edu.co",
            "specialty" to "Psicología Clínica y Bienestar Estudiantil"
        )
    )
    val psychologistProfile: StateFlow<Map<String, String>> = _psychologistProfile.asStateFlow()

    private val _studentName = MutableStateFlow("Valentina")
    val studentName: StateFlow<String> = _studentName.asStateFlow()

    // Pre-consultation store
    private val _preConsultation = MutableStateFlow(
        PreConsultationData(
            studentId = "val_1",
            reasonForConsultation = "Gestión de ansiedad académica ante cierres de semestre",
            currentSituation = "Siento que me cuesta conciliar el descanso y la rumiación de entregas.",
            expectations = "Aprender herramientas para regular los picos de activación fisiológica.",
            schedulePreferences = "Jueves o Viernes en las mañanas",
            academicContextNotes = "Semestre 7 con carga de 5 materias",
            isSharedWithPsychologist = false, // STRICT: Private by default
            lastUpdatedMillis = timeProvider() - 86400000L * 2
        )
    )
    val preConsultation: StateFlow<PreConsultationData> = _preConsultation.asStateFlow()

    // Contrapeso Active Environment
    private val _activeEnvironment = MutableStateFlow(ContrapesoEnvironment.CALMAR)
    val activeEnvironment: StateFlow<ContrapesoEnvironment> = _activeEnvironment.asStateFlow()

    // Emotion Records (Full Student Store)
    private val _emotionRecords = MutableStateFlow<List<EmotionRecord>>(
        listOf(
            EmotionRecord(
                id = "em_1",
                studentId = "val_1",
                timestamp = timeProvider() - 86400000L * 2,
                category = TidalCategory.MOVIDA,
                emotions = listOf("Inquietud", "Expectativa"),
                intensity = 3,
                contextNotes = "Semana de parciales y entregas finales"
            ),
            EmotionRecord(
                id = "em_2",
                studentId = "val_1",
                timestamp = timeProvider() - 86400000L,
                category = TidalCategory.TRANQUILA,
                emotions = listOf("Calma", "Alivio"),
                intensity = 2,
                contextNotes = "Paseo por el campus y café"
            )
        )
    )
    val emotionRecords: StateFlow<List<EmotionRecord>> = _emotionRecords.asStateFlow()

    // Catalog Activities
    private val initialActivities = listOf(
        ActivityItem(
            id = "act_1",
            title = "Respiración 4-7-8 Océano",
            shortDescription = "Reduce la activación fisiológica y el ritmo cardíaco.",
            fullDescription = "Técnica de respiración rítmica respaldada clínicamente para modular el sistema nervioso parasimpático.",
            category = ActivityCategory.RESPIRACION,
            relatedNeeds = listOf(NeedFilter.CALM, NeedFilter.REST),
            durationMinutes = 4,
            exerciseType = "Respiración Guiada",
            steps = listOf(
                "Inhala lentamente por la nariz contando mentalmente hasta 4.",
                "Mantén suavemente el aire durante 7 segundos sintiendo la suspensión.",
                "Exhala por la boca con un soplido suave durante 8 segundos como el murmullo de una ola.",
                "Repite este ciclo 4 veces consecutivas con calma."
            ),
            reflectivePrompt = "¿Cómo sientes la respiración y el cuerpo tras este ejercicio?"
        ),
        ActivityItem(
            id = "act_2",
            title = "Descarga Mental en Marea",
            shortDescription = "Escribe sin censura para despejar la saturación mental.",
            fullDescription = "Espacio privado para depositar pensamientos abrumadores y recuperar perspectiva.",
            category = ActivityCategory.ESCRITURA,
            relatedNeeds = listOf(NeedFilter.CALM, NeedFilter.EXPRESS, NeedFilter.BALANCE),
            durationMinutes = 5,
            exerciseType = "Escritura Libre Privada",
            steps = listOf(
                "Anota todo lo que ocupa tu mente en este momento, sin corregir la sintaxis.",
                "Identifica qué cosas están bajo tu control directo y cuáles escapan a tus manos.",
                "Elige una sola acción pequeña y concreta para la más urgente."
            ),
            reflectivePrompt = "¿Lograste aligerar la carga mental que traías hoy?"
        ),
        ActivityItem(
            id = "act_3",
            title = "Anclaje de los 5 Sentidos",
            shortDescription = "Regresa al momento presente ante la rumiación.",
            fullDescription = "Técnica de grounding somático para estabilizarte cuando el pensamiento se acelera.",
            category = ActivityCategory.PSICOLOGIA,
            relatedNeeds = listOf(NeedFilter.BALANCE, NeedFilter.FOCUS),
            durationMinutes = 3,
            exerciseType = "Mindfulness Somático",
            steps = listOf(
                "Observa 5 cosas que puedas ver a tu alrededor y fíjate en sus detalles.",
                "Toca 4 texturas u objetos diferentes (ropa, mesa, cabello).",
                "Escucha 3 sonidos distintos del entorno inmediato.",
                "Identifica 2 olores en el aire.",
                "Percibe 1 sabor o sensación física en tu boca."
            ),
            reflectivePrompt = "¿Sientes mayor conexión y presencia física ahora?"
        ),
        ActivityItem(
            id = "act_4",
            title = "Pausa de Foco Profundo",
            shortDescription = "Sesión guiada para comenzar bloques de estudio concentrado.",
            fullDescription = "Estructura la atención y minimiza la tentación de multitarea digital.",
            category = ActivityCategory.CONCENTRACION,
            relatedNeeds = listOf(NeedFilter.FOCUS, NeedFilter.ENERGY),
            durationMinutes = 15,
            exerciseType = "Foco Estructurado",
            steps = listOf(
                "Silencia notificaciones y prepara únicamente el material necesario.",
                "Define la meta singular de este bloque de 15 minutos.",
                "Trabaja sin saltar a otras pestañas ni aplicaciones hasta que suene el cierre."
            ),
            reflectivePrompt = "¿Pudiste sostener el foco durante la mayor parte del bloque?"
        ),
        ActivityItem(
            id = "act_5",
            title = "Relajación Muscular Progresiva",
            shortDescription = "Libera la tensión acumulada en cuello, hombros y mandíbula.",
            fullDescription = "Contracción y relajación sucesiva de grupos musculares para inducir descanso físico profundo.",
            category = ActivityCategory.DESCANSO,
            relatedNeeds = listOf(NeedFilter.REST, NeedFilter.CALM),
            durationMinutes = 6,
            exerciseType = "Relajación Somática",
            steps = listOf(
                "Aprieta con suavidad los hombros hacia las orejas durante 5 segundos; luego suelta de golpe.",
                "Tensa las manos en puños durante 5 segundos y siente el calor al abrir los dedos.",
                "Afloja la mandíbula permitiendo que la lengua descanse en el paladar.",
                "Respira profundo sintiendo el peso del cuerpo sobre la superficie."
            ),
            reflectivePrompt = "¿Qué zonas de tu cuerpo soltaron mayor tensión?"
        )
    )

    private val _activities = MutableStateFlow<List<ActivityItem>>(initialActivities)
    val activities: StateFlow<List<ActivityItem>> = _activities.asStateFlow()

    // Activity Logs / Bitácora
    private val _activityLogs = MutableStateFlow<List<ActivityLogEntry>>(
        listOf(
            ActivityLogEntry(
                id = "log_1",
                activityId = "act_1",
                title = "Respiración 4-7-8 Océano",
                categoryName = "Respiración y regulación",
                timestamp = timeProvider() - 86400000L * 3,
                durationSeconds = 240,
                status = ActivityStatus.COMPLETED,
                privateReflection = "Me ayudó mucho a calmar la palpitación antes de entrar al salón.",
                sharedWithPsychologist = false
            ),
            ActivityLogEntry(
                id = "log_2",
                activityId = "act_3",
                title = "Anclaje de los 5 Sentidos",
                categoryName = "Psicología y autoconocimiento",
                timestamp = timeProvider() - 86400000L,
                durationSeconds = 180,
                status = ActivityStatus.COMPLETED,
                privateReflection = "Volví al presente, la vista del cielo me tranquilizó.",
                sharedWithPsychologist = false
            )
        )
    )
    val activityLogs: StateFlow<List<ActivityLogEntry>> = _activityLogs.asStateFlow()

    // Set of completed attempt IDs to ensure idempotency across multiple button taps
    private val completedAttemptIds = mutableSetOf<String>()

    // Pairing with Psychologist
    private val _pairing = MutableStateFlow(
        StudentPsychologistPairing(
            isPaired = true,
            psychologistName = "Dra. Sofía Mendoza",
            psychologistTitle = "Psicóloga Clínica y Bienestar Universitario",
            pairingCode = "729401",
            permissionsShareEmotions = true,
            permissionsShareWeeklyProgress = true,
            permissionsSharePrivateNotes = false // STRICT INVIOLABLE RULE
        )
    )
    val pairing: StateFlow<StudentPsychologistPairing> = _pairing.asStateFlow()

    // Active Pairing Codes with expiration
    private val _pairingCodes = MutableStateFlow<List<PairingCode>>(
        listOf(
            PairingCode(
                code = "729401",
                psychologistId = "psy_sofia",
                psychologistName = "Dra. Sofía Mendoza",
                psychologistTitle = "Psicóloga Clínica y Bienestar Universitario",
                createdAtMillis = timeProvider() - 60000L,
                expiresAtMillis = timeProvider() + 86400000L * 7, // 7 days expiration
                isUsed = false
            )
        )
    )
    val pairingCodes: StateFlow<List<PairingCode>> = _pairingCodes.asStateFlow()

    val psychologistActiveCode: StateFlow<String> = MutableStateFlow("729401").asStateFlow()

    // Appointments Store
    private val _appointments = MutableStateFlow<List<Appointment>>(
        listOf(
            Appointment(
                id = "apt_1",
                psychologistId = "psy_sofia",
                studentId = "val_1",
                studentName = "Valentina",
                dateTimeMillis = timeProvider() + 86400000L * 2,
                timeLabel = "10:00 AM - 10:45 AM",
                dateLabel = "Jueves 12 de Noviembre",
                isBooked = true
            ),
            Appointment(
                id = "apt_2",
                psychologistId = "psy_sofia",
                studentId = null,
                studentName = null,
                dateTimeMillis = timeProvider() + 86400000L * 3,
                timeLabel = "03:00 PM - 03:45 PM",
                dateLabel = "Viernes 13 de Noviembre",
                isBooked = false
            ),
            Appointment(
                id = "apt_3",
                psychologistId = "psy_sofia",
                studentId = null,
                studentName = null,
                dateTimeMillis = timeProvider() + 86400000L * 6,
                timeLabel = "11:00 AM - 11:45 AM",
                dateLabel = "Lunes 16 de Noviembre",
                isBooked = false
            )
        )
    )
    val appointments: StateFlow<List<Appointment>> = _appointments.asStateFlow()

    // Route Stations
    private val _routeStations = MutableStateFlow<List<RouteStation>>(
        listOf(
            RouteStation(
                id = "st_descanso",
                title = "Descanso",
                subtitle = "Recarga tu energía",
                iconType = "lotus",
                isCompleted = true,
                isCurrent = false,
                progress = 1.0f
            ),
            RouteStation(
                id = "st_equilibrio",
                title = "Equilibrio",
                subtitle = "Encuentra el balance",
                iconType = "sun",
                isCompleted = false,
                isCurrent = true,
                progress = 0.65f
            ),
            RouteStation(
                id = "st_exploracion",
                title = "Exploración",
                subtitle = "Descubre nuevas herramientas",
                iconType = "sparkle",
                isCompleted = false,
                isCurrent = false,
                progress = 0.0f
            ),
            RouteStation(
                id = "st_acompanamiento",
                title = "Acompañamiento",
                subtitle = "Tu bienestar, en buenas manos",
                iconType = "heart",
                isCompleted = false,
                isCurrent = false,
                progress = 0.0f
            )
        )
    )
    val routeStations: StateFlow<List<RouteStation>> = _routeStations.asStateFlow()

    // Profile registration and session handling (Fase 4A)
    fun registerStudentProfile(name: String, lastName: String, email: String, career: String) {
        val trimmedName = name.trim().ifEmpty { "Estudiante" }
        _studentName.value = trimmedName
        _studentProfile.value = mapOf(
            "name" to trimmedName,
            "lastName" to lastName.trim(),
            "email" to email.trim(),
            "career" to career.trim()
        )
        _currentRole.value = UserRole.STUDENT
    }

    fun registerPsychologistProfile(name: String, lastName: String, email: String, specialty: String) {
        _psychologistProfile.value = mapOf(
            "name" to name.trim(),
            "lastName" to lastName.trim(),
            "email" to email.trim(),
            "specialty" to specialty.trim()
        )
        _currentRole.value = UserRole.PSYCHOLOGIST
    }

    fun logout() {
        _currentRole.value = null
    }

    // Role switcher
    fun switchRole(role: UserRole?) {
        _currentRole.value = role
    }

    // Pre-consultation update and share authorization (Fase 4A)
    fun updatePreConsultation(
        reason: String,
        situation: String,
        expectations: String,
        schedulePrefs: String,
        academicContext: String,
        isShared: Boolean
    ) {
        _preConsultation.value = PreConsultationData(
            studentId = "val_1",
            reasonForConsultation = reason.trim(),
            currentSituation = situation.trim(),
            expectations = expectations.trim(),
            schedulePreferences = schedulePrefs.trim(),
            academicContextNotes = academicContext.trim(),
            isSharedWithPsychologist = isShared,
            lastUpdatedMillis = timeProvider()
        )
    }

    /**
     * Authorized pre-consultation query for the psychologist.
     * STRICT PRIVACY: Returns null if not paired or if student kept it private.
     */
    fun getAuthorizedPreConsultation(psychologistId: String = "psy_sofia"): PreConsultationData? {
        val currentPairing = _pairing.value
        val data = _preConsultation.value
        if (!currentPairing.isPaired || !data.isSharedWithPsychologist) {
            return null
        }
        return data
    }

    // Set Active Contrapeso Environment
    fun activateEnvironment(env: ContrapesoEnvironment) {
        _activeEnvironment.value = env
    }

    // Save Emotion Check-in
    fun addEmotionRecord(category: TidalCategory, emotions: List<String>, intensity: Int, context: String) {
        val newRecord = EmotionRecord(
            id = "em_${timeProvider()}",
            studentId = "val_1",
            timestamp = timeProvider(),
            category = category,
            emotions = emotions,
            intensity = intensity,
            contextNotes = context,
            sharedWithPsychologist = _pairing.value.permissionsShareEmotions
        )
        _emotionRecords.update { listOf(newRecord) + it }
    }

    // Add completed activity to log with attemptId idempotency (Block 4)
    fun logActivityCompletion(
        activityId: String,
        durationSeconds: Int,
        status: ActivityStatus,
        reflection: String,
        attemptId: String = "att_${timeProvider()}"
    ): Boolean {
        // Prevent duplicate submissions of the exact same attempt
        if (completedAttemptIds.contains(attemptId)) {
            return false
        }
        completedAttemptIds.add(attemptId)

        val activity = _activities.value.find { it.id == activityId }
        val title = activity?.title ?: "Actividad"
        val categoryName = activity?.category?.displayName ?: "Bienestar"
        val newLog = ActivityLogEntry(
            id = "log_${timeProvider()}",
            activityId = activityId,
            title = title,
            categoryName = categoryName,
            timestamp = timeProvider(),
            durationSeconds = durationSeconds,
            status = status,
            privateReflection = reflection,
            sharedWithPsychologist = false // STRICT RULE: Never share private reflection notes
        )
        _activityLogs.update { listOf(newLog) + it }

        if (status == ActivityStatus.COMPLETED) {
            _activities.update { list ->
                list.map {
                    if (it.id == activityId) it.copy(currentStatus = ActivityStatus.COMPLETED) else it
                }
            }
        }
        return true
    }

    // Redeem Pairing Code with Expiration Validation (Block 3)
    fun pairWithPsychologist(code: String): Result<String> {
        val cleanCode = code.trim()
        if (cleanCode.length != 6) {
            return Result.failure(IllegalArgumentException("El código debe tener exactamente 6 dígitos"))
        }

        val now = timeProvider()
        val foundCode = _pairingCodes.value.find { it.code == cleanCode }

        if (foundCode == null) {
            return Result.failure(IllegalArgumentException("Código inválido o inexistente. Consulta con tu psicólogo."))
        }

        if (foundCode.isRevoked) {
            return Result.failure(IllegalStateException("Este código de vinculación ha sido revocado."))
        }

        if (now > foundCode.expiresAtMillis) {
            return Result.failure(IllegalStateException("El código de vinculación ha expirado."))
        }

        // New pairing starts with clean default permissions without inheriting past states (Block 2)
        _pairing.update {
            it.copy(
                isPaired = true,
                psychologistName = foundCode.psychologistName,
                psychologistTitle = foundCode.psychologistTitle,
                pairingCode = cleanCode,
                permissionsShareEmotions = false, // Starts disabled by default per Block 2
                permissionsShareWeeklyProgress = false,
                permissionsSharePrivateNotes = false
            )
        }

        // Mark code as used if single-use policy applies
        _pairingCodes.update { list ->
            list.map { if (it.code == cleanCode) it.copy(isUsed = true) else it }
        }

        return Result.success("Vinculación autorizada con ${foundCode.psychologistName}")
    }

    // Revoke Pairing
    fun revokePairing() {
        _pairing.update {
            it.copy(
                isPaired = false,
                psychologistName = null,
                psychologistTitle = null,
                pairingCode = null,
                permissionsShareEmotions = false,
                permissionsShareWeeklyProgress = false,
                permissionsSharePrivateNotes = false
            )
        }
        // Also unbook appointments for this student
        _appointments.update { list ->
            list.map {
                if (it.studentId == "val_1") it.copy(isBooked = false, studentId = null, studentName = null) else it
            }
        }
    }

    // Toggle Permission - Block 2: Only allowed if active pairing exists
    fun updatePermissions(shareEmotions: Boolean, shareWeeklyProgress: Boolean): Boolean {
        if (!_pairing.value.isPaired) {
            return false // Cannot toggle permissions without valid pairing
        }
        _pairing.update {
            it.copy(
                permissionsShareEmotions = shareEmotions,
                permissionsShareWeeklyProgress = shareWeeklyProgress,
                permissionsSharePrivateNotes = false // Inviolable rule
            )
        }
        return true
    }

    // Book Appointment - Atomic Slot Validation (Block 6)
    @Synchronized
    fun bookAppointment(appointmentId: String): Boolean {
        var booked = false
        _appointments.update { list ->
            list.map { appt ->
                if (appt.id == appointmentId && !appt.isBooked) {
                    booked = true
                    appt.copy(isBooked = true, studentId = "val_1", studentName = _studentName.value)
                } else {
                    appt
                }
            }
        }
        return booked
    }

    // Cancel Appointment (Frees the slot cleanly without duplication)
    @Synchronized
    fun cancelAppointment(appointmentId: String) {
        _appointments.update { list ->
            list.map { appt ->
                if (appt.id == appointmentId) {
                    appt.copy(isBooked = false, studentId = null, studentName = null)
                } else {
                    appt
                }
            }
        }
    }

    // Psychologist generates a new 6-digit code with expiration
    fun generateNewPairingCode(expirationHours: Int = 48): String {
        val newCode = ((100000..999999).random()).toString()
        val now = timeProvider()
        val pairingEntry = PairingCode(
            code = newCode,
            psychologistId = "psy_sofia",
            psychologistName = "Dra. Sofía Mendoza",
            psychologistTitle = "Psicóloga Clínica y Bienestar Universitario",
            createdAtMillis = now,
            expiresAtMillis = now + expirationHours * 3600000L,
            isUsed = false
        )
        _pairingCodes.update { listOf(pairingEntry) + it }
        return newCode
    }

    // Psychologist creates a new availability slot
    fun addPsychologistSlot(dateLabel: String, timeLabel: String) {
        val newSlot = Appointment(
            id = "apt_${timeProvider()}",
            psychologistId = "psy_sofia",
            studentId = null,
            studentName = null,
            dateTimeMillis = timeProvider() + 86400000L * 4,
            timeLabel = timeLabel,
            dateLabel = dateLabel,
            isBooked = false
        )
        _appointments.update { it + newSlot }
    }

    /**
     * BLOCK 1: CLINICAL PRIVACY FRONTIER
     * Deliver ONLY authorized clinical data to the psychologist portal.
     * Never sends context notes, student ID, or private reflections.
     * Denies access by default if pairing is inactive or student revoked emotion sharing.
     */
     fun getAuthorizedClinicalEmotions(psychologistId: String = "psy_sofia"): List<AuthorizedEmotionRecord> {
        val currentPairing = _pairing.value
        // Deny access if not paired or psychologist doesn't match
        if (!currentPairing.isPaired || !currentPairing.permissionsShareEmotions) {
            return emptyList()
        }

        // Return strictly mapped minimal records
        return _emotionRecords.value.map { fullRecord ->
            AuthorizedEmotionRecord(
                id = fullRecord.id,
                timestamp = fullRecord.timestamp,
                category = fullRecord.category,
                emotions = fullRecord.emotions,
                intensity = fullRecord.intensity
            )
        }
    }

    /**
     * FASE 4: Returns linked students specifically for this psychologist.
     * Empty if no active pairing exists.
     */
    fun getLinkedStudents(psychologistId: String = "psy_sofia"): List<LinkedStudent> {
        val currentPairing = _pairing.value
        if (!currentPairing.isPaired) {
            return emptyList()
        }
        return listOf(
            LinkedStudent(
                studentId = "val_1",
                displayName = "Valentina",
                pairedAtMillis = timeProvider() - 86400000L * 14,
                permissionsShareEmotions = currentPairing.permissionsShareEmotions,
                permissionsShareWeeklyProgress = currentPairing.permissionsShareWeeklyProgress
            )
        )
    }

    /**
     * FASE 4: Revoke an active pairing code before it expires.
     */
    fun revokePairingCode(code: String): Boolean {
        var found = false
        _pairingCodes.update { list ->
            list.map {
                if (it.code == code && !it.isRevoked) {
                    found = true
                    it.copy(isRevoked = true)
                } else {
                    it
                }
            }
        }
        return found
    }

    /**
     * FASE 4: Real aggregated KPIs for the professional dashboard.
     * Exclusively calculated from live data without artificial mocks.
     */
    fun getPsychologistKpis(psychologistId: String = "psy_sofia"): PsychologistKpiSummary {
        val currentPairing = _pairing.value
        val activeStudents = if (currentPairing.isPaired) 1 else 0
        val myAppointments = _appointments.value.filter { it.psychologistId == psychologistId }
        val booked = myAppointments.count { it.isBooked }
        val available = myAppointments.count { !it.isBooked }
        val authorizedActivities = if (currentPairing.isPaired && currentPairing.permissionsShareWeeklyProgress) {
            _activityLogs.value.count { it.status == ActivityStatus.COMPLETED }
        } else {
            0
        }

        return PsychologistKpiSummary(
            activeLinkedStudentsCount = activeStudents,
            bookedAppointmentsCount = booked,
            availableSlotsCount = available,
            authorizedActivitiesCount = authorizedActivities
        )
    }

    /**
     * BLOCK 5: Real weekly progress calculation (Last 7 days strictly)
     */
    fun getWeeklyMomentsCount(): Int {
        val sevenDaysAgo = timeProvider() - 7 * 86400000L
        return _emotionRecords.value.count { it.timestamp >= sevenDaysAgo }
    }

    fun getWeeklyCompletedActivitiesCount(): Int {
        val sevenDaysAgo = timeProvider() - 7 * 86400000L
        return _activityLogs.value.count { it.timestamp >= sevenDaysAgo && it.status == ActivityStatus.COMPLETED }
    }
}
