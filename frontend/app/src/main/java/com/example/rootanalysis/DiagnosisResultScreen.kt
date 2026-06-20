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
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiagnosisResultScreen(
    analysis: com.example.rootanalysis.network.CaseAnalysisResponse? = null,
    onBackClick: () -> Unit = {},
    onSeverityDetailsClick: () -> Unit = {},
    onTreatmentPlanClick: () -> Unit = {},
    onViewDecisionTreeClick: () -> Unit = {}
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

    val headerContainerColor = when(activeAnalysis.resorptionType) {
        "Pathological" -> Color(0xFFFEF2F2)
        "Physiological" -> Color(0xFFFFF7ED)
        else -> Color(0xFFF0FDF4)
    }
    
    val headerBorderColor = when(activeAnalysis.resorptionType) {
        "Pathological" -> Color(0xFFFCA5A5)
        "Physiological" -> Color(0xFFFDBA74)
        else -> Color(0xFF86EFAC)
    }

    val badgeColor = when(activeAnalysis.resorptionType) {
        "Pathological" -> Color(0xFFEF4444)
        "Physiological" -> Color(0xFFF59E0B)
        else -> Color(0xFF10B981)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Diagnosis Result", fontSize = 20.sp, fontWeight = FontWeight.Bold) },
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
            // Result Header Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = headerContainerColor),
                border = androidx.compose.foundation.BorderStroke(1.dp, headerBorderColor)
            ) {
                Row(
                    modifier = Modifier.padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .clip(CircleShape)
                            .background(badgeColor),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Info, contentDescription = null, tint = Color.White, modifier = Modifier.size(32.dp))
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(
                            text = "${activeAnalysis.resorptionType} Resorption",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0F172A)
                        )
                        Text(
                            text = "Tooth ${activeAnalysis.toothNumber} (${activeAnalysis.toothName})",
                            fontSize = 13.sp,
                            color = Color(0xFF64748B)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Surface(
                            color = badgeColor,
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            val grade = when (activeAnalysis.severity.lowercase()) {
                                "severe" -> "Grade 3 - Severe"
                                "moderate" -> "Grade 2 - Moderate"
                                "mild" -> "Grade 1 - Mild"
                                else -> "Grade 0 - None"
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
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // AI-Generated Diagnosis
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
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
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    val diagnosisText = if (activeAnalysis.resorptionType == "Normal") {
                        "The radiographic analysis shows healthy dental structures on Tooth ${activeAnalysis.toothNumber}. The root structure shows 100% remaining integrity, with normal bone height and no signs of pathological or physiological resorption."
                    } else {
                        "The radiographic analysis reveals ${activeAnalysis.severity.lowercase()} root resorption affecting approximately ${activeAnalysis.affectedPercentage}% of the root length, primarily in the ${activeAnalysis.affectedRegion.lowercase()} region. The resorption pattern is consistent with ${activeAnalysis.resorptionType.lowercase()} root resorption."
                    }
                    
                    Text(
                        text = diagnosisText,
                        fontSize = 14.sp,
                        color = Color(0xFF475569),
                        lineHeight = 22.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Clinical Implications
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
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
                    Spacer(modifier = Modifier.height(20.dp))
                    
                    if (activeAnalysis.clinicalImplications.isEmpty()) {
                        BulletPoint("Root length and outline appear normal")
                        BulletPoint("Adequate periodontal ligament space")
                        BulletPoint("Successor bud development is synchronized")
                    } else {
                        activeAnalysis.clinicalImplications.forEach { implication ->
                            BulletPoint(implication)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Action Buttons
            Row(modifier = Modifier.fillMaxWidth()) {
                OutlinedButton(
                    onClick = onSeverityDetailsClick,
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp),
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0))
                ) {
                    Icon(Icons.Outlined.Description, contentDescription = null, modifier = Modifier.size(20.dp), tint = Color(0xFF0F172A))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Severity Details", color = Color(0xFF0F172A), fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.width(12.dp))
                Button(
                    onClick = onTreatmentPlanClick,
                    modifier = Modifier
                        .weight(1.2f)
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1396B2)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Treatment Plan", fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(Icons.AutoMirrored.Filled.ArrowForwardIos, contentDescription = null, modifier = Modifier.size(16.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedButton(
                onClick = onViewDecisionTreeClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0))
            ) {
                Text("View Decision Tree", color = Color(0xFF0F172A), fontWeight = FontWeight.Bold)
            }
            
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun BulletPoint(text: String) {
    Row(
        modifier = Modifier.padding(vertical = 4.dp),
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

@Preview(showBackground = true)
@Composable
fun DiagnosisResultPreview() {
    DiagnosisResultScreen()
}
