package com.example.rootanalysis

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Warning
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
fun TreatmentPlanScreen(
    analysis: com.example.rootanalysis.network.CaseAnalysisResponse? = null,
    onBackClick: () -> Unit = {},
    onGenerateReportClick: () -> Unit = {},
    onRiskFactorsClick: () -> Unit = {}
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
                title = { Text("Treatment Plan", fontSize = 20.sp, fontWeight = FontWeight.Bold) },
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
            // Action Items Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFEFF6FF))
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "Action Items:",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1E40AF)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    if (activeAnalysis.treatmentPlan.isEmpty()) {
                        BulletPointItem("No immediate action required.")
                        BulletPointItem("Schedule routine follow-up in 6 months.")
                    } else {
                        activeAnalysis.treatmentPlan.forEach { planItem ->
                            BulletPointItem(planItem)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Follow-up Schedule Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.5.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.CalendarMonth,
                            contentDescription = null,
                            tint = Color(0xFF1396B2),
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "Follow-up Schedule",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0F172A)
                        )
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                    
                    val timeFrame = when(activeAnalysis.severity.lowercase()) {
                        "severe" -> "2-4 weeks"
                        "moderate" -> "3 months"
                        "mild" -> "3-4 months"
                        else -> "6 months"
                    }
                    val exfoliationTime = "6-12 months"

                    ScheduleItem("Next Evaluation", "Clinical and radiographic assessment", timeFrame)
                    Spacer(modifier = Modifier.height(12.dp))
                    ScheduleItem("Expected Exfoliation", "Natural tooth loss timeline", exfoliationTime)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Warning Signs Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFBEB)),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFEF3C7))
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = null,
                            tint = Color(0xFFB45309),
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "Warning Signs to Monitor",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF92400E)
                        )
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                    
                    if (activeAnalysis.warningSigns.isEmpty()) {
                        WarningItem("Increased tooth mobility")
                        WarningItem("Local pain or chewing sensitivity")
                    } else {
                        activeAnalysis.warningSigns.forEach { warningItem ->
                            WarningItem(warningItem)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Footer Buttons
            Row(modifier = Modifier.fillMaxWidth()) {
                OutlinedButton(
                    onClick = onRiskFactorsClick,
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp),
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0))
                ) {
                    Text("Risk Factors", color = Color(0xFF0F172A), fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.width(12.dp))
                Button(
                    onClick = onGenerateReportClick,
                    modifier = Modifier
                        .weight(1.2f)
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1396B2)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Description, contentDescription = null, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Generate Report", fontWeight = FontWeight.Bold)
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun BulletPointItem(text: String) {
    Row(modifier = Modifier.padding(vertical = 4.dp), verticalAlignment = Alignment.Top) {
        Box(
            modifier = Modifier
                .padding(top = 8.dp)
                .size(6.dp)
                .clip(CircleShape)
                .background(Color(0xFF1E40AF))
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(text = text, fontSize = 15.sp, color = Color(0xFF1E40AF))
    }
}

@Composable
fun ScheduleItem(title: String, subtitle: String, badge: String) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = Color(0xFFF8FAFC)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = title, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0F172A))
                Text(text = subtitle, fontSize = 13.sp, color = Color(0xFF64748B))
            }
            Surface(
                color = if (badge == "3 months") Color(0xFF1396B2) else Color(0xFFF1F5F9),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = badge,
                    color = if (badge == "3 months") Color.White else Color(0xFF0F172A),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                )
            }
        }
    }
}

@Composable
fun WarningItem(text: String) {
    Text(
        text = text,
        fontSize = 15.sp,
        color = Color(0xFF92400E),
        modifier = Modifier.padding(vertical = 4.dp, horizontal = 36.dp)
    )
}

@Preview(showBackground = true)
@Composable
fun TreatmentPlanPreview() {
    TreatmentPlanScreen()
}
