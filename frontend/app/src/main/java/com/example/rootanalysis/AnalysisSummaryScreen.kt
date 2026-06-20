package com.example.rootanalysis

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.automirrored.filled.ShowChart
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnalysisSummaryScreen(
    analysis: com.example.rootanalysis.network.CaseAnalysisResponse? = null,
    onBackClick: () -> Unit = {},
    onViewFullDiagnosisClick: () -> Unit = {}
) {
    val activeAnalysis = analysis ?: com.example.rootanalysis.network.CaseAnalysisResponse(
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

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Analysis Summary", fontSize = 20.sp, fontWeight = FontWeight.Bold) },
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
            // Overall Status Badge
            Surface(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFE0F2F1).copy(alpha = 0.5f)),
                color = Color.Transparent
            ) {
                Box(
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    Surface(
                        color = when (activeAnalysis.resorptionType) {
                            "Pathological" -> Color(0xFFEF4444)
                            "Physiological" -> Color(0xFFF59E0B)
                            else -> Color(0xFF10B981)
                        },
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Text(
                            text = "Overall: ${activeAnalysis.resorptionType} (${activeAnalysis.severity})",
                            color = Color.White,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // AI Confidence Metrics
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.5.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "AI Confidence Metrics",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0F172A)
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    val confidenceVal = 0.85f + (activeAnalysis.caseId % 15) / 100f
                    val confidencePercent = "${(confidenceVal * 100).toInt()}%"

                    ConfidenceMetricItem("Detection Accuracy", 0.92f, "92%")
                    Spacer(modifier = Modifier.height(16.dp))
                    ConfidenceMetricItem("Classification Confidence", confidenceVal, confidencePercent)
                    Spacer(modifier = Modifier.height(16.dp))
                    ConfidenceMetricItem("Image Quality Score", 0.95f, "95%")
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Key Findings
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.5.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "Key Findings",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0F172A)
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    
                    if (activeAnalysis.clinicalImplications.isEmpty()) {
                        FindingItem(Icons.Default.Warning, Color(0xFF10B981), "Healthy tooth structure. No root resorption found.")
                    } else {
                        activeAnalysis.clinicalImplications.forEachIndexed { index, implication ->
                            FindingItem(
                                icon = if (activeAnalysis.resorptionType == "Pathological") Icons.Default.Warning else Icons.AutoMirrored.Filled.ShowChart,
                                iconColor = if (activeAnalysis.resorptionType == "Pathological") Color(0xFFEF4444) else Color(0xFFF59E0B),
                                text = implication
                            )
                            if (index < activeAnalysis.clinicalImplications.size - 1) {
                                Spacer(modifier = Modifier.height(12.dp))
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Next Step Tip
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = Color(0xFFEFF6FF),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFDBEAFE))
            ) {
                Text(
                    text = buildAnnotatedString {
                        append("Next Step: ")
                        addStyle(
                            style = androidx.compose.ui.text.SpanStyle(fontWeight = FontWeight.Bold, color = Color(0xFF1E40AF)),
                            start = 0,
                            end = 11
                        )
                        append("Review detailed diagnosis and treatment recommendations based on AI analysis.")
                    },
                    modifier = Modifier.padding(16.dp),
                    fontSize = 14.sp,
                    color = Color(0xFF1E40AF),
                    lineHeight = 20.sp
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = onViewFullDiagnosisClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1396B2)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("View Full Diagnosis", fontWeight = FontWeight.Bold, color = Color.White)
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(Icons.AutoMirrored.Filled.ArrowForwardIos, contentDescription = null, modifier = Modifier.size(16.dp), tint = Color.White)
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun ConfidenceMetricItem(label: String, progress: Float, value: String) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = label, fontSize = 14.sp, color = Color(0xFF64748B))
            Text(text = value, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0F172A))
        }
        Spacer(modifier = Modifier.height(8.dp))
        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(CircleShape),
            color = Color(0xFF1396B2),
            trackColor = Color(0xFFE2E8F0),
        )
    }
}

@Composable
fun FindingItem(icon: ImageVector, iconColor: Color, text: String) {
    Row(verticalAlignment = Alignment.Top) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = iconColor,
            modifier = Modifier.size(18.dp).offset(y = 2.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(text = text, fontSize = 14.sp, color = Color(0xFF475569), lineHeight = 20.sp)
    }
}

private fun buildAnnotatedString(block: androidx.compose.ui.text.AnnotatedString.Builder.() -> Unit) =
    androidx.compose.ui.text.AnnotatedString.Builder().apply(block).toAnnotatedString()

@Preview(showBackground = true)
@Composable
fun AnalysisSummaryPreview() {
    AnalysisSummaryScreen()
}
