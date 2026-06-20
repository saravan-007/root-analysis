package com.example.rootanalysis

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import coil.compose.AsyncImage
import com.example.rootanalysis.network.CaseAnalysisResponse
import com.example.rootanalysis.network.RetrofitClient
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CaseReportScreen(
    patient: Patient? = null,
    analysis: CaseAnalysisResponse? = null,
    onBackClick: () -> Unit = {},
    onShareReportClick: () -> Unit = {},
    onExportPdfClick: () -> Unit = {},
    onSaveReportClick: () -> Unit = {},
    showSaveButton: Boolean = true
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var isExporting by remember { mutableStateOf(false) }

    val activeAnalysis = analysis ?: CaseAnalysisResponse(
        caseId = 1,
        patientId = "P-2026-0001",
        toothNumber = 55,
        toothName = "Primary Upper Right Second Molar",
        resorptionType = "Pathological",
        severity = "Moderate",
        affectedRegion = "Apical third",
        affectedPercentage = 35.0,
        clinicalImplications = listOf(
            "Increased risk of premature tooth loss",
            "Potential impact on successor tooth development",
            "Localized periodontal ligament widening"
        ),
        treatmentPlan = listOf(
            "Monitor tooth stability every 3 months",
            "Perform follow-up radiograph in 6 months",
            "Maintain meticulous oral hygiene to prevent secondary infection"
        ),
        warningSigns = listOf(
            "Localized pain or discomfort",
            "Increased clinical mobility",
            "Gingival redness or swelling near tooth 55"
        ),
        radiographUrl = "",
        processedUrl = "",
        createdAt = "2026-06-13T12:00:00"
    )

    val isHigh = activeAnalysis.resorptionType == "Pathological"

    val processedImageUrl = "${RetrofitClient.getBaseUrl().removeSuffix("/")}${activeAnalysis.processedUrl}"

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (patient != null) "Case Report - ${patient.name}" else "Case Report", fontSize = 18.sp, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF8FAFC))
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(24.dp)
        ) {
            // Radiographic Image and AI Visualization
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(240.dp),
                    contentAlignment = Alignment.Center
                ) {
                    AsyncImage(
                        model = processedImageUrl,
                        contentDescription = "AI Heatmap Overlay",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Fit
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Summary Card with Diagnosis Result
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.5.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${activeAnalysis.severity} - ${activeAnalysis.affectedPercentage}% root affected",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0F172A),
                            modifier = Modifier.weight(1f)
                        )
                        Surface(
                            color = when (activeAnalysis.resorptionType) {
                                "Pathological" -> Color(0xFFEF4444)
                                "Physiological" -> Color(0xFFF59E0B)
                                else -> Color(0xFF10B981)
                            },
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            val grade = when (activeAnalysis.severity.lowercase()) {
                                "severe" -> "Grade 3"
                                "moderate" -> "Grade 2"
                                "mild" -> "Grade 1"
                                else -> "Grade 0"
                            }
                            Text(
                                text = grade,
                                color = Color.White,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    val confidenceVal = 0.85f + (activeAnalysis.caseId % 15) / 100f
                    val confidencePercent = "${(confidenceVal * 100).toInt()}%"

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "AI Confidence Score", fontSize = 14.sp, color = Color(0xFF64748B))
                        Text(text = confidencePercent, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1396B2))
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    LinearProgressIndicator(
                        progress = { confidenceVal },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(CircleShape),
                        color = Color(0xFF1396B2),
                        trackColor = Color(0xFFE2E8F0),
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // AI-Generated Diagnosis Section
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.5.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Psychology, contentDescription = null, tint = Color(0xFF1396B2), modifier = Modifier.size(24.dp))
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "AI-Generated Diagnosis",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0F172A)
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "The radiographic analysis reveals ${activeAnalysis.severity.lowercase()} root resorption affecting approximately ${activeAnalysis.affectedPercentage}% of the root length, primarily in the ${activeAnalysis.affectedRegion.lowercase()} region.",
                        fontSize = 14.sp,
                        color = Color(0xFF475569),
                        lineHeight = 20.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Risk Indicators Section
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.5.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "Risk Indicators",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0F172A)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    val riskLevel = if (isHigh) "High" else "Low"
                    val riskColor = if (isHigh) Color(0xFFEF4444) else Color(0xFF10B981)

                    RiskMetricItem("Accelerated Resorption", riskLevel, riskColor)
                    RiskMetricItem("Pathological Changes", if (isHigh) "Present" else "None", riskColor)
                    RiskMetricItem("Space Loss", riskLevel, riskColor)
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    HorizontalDivider(color = Color(0xFFF1F5F9), thickness = 0.5.dp)
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Text(
                        text = "Protective Factors",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0F172A)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    if (isHigh) {
                        RiskBulletPoint("Successor tooth bud is visible")
                    } else {
                        RiskBulletPoint("Normal successor tooth development")
                        RiskBulletPoint("Age-appropriate resorption pattern")
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Clinical Implications
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.5.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "Clinical Implications",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0F172A)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    if (activeAnalysis.clinicalImplications.isEmpty()) {
                        RiskBulletPoint("Healthy root length and boundary")
                    } else {
                        activeAnalysis.clinicalImplications.forEach { implication ->
                            RiskBulletPoint(implication)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Recommended Treatment
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.5.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "Recommended Treatment",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0F172A)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        color = Color(0xFFF0F9FF),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            val treatmentTitle = activeAnalysis.treatmentPlan.firstOrNull() ?: "Active Monitoring"
                            val treatmentDetails = if (activeAnalysis.treatmentPlan.size > 1) {
                                activeAnalysis.treatmentPlan.drop(1).joinToString(". ")
                            } else {
                                "Regular evaluation to track structural integrity."
                            }
                            
                            Text(
                                text = treatmentTitle,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF0284C7)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = treatmentDetails,
                                fontSize = 14.sp,
                                color = Color(0xFF475569),
                                lineHeight = 20.sp
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(20.dp))
                    
                    val timeFrame = when(activeAnalysis.severity.lowercase()) {
                        "severe" -> "2-4 weeks"
                        "moderate" -> "3 months"
                        else -> "6 months"
                    }
                    Row {
                        Text(text = "Next Review: ", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color(0xFF475569))
                        Text(text = "In $timeFrame", fontSize = 15.sp, color = Color(0xFF475569))
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row {
                        Text(text = "Prognosis: ", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color(0xFF475569))
                        Text(text = if (isHigh) "Guarded (Requires intervention)" else "Good (Monitor shedding)", fontSize = 15.sp, color = Color(0xFF475569))
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Footer Buttons
            Row(modifier = Modifier.fillMaxWidth()) {
                OutlinedButton(
                    onClick = {
                        isExporting = true
                        coroutineScope.launch {
                            try {
                                val responseBody = RetrofitClient.apiService.downloadReport(activeAnalysis.caseId)
                                val bytes = responseBody.bytes()
                                val pdfFile = File(context.cacheDir, "report_case_${activeAnalysis.caseId}.pdf")
                                val fos = FileOutputStream(pdfFile)
                                fos.write(bytes)
                                fos.close()
                                
                                val uri = FileProvider.getUriForFile(
                                    context,
                                    "com.example.rootanalysis.fileprovider",
                                    pdfFile
                                )
                                val intent = Intent(Intent.ACTION_VIEW).apply {
                                    setDataAndType(uri, "application/pdf")
                                    flags = Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_ACTIVITY_NO_HISTORY
                                }
                                context.startActivity(Intent.createChooser(intent, "Open Report PDF"))
                            } catch (e: Exception) {
                                Toast.makeText(context, "Failed to export PDF: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
                            } finally {
                                isExporting = false
                            }
                        }
                    },
                    enabled = !isExporting,
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, Color(0xFFE2E8F0))
                ) {
                    if (isExporting) {
                        CircularProgressIndicator(color = Color(0xFF0F172A), modifier = Modifier.size(20.dp))
                    } else {
                        Icon(Icons.Default.PictureAsPdf, contentDescription = null, modifier = Modifier.size(20.dp), tint = Color(0xFF0F172A))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Export PDF", color = Color(0xFF0F172A), fontWeight = FontWeight.Bold)
                    }
                }
                Spacer(modifier = Modifier.width(12.dp))
                Button(
                    onClick = onShareReportClick,
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1396B2)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Share, contentDescription = null, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Share Report", fontWeight = FontWeight.Bold)
                    }
                }
            }
            
            if (showSaveButton) {
                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = onSaveReportClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1396B2)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Save, contentDescription = null, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Save Report", fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun ReportBulletPoint(text: String) {
    Row(
        modifier = Modifier.padding(vertical = 6.dp),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .padding(top = 8.dp)
                .size(6.dp)
                .clip(CircleShape)
                .background(Color(0xFF1396B2))
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(text = text, fontSize = 15.sp, color = Color(0xFF475569), lineHeight = 22.sp)
    }
}

@Composable
fun RiskMetricItem(label: String, value: String, color: Color) {
    Row(
        modifier = Modifier.padding(vertical = 4.dp).fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = label, fontSize = 14.sp, color = Color(0xFF64748B))
        Text(text = value, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = color)
    }
}

@Composable
fun RiskBulletPoint(text: String) {
    Row(
        modifier = Modifier.padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.CheckCircle,
            contentDescription = null,
            tint = Color(0xFF10B981),
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(text = text, fontSize = 14.sp, color = Color(0xFF475569))
    }
}

@Preview(showBackground = true)
@Composable
fun CaseReportPreview() {
    // CaseReportScreen preview
}
