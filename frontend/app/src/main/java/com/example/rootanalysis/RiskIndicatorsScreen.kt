package com.example.rootanalysis

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.outlined.Info
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
fun RiskIndicatorsScreen(
    onBackClick: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Risk Indicators", fontSize = 20.sp, fontWeight = FontWeight.Bold) },
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
            // Individual Risk Factors Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.5.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "Individual Risk Factors",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0F172A)
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    RiskFactorItem("Accelerated Resorption", 0.35f, "Low", Color(0xFF10B981))
                    Spacer(modifier = Modifier.height(16.dp))
                    RiskFactorItem("Pathological Changes", 0.1f, "None", Color(0xFF10B981))
                    Spacer(modifier = Modifier.height(16.dp))
                    RiskFactorItem("Premature Exfoliation", 0.45f, "Low", Color(0xFF10B981))
                    Spacer(modifier = Modifier.height(16.dp))
                    RiskFactorItem("Successor Interference", 0.25f, "Minimal", Color(0xFF10B981))
                    Spacer(modifier = Modifier.height(16.dp))
                    RiskFactorItem("Space Loss", 0.3f, "Low", Color(0xFF10B981))
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Protective Factors Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.5.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Outlined.Info,
                            contentDescription = null,
                            tint = Color(0xFF1396B2),
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "Protective Factors",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0F172A)
                        )
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                    
                    ProtectiveFactorItem("Normal successor tooth development")
                    ProtectiveFactorItem("Age-appropriate resorption pattern")
                    ProtectiveFactorItem("Adequate bone support maintained")
                    ProtectiveFactorItem("No signs of infection or inflammation")
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Clinical Note Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFEFF6FF)),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFDBEAFE))
            ) {
                Row(modifier = Modifier.padding(16.dp)) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        tint = Color(0xFF1E40AF),
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(
                            text = "Clinical Note",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1E40AF)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "The low-risk profile supports conservative management. Continue routine monitoring and maintain good oral hygiene practices.",
                            fontSize = 14.sp,
                            color = Color(0xFF1E40AF),
                            lineHeight = 20.sp
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun RiskFactorItem(label: String, progress: Float, value: String, color: Color) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = label, fontSize = 14.sp, color = Color(0xFF475569))
            Text(text = value, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = color)
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
fun ProtectiveFactorItem(text: String) {
    Row(
        modifier = Modifier.padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.CheckCircle,
            contentDescription = null,
            tint = Color(0xFF10B981),
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(text = text, fontSize = 15.sp, color = Color(0xFF475569))
    }
}

@Preview(showBackground = true)
@Composable
fun RiskIndicatorsPreview() {
    RiskIndicatorsScreen()
}
