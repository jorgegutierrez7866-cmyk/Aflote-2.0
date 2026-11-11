package com.example.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.data.AfloteRepository
import com.example.model.*
import kotlinx.coroutines.flow.StateFlow

class AfloteViewModel(
    val repository: AfloteRepository = AfloteRepository()
) : ViewModel() {

    val currentRole: StateFlow<UserRole?> = repository.currentRole
    val studentName: StateFlow<String> = repository.studentName
    val studentProfile: StateFlow<Map<String, String>> = repository.studentProfile
    val psychologistProfile: StateFlow<Map<String, String>> = repository.psychologistProfile
    val preConsultation: StateFlow<PreConsultationData> = repository.preConsultation

    val activeEnvironment: StateFlow<ContrapesoEnvironment> = repository.activeEnvironment
    val emotionRecords: StateFlow<List<EmotionRecord>> = repository.emotionRecords
    val activities: StateFlow<List<ActivityItem>> = repository.activities
    val activityLogs: StateFlow<List<ActivityLogEntry>> = repository.activityLogs
    val pairing: StateFlow<StudentPsychologistPairing> = repository.pairing
    val appointments: StateFlow<List<Appointment>> = repository.appointments
    val routeStations: StateFlow<List<RouteStation>> = repository.routeStations
    val psychologistActiveCode: StateFlow<String> = repository.psychologistActiveCode
    val pairingCodes: StateFlow<List<PairingCode>> = repository.pairingCodes

    // FASE 4A: Profile registration and session handling
    fun registerStudentProfile(name: String, lastName: String, email: String, career: String) =
        repository.registerStudentProfile(name, lastName, email, career)

    fun registerPsychologistProfile(name: String, lastName: String, email: String, specialty: String) =
        repository.registerPsychologistProfile(name, lastName, email, specialty)

    fun logout() = repository.logout()

    fun switchRole(role: UserRole?) = repository.switchRole(role)

    // FASE 4A: Pre-consultation
    fun updatePreConsultation(
        reason: String,
        situation: String,
        expectations: String,
        schedulePrefs: String,
        academicContext: String,
        isShared: Boolean
    ) = repository.updatePreConsultation(reason, situation, expectations, schedulePrefs, academicContext, isShared)

    fun getAuthorizedPreConsultation(psychologistId: String = "psy_sofia"): PreConsultationData? =
        repository.getAuthorizedPreConsultation(psychologistId)

    fun activateEnvironment(env: ContrapesoEnvironment) = repository.activateEnvironment(env)
    fun addEmotionRecord(category: TidalCategory, emotions: List<String>, intensity: Int, context: String) =
        repository.addEmotionRecord(category, emotions, intensity, context)

    fun logActivityCompletion(activityId: String, durationSeconds: Int, status: ActivityStatus, reflection: String, attemptId: String) =
        repository.logActivityCompletion(activityId, durationSeconds, status, reflection, attemptId)

    fun pairWithPsychologist(code: String) = repository.pairWithPsychologist(code)
    fun revokePairing() = repository.revokePairing()
    fun updatePermissions(shareEmotions: Boolean, shareWeeklyProgress: Boolean) =
        repository.updatePermissions(shareEmotions, shareWeeklyProgress)

    fun bookAppointment(appointmentId: String) = repository.bookAppointment(appointmentId)
    fun cancelAppointment(appointmentId: String) = repository.cancelAppointment(appointmentId)
    fun generateNewPairingCode() = repository.generateNewPairingCode()
    fun revokePairingCode(code: String) = repository.revokePairingCode(code)
    fun addPsychologistSlot(dateLabel: String, timeLabel: String) =
        repository.addPsychologistSlot(dateLabel, timeLabel)

    // BLOCK 1: Privacy frontier query
    fun getAuthorizedClinicalEmotions(): List<AuthorizedEmotionRecord> =
        repository.getAuthorizedClinicalEmotions()

    // FASE 4: Clinical KPIs and Linked Students
    fun getPsychologistKpis(): PsychologistKpiSummary =
        repository.getPsychologistKpis()

    fun getLinkedStudents(): List<LinkedStudent> =
        repository.getLinkedStudents()

    // BLOCK 5: Weekly strictly 7-day progress
    fun getWeeklyMomentsCount(): Int = repository.getWeeklyMomentsCount()
    fun getWeeklyCompletedActivitiesCount(): Int = repository.getWeeklyCompletedActivitiesCount()
}
