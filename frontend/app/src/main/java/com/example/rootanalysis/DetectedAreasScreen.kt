package com.example.rootanalysis

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetectedAreasScreen(
    onBackClick: () -> Unit = {},
    onContinueClick: () -> Unit = {},
    onViewDetailsClick: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detected Areas", fontSize = 20.sp, fontWeight = FontWeight.Bold) },
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
        ) {
            // Radiograph Visualization Area
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .padding(16.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFF1E293B)),
                contentAlignment = Alignment.Center
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    // Draw Area 1 (Red)
                    drawRoundRect(
                        color = Color(0xFFEF4444),
                        topLeft = Offset(100f, 50f),
                        size = Size(200f, 150f),
                        style = Stroke(width = 4f),
                        cornerRadius = androidx.compose.ui.geometry.CornerRadius(12f)
                    )
                    
                    // Draw Area 2 (Orange)
                    drawRoundRect(
                        color = Color(0xFFF59E0B),
                        topLeft = Offset(250f, 180f),
                        size = Size(220f, 180f),
                        style = Stroke(width = 4f),
                        cornerRadius = androidx.compose.ui.geometry.CornerRadius(12f)
                    )
                }
                
                // Labels for Canvas items (Simplified with absolute positioning for demo)
                Box(modifier = Modifier.fillMaxSize()) {
                    DetectionLabel("Area 1", Color(0xFFEF4444), Modifier.offset(x = 100.dp, y = 40.dp))
                    DetectionLabel("Area 2", Color(0xFFF59E0B), Modifier.offset(x = 180.dp, y = 140.dp))
                }
            }

            // Detected Resorption Areas Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.5.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "Detected Resorption Areas",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0F172A)
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    
                    DetectionItem(
                        title = "Apical third",
                        confidence = 87,
                        status = "Moderate",
                        statusBg = Color(0xFFFEF2F2),
                        statusText = Color(0xFFEF4444),
                        iconBg = Color(0xFFFEF2F2),
                        iconColor = Color(0xFFEF4444)
                    )
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    DetectionItem(
                        title = "Root surface",
                        confidence = 72,
                        status = "Mild",
                        statusBg = Color(0xFFFFFBEB),
                        statusText = Color(0xFFF59E0B),
                        iconBg = Color(0xFFFFFBEB),
                        iconColor = Color(0xFFF59E0B)
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Footer Buttons
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = onViewDetailsClick,
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp),
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0))
                ) {
                    Text("View Details", color = Color(0xFF0F172A), fontWeight = FontWeight.Bold)
                }
                
                Button(
                    onClick = onContinueClick,
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1396B2)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Continue", fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(Icons.AutoMirrored.Filled.ArrowForwardIos, contentDescription = null, modifier = Modifier.size(16.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun DetectionLabel(text: String, color: Color, modifier: Modifier) {
    Surface(
        modifier = modifier,
        color = color,
        shape = RoundedCornerShape(4.dp)
    ) {
        Text(
            text = text,
            color = Color.White,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
        )
    }
}

@Composable
fun DetectionItem(
    title: String,
    confidence: Int,
    status: String,
    statusBg: Color,
    statusText: Color,
    iconBg: Color,
    iconColor: Color
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = Color(0xFFF8FAFC)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(iconBg),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Warning, contentDescription = null, tint = iconColor, modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = title, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0F172A))
                Text(text = "Confidence: $confidence%", fontSize = 13.sp, color = Color(0xFF64748B))
            }
            Surface(
                color = statusBg,
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = status,
                    color = statusText,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DetectedAreasPreview() {
    DetectedAreasScreen()
}
