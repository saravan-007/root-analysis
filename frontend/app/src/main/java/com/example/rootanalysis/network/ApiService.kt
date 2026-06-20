package com.example.rootanalysis.network

import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.http.*

interface ApiService {
    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): LoginResponse

    @FormUrlEncoded
    @POST("signup")
    suspend fun signup(
        @Field("name") name: String,
        @Field("license") license: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): SignupResponse

    @FormUrlEncoded
    @POST("update_profile")
    suspend fun updateProfile(
        @Field("id") id: Int,
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("phone") phone: String,
        @Field("clinic") clinic: String
    ): UpdateProfileResponse

    @GET("patients")
    suspend fun getPatients(
        @Query("search") search: String? = null
    ): List<com.example.rootanalysis.Patient>

    @POST("patients")
    suspend fun createPatient(
        @Body patient: com.example.rootanalysis.Patient
    ): com.example.rootanalysis.Patient

    @GET("patients/{patient_id}")
    suspend fun getPatientDetails(
        @Path("patient_id") patientId: String
    ): PatientDetailResponse

    @Multipart
    @POST("cases/analyze")
    suspend fun analyzeRadiograph(
        @Part("patient_id") patientId: RequestBody,
        @Part("tooth_number") toothNumber: RequestBody,
        @Part("tooth_name") toothName: RequestBody?,
        @Part("clinical_notes") clinicalNotes: RequestBody?,
        @Part("doctor_id") doctorId: RequestBody?,
        @Part file: MultipartBody.Part
    ): CaseAnalysisResponse

    @GET("cases/{case_id}/report")
    @Streaming
    suspend fun downloadReport(
        @Path("case_id") caseId: Int
    ): ResponseBody
}
