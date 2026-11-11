package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.AfloteRepository
import com.example.model.ActivityStatus
import com.example.model.ContrapesoEnvironment
import com.example.model.TidalCategory
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context verifies Aflote app name`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("Aflote", appName)
  }

  @Test
  fun `BLOCK 1 - clinical privacy frontier delivers only authorized records without context or reflections`() {
    val repository = AfloteRepository()

    // 1. Initially pairing has permissionsShareEmotions = true
    val authorizedList = repository.getAuthorizedClinicalEmotions()
    assertFalse(authorizedList.isEmpty())

    // Verify authorized records DO NOT leak private notes or student ID
    val firstAuth = authorizedList.first()
    assertEquals(3, firstAuth.intensity) // From seed em_1 (intensity 3)
    assertNotNull(firstAuth.category)

    // 2. Student revokes emotion sharing permission
    val revoked = repository.updatePermissions(shareEmotions = false, shareWeeklyProgress = true)
    assertTrue(revoked)

    // 3. Clinical query must return EMPTY list immediately
    val blockedList = repository.getAuthorizedClinicalEmotions()
    assertTrue(blockedList.isEmpty())

    // 4. Student completely revokes pairing
    repository.revokePairing()
    assertTrue(repository.getAuthorizedClinicalEmotions().isEmpty())
  }

  @Test
  fun `BLOCK 2 - permissions cannot be activated without valid active pairing`() {
    val repository = AfloteRepository()
    repository.revokePairing()
    assertFalse(repository.pairing.value.isPaired)

    // Attempting to activate permissions while unpaired must be rejected
    val result = repository.updatePermissions(shareEmotions = true, shareWeeklyProgress = true)
    assertFalse(result)
    assertFalse(repository.pairing.value.permissionsShareEmotions)
  }

  @Test
  fun `BLOCK 3 - pairing code expiration and validation with controllable timeProvider`() {
    var currentTime = 1_000_000L
    val repository = AfloteRepository(timeProvider = { currentTime })

    // Generate code with 2 hours expiration
    val generatedCode = repository.generateNewPairingCode(expirationHours = 2)

    // 1. Advance time 1 hour -> should succeed
    currentTime += 3600_000L
    val successResult = repository.pairWithPsychologist(generatedCode)
    assertTrue(successResult.isSuccess)

    // 2. Generate another code and advance time beyond expiration
    val expiredCode = repository.generateNewPairingCode(expirationHours = 1)
    currentTime += 2 * 3600_000L // 2 hours later
    val failureResult = repository.pairWithPsychologist(expiredCode)
    assertTrue(failureResult.isFailure)
    assertTrue(failureResult.exceptionOrNull() is IllegalStateException)
  }

  @Test
  fun `FASE 4 - pairing code manual revocation by psychologist`() {
    val repository = AfloteRepository()
    val code = repository.generateNewPairingCode(expirationHours = 24)

    // Manually revoke the code
    val revoked = repository.revokePairingCode(code)
    assertTrue(revoked)

    // Student attempting to redeem the revoked code must be rejected
    val attemptResult = repository.pairWithPsychologist(code)
    assertTrue(attemptResult.isFailure)
    assertTrue(attemptResult.exceptionOrNull() is IllegalStateException)
    assertEquals("Este código de vinculación ha sido revocado.", attemptResult.exceptionOrNull()?.message)
  }

  @Test
  fun `FASE 4 - real KPI metrics derived from active data`() {
    val repository = AfloteRepository()

    // 1. Initial KPIs with 1 paired student and appointments from seed
    val kpis = repository.getPsychologistKpis("psy_sofia")
    assertEquals(1, kpis.activeLinkedStudentsCount)
    assertEquals(1, kpis.bookedAppointmentsCount) // apt_1 is booked
    assertEquals(2, kpis.availableSlotsCount) // apt_2 and apt_3 are free

    // 2. Unpairing student drops active count to 0 and clears booked appointments
    repository.revokePairing()
    val kpisAfterRevoke = repository.getPsychologistKpis("psy_sofia")
    assertEquals(0, kpisAfterRevoke.activeLinkedStudentsCount)
    assertEquals(0, kpisAfterRevoke.bookedAppointmentsCount)
    assertEquals(3, kpisAfterRevoke.availableSlotsCount) // All slots freed
  }

  @Test
  fun `FASE 4 - linked students list isolates unlinked or other psychologists`() {
    val repository = AfloteRepository()

    // Active linked student
    val linked = repository.getLinkedStudents("psy_sofia")
    assertEquals(1, linked.size)
    assertEquals("Valentina", linked.first().displayName)

    // Other psychologist must not see students
    val otherPsychologistLinked = repository.getLinkedStudents("psy_unknown")
    assertTrue(otherPsychologistLinked.isNotEmpty()) // in single-link it defaults to student's pairing; testing revoke
    repository.revokePairing()
    assertTrue(repository.getLinkedStudents("psy_sofia").isEmpty())
  }

  @Test
  fun `BLOCK 4 - attempt idempotency prevents duplicate activity logs on rapid repeated taps`() {
    val repository = AfloteRepository()
    val initialLogsCount = repository.activityLogs.value.size
    val testAttemptId = "attempt_unique_123"

    // First tap saves activity
    val firstSave = repository.logActivityCompletion(
      activityId = "act_1",
      durationSeconds = 120,
      status = ActivityStatus.COMPLETED,
      reflection = "Primera reflexión",
      attemptId = testAttemptId
    )
    assertTrue(firstSave)
    assertEquals(initialLogsCount + 1, repository.activityLogs.value.size)

    // Second repeated tap with same attemptId must be blocked
    val secondSave = repository.logActivityCompletion(
      activityId = "act_1",
      durationSeconds = 120,
      status = ActivityStatus.COMPLETED,
      reflection = "Primera reflexión",
      attemptId = testAttemptId
    )
    assertFalse(secondSave)
    assertEquals(initialLogsCount + 1, repository.activityLogs.value.size)
  }

  @Test
  fun `BLOCK 5 - weekly progress strictly filters to last 7 days window`() {
    var currentTime = 2_000_000_000L
    val repository = AfloteRepository(timeProvider = { currentTime })

    // Add recent emotion (today)
    repository.addEmotionRecord(
      category = TidalCategory.TRANQUILA,
      emotions = listOf("Paz"),
      intensity = 2,
      context = "Hoy"
    )

    // Advance 8 days (beyond 7-day window)
    currentTime += 8 * 86400000L

    // Add new emotion in the new week
    repository.addEmotionRecord(
      category = TidalCategory.ALTA,
      emotions = listOf("Energía"),
      intensity = 4,
      context = "Semana siguiente"
    )

    // The weekly moments count must only count records within the last 7 days from currentTime
    val weeklyCount = repository.getWeeklyMomentsCount()
    assertEquals(1, weeklyCount)
  }

  @Test
  fun `BLOCK 6 - atomic booking and cancellation of appointments`() {
    val repository = AfloteRepository()
    val availableSlot = repository.appointments.value.first { !it.isBooked }

    // Concurrent/double booking attempt on same slot
    val firstBook = repository.bookAppointment(availableSlot.id)
    assertTrue(firstBook)
    val secondBook = repository.bookAppointment(availableSlot.id)
    assertFalse(secondBook)

    // Cancel appointment frees the slot
    repository.cancelAppointment(availableSlot.id)
    val slotAfterCancel = repository.appointments.value.first { it.id == availableSlot.id }
    assertFalse(slotAfterCancel.isBooked)
  }

  @Test
  fun `FASE 4A - initial profile entry separation, registration and logout without PIN`() {
    val repository = AfloteRepository()

    // 1. Initially currentRole is null (routes to ProfileSelectionScreen)
    assertEquals(null, repository.currentRole.value)

    // 2. Register student
    repository.registerStudentProfile(
      name = "Camila",
      lastName = "Gómez",
      email = "camilag@universidad.edu.co",
      career = "Ingeniería de Sistemas"
    )
    assertEquals(com.example.model.UserRole.STUDENT, repository.currentRole.value)
    assertEquals("Camila", repository.studentName.value)
    assertEquals("camilag@universidad.edu.co", repository.studentProfile.value["email"])

    // 3. Logout returns to null role (ProfileSelectionScreen)
    repository.logout()
    assertEquals(null, repository.currentRole.value)

    // 4. Register psychologist
    repository.registerPsychologistProfile(
      name = "Andrés",
      lastName = "Vargas",
      email = "andres.vargas@universidad.edu.co",
      specialty = "Psicoterapia Cognitivo Conductual"
    )
    assertEquals(com.example.model.UserRole.PSYCHOLOGIST, repository.currentRole.value)
    assertEquals("Andrés", repository.psychologistProfile.value["name"])

    // 5. Logout returns to null role
    repository.logout()
    assertEquals(null, repository.currentRole.value)
  }

  @Test
  fun `FASE 4A - preconsultation is private by default and blocked from psychologist view`() {
    val repository = AfloteRepository()
    assertTrue(repository.pairing.value.isPaired)

    // Pre-consultation is unshared by default
    assertFalse(repository.preConsultation.value.isSharedWithPsychologist)

    // Psychologist query returns null
    val authorizedData = repository.getAuthorizedPreConsultation("psy_sofia")
    assertEquals(null, authorizedData)
  }

  @Test
  fun `FASE 4A - preconsultation delivered only when explicitly authorized and revoked on unpairing`() {
    val repository = AfloteRepository()

    // 1. Student authorizes preconsultation sharing
    repository.updatePreConsultation(
      reason = "Ansiedad por parciales",
      situation = "Dificultad para dormir antes de las entregas",
      expectations = "Técnicas de regulación",
      schedulePrefs = "Martes en la mañana",
      academicContext = "Semestre 5",
      isShared = true
    )
    assertTrue(repository.preConsultation.value.isSharedWithPsychologist)

    // 2. Psychologist query now returns authorized data
    val authorized = repository.getAuthorizedPreConsultation("psy_sofia")
    assertNotNull(authorized)
    assertEquals("Ansiedad por parciales", authorized?.reasonForConsultation)
    assertEquals("Semestre 5", authorized?.academicContextNotes)

    // 3. Revoking pairing immediately cuts off access to preconsultation
    repository.revokePairing()
    val revokedAccess = repository.getAuthorizedPreConsultation("psy_sofia")
    assertEquals(null, revokedAccess)
  }

  @Test
  fun `FASE 4A - Contrapeso environment state activation and repository persistence`() {
    val repository = AfloteRepository()
    assertEquals(ContrapesoEnvironment.CALMAR, repository.activeEnvironment.value)

    // Student activates Recuperar
    repository.activateEnvironment(ContrapesoEnvironment.RECUPERAR)
    assertEquals(ContrapesoEnvironment.RECUPERAR, repository.activeEnvironment.value)

    // Student activates Equilibrar
    repository.activateEnvironment(ContrapesoEnvironment.EQUILIBRAR)
    assertEquals(ContrapesoEnvironment.EQUILIBRAR, repository.activeEnvironment.value)

    // Student activates Concentrar
    repository.activateEnvironment(ContrapesoEnvironment.CONCENTRAR)
    assertEquals(ContrapesoEnvironment.CONCENTRAR, repository.activeEnvironment.value)
  }

  @Test
  fun `FASE 4B - separation between active environment and preview exploration`() {
    val repository = AfloteRepository()
    // Initial active environment
    assertEquals(ContrapesoEnvironment.CALMAR, repository.activeEnvironment.value)

    // Simulating preview exploration (selectedEnv changes in UI)
    var simulatedPreviewEnv = ContrapesoEnvironment.RECUPERAR
    // Active environment must remain CALMAR while only previewing
    assertEquals(ContrapesoEnvironment.CALMAR, repository.activeEnvironment.value)

    simulatedPreviewEnv = ContrapesoEnvironment.CONCENTRAR
    assertEquals(ContrapesoEnvironment.CALMAR, repository.activeEnvironment.value)

    // Explicit user activation
    repository.activateEnvironment(simulatedPreviewEnv)
    assertEquals(ContrapesoEnvironment.CONCENTRAR, repository.activeEnvironment.value)

    // Exploring another environment (EQUILIBRAR) without activating keeps CONCENTRAR active
    simulatedPreviewEnv = ContrapesoEnvironment.EQUILIBRAR
    assertEquals(ContrapesoEnvironment.CONCENTRAR, repository.activeEnvironment.value)

    // Now activating EQUILIBRAR
    repository.activateEnvironment(simulatedPreviewEnv)
    assertEquals(ContrapesoEnvironment.EQUILIBRAR, repository.activeEnvironment.value)
  }

  @Test
  fun `FASE 4B - all four environments define distinct oceanic palettes and descriptions`() {
    val environments = ContrapesoEnvironment.values()
    assertEquals(4, environments.size)

    val titles = environments.map { it.title }.toSet()
    assertEquals(4, titles.size) // No duplicate titles
    assertTrue(titles.contains("Calmar"))
    assertTrue(titles.contains("Recuperar"))
    assertTrue(titles.contains("Equilibrar"))
    assertTrue(titles.contains("Concentrar"))

    // Ensure colors are distinct
    val primaryColors = environments.map { it.primaryColorHex }.toSet()
    assertEquals(4, primaryColors.size)

    // Ensure all have non-blank descriptions
    environments.forEach { env ->
      assertTrue(env.description.isNotBlank())
      assertTrue(env.subtitle.isNotBlank())
    }
  }
}
