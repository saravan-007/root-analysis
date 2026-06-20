package com.example.rootanalysis.network

import com.google.gson.annotations.SerializedName

data class Doctor(
    val id: Int,
    val name: String,
    val email: String,
    @SerializedName("medical_license") val medicalLicense: String,
    val phone: String?,
    val clinic: String?
)

data class LoginResponse(
    val success: Boolean,
    val message: String,
    val id: Int?,
    val name: String?,
    val email: String?,
    @SerializedName("medical_license") val medicalLicense: String?,
    val phone: String?,
    val clinic: String?
)

data class SignupResponse(
    val success: Boolean,
    val message: String,
    val id: Int?
)

data class ProfileResponse(
    val success: Boolean,
    val message: String?,
    val name: String?,
    val email: String?,
    val phone: String?,
    val clinic: String?,
    @SerializedName("medical_license") val medicalLicense: String?
)

data class UpdateProfileResponse(
    val success: Boolean,
    val message: String
)

data class PatientDetailResponse(
    val id: String,
    val name: String,
    val age: Int,
    val gender: String,
    @SerializedName("last_visit") val lastVisit: String?,
    @SerializedName("active_cases") val activeCases: Int,
    val guardian: String?,
    val phone: String?,
    val email: String?,
    val cases: List<CaseAnalysisResponse> = emptyList()
)

data class CaseAnalysisResponse(
    @SerializedName("case_id") val caseId: Int,
    @SerializedName("patient_id") val patientId: String,
    @SerializedName("tooth_number") val toothNumber: Int,
    @SerializedName("tooth_name") val toothName: String,
    @SerializedName("resorption_type") val resorptionType: String,
    val severity: String,
    @SerializedName("affected_region") val affectedRegion: String,
    @SerializedName("affected_percentage") val affectedPercentage: Double,
    @SerializedName("clinical_implications") val clinicalImplications: List<String>,
    @SerializedName("treatment_plan") val treatmentPlan: List<String>,
    @SerializedName("warning_signs") val warningSigns: List<String>,
    @SerializedName("radiograph_url") val radiographUrl: String,
    @SerializedName("processed_url") val processedUrl: String,
    @SerializedName("created_at") val createdAt: String
)
