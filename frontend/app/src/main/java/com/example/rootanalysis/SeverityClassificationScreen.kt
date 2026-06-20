package com.example.rootanalysis

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Timeline
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
fun SeverityClassificationScreen(
    onBackClick: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Severity Classification", fontSize = 20.sp, fontWeight = FontWeight.Bold) },
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
            // Current Classification Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFBEB)),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFEF3C7))
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFF59E0B)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Timeline, contentDescription = null, tint = Color.White, modifier = Modifier.size(32.dp))
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text(
                                text = "Current Classification",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF0F172A)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Surface(
                                color = Color(0xFFD97706),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    text = "Grade 2 - Moderate",
                                    color = Color.White,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "Resorption Extent", fontSize = 14.sp, color = Color(0xFF64748B))
                        Text(text = "40%", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0F172A))
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    LinearProgressIndicator(
                        progress = { 0.4f },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(CircleShape),
                        color = Color(0xFF1396B2),
                        trackColor = Color(0xFFE2E8F0),
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Classification Scale",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0F172A)
            )
            Spacer(modifier = Modifier.height(16.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.5.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    ScaleItem("Grade 1", "Minimal", "Less than 25% root resorption", Color(0xFF22C55E))
                    Spacer(modifier = Modifier.height(12.dp))
                    ScaleItem("Grade 2", "Moderate", "25-50% root resorption", Color(0xFFF59E0B), isCurrent = true)
                    Spacer(modifier = Modifier.height(12.dp))
                    ScaleItem("Grade 3", "Severe", "50-75% root resorption", Color(0xFFF97316))
                    Spacer(modifier = Modifier.height(12.dp))
                    ScaleItem("Grade 4", "Extreme", "More than 75% root resorption", Color(0xFFEF4444))
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Assessment Details
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.5.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "Assessment Details",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0F172A)
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    AssessmentDetailRow("Root Length Remaining:", "60%")
                    AssessmentDetailRow("Affected Area:", "Apical Third")
                    AssessmentDetailRow("Pattern Type:", "Physiologic")
                    AssessmentDetailRow("Progression Rate:", "Normal")
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun ScaleItem(title: String, subtitle: String, description: String, color: Color, isCurrent: Boolean = false) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = Color.White,
        border = if (isCurrent) androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFF59E0B)) else androidx.compose.foundation.BorderStroke(0.5.dp, Color(0xFFF1F5F9))
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(modifier = Modifier.size(12.dp).clip(CircleShape).background(color))
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = title, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0F172A))
                    Spacer(modifier = Modifier.weight(1f))
                    Text(text = subtitle, fontSize = 12.sp, color = Color(0xFF94A3B8))
                    if (isCurrent) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Surface(
                            color = Color(0xFF0EA5E9),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = "Current",
                                color = Color.White,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                            )
                        }
                    }
                }
                Text(text = description, fontSize = 13.sp, color = Color(0xFF64748B))
            }
        }
    }
}

@Composable
fun AssessmentDetailRow(label: String, value: String) {
    Row(
        modifier = Modifier.padding(vertical = 6.dp).fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, fontSize = 15.sp, color = Color(0xFF64748B))
        Text(text = value, fontSize = 15.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF334155))
    }
}

@Preview(showBackground = true)
@Composable
fun SeverityClassificationPreview() {
    SeverityClassificationScreen()
}
